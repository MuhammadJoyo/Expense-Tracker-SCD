package com.expensetracker.query;

import com.expensetracker.model.Expense;
import java.util.*;

/**
 * Expense Query Language (EQL), a small language for filtering expenses.
 * Examples: amount > 500 AND category == FOOD
 *           (category == FOOD OR category == BILLS) AND amount <= 5000
 * Its recursive AST (And/Or/Comparison) is a recursive data type.
 */
public final class ExpenseQuery {
    private ExpenseQuery() {}
    public static boolean matches(String source, Expense expense) {
        Parser p = new Parser(source);
        Expr e = p.parseOr();
        p.need(Type.END);
        return e.test(expense);
    }
    private interface Expr { boolean test(Expense e); }
    private record Compare(String field, String op, String value) implements Expr {
        public boolean test(Expense e) {
            if (field.equals("amount")) {
                double a = e.getAmount(), b = Double.parseDouble(value);
                return switch (op) {
                    case ">" -> a>b; case ">=" -> a>=b; case "<" -> a<b;
                    case "<=" -> a<=b; case "==" -> a==b; case "!=" -> a!=b;
                    default -> false;
                };
            }
            String actual = field.equals("title") ? e.getTitle() : e.getCategory();
            int c = actual.compareToIgnoreCase(value);
            return switch (op) {
                case "==" -> c==0; case "!=" -> c!=0; case "contains" -> actual.toLowerCase(Locale.ROOT).contains(value.toLowerCase(Locale.ROOT));
                default -> false;
            };
        }
    }
    private record And(Expr left, Expr right) implements Expr {
        public boolean test(Expense e) { return left.test(e) && right.test(e); }
    }
    private record Or(Expr left, Expr right) implements Expr {
        public boolean test(Expense e) { return left.test(e) || right.test(e); }
    }
    private enum Type { WORD, VALUE, OP, LP, RP, END }
    private record Token(Type type, String text) {}
    private static final class Parser {
        private final List<Token> t; private int i=0;
        Parser(String s) { if (s==null || s.isBlank()) throw new IllegalArgumentException("Query is empty."); t=lex(s); }
        Expr parseOr() {
            Expr e=parseAnd();
            while (word("OR")) e=new Or(e,parseAnd());
            return e;
        }
        Expr parseAnd() {
            Expr e=primary();
            while (word("AND")) e=new And(e,primary());
            return e;
        }
        Expr primary() {
            if (take(Type.LP)) { Expr e=parseOr(); need(Type.RP); return e; }
            String field=need(Type.WORD).text.toLowerCase(Locale.ROOT);
            String op=need(Type.OP).text;
            String value=needValue().text;
            if (!Set.of("amount","category","title").contains(field)) throw new IllegalArgumentException("Use amount, category or title.");
            if (field.equals("amount")) {
                if (!Set.of(">","<",">=","<=","==","!=").contains(op)) throw new IllegalArgumentException("Invalid amount operator.");
                try { Double.parseDouble(value); } catch (NumberFormatException x) { throw new IllegalArgumentException("Amount must be numeric."); }
            } else if (!Set.of("==","!=","contains").contains(op)) throw new IllegalArgumentException("Use ==, != or contains for text.");
            return new Compare(field,op,value);
        }
        boolean word(String w) { if (peek().type==Type.WORD && peek().text.equalsIgnoreCase(w)) { i++; return true; } return false; }
        boolean take(Type x) { if(peek().type==x){i++;return true;} return false; }
        Token need(Type x) { Token v=peek(); if(v.type!=x) throw new IllegalArgumentException("Unexpected query token: "+v.text); i++; return v; }
        Token needValue() { Token v=peek(); if(v.type!=Type.VALUE && v.type!=Type.WORD) throw new IllegalArgumentException("Expected a query value."); i++; return v; }
        Token peek(){return t.get(i);}
        static List<Token> lex(String s) {
            List<Token> out=new ArrayList<>(); int p=0;
            while(p<s.length()) {
                char c=s.charAt(p); if(Character.isWhitespace(c)){p++;continue;}
                if(c=='('){out.add(new Token(Type.LP,"("));p++;continue;}
                if(c==')'){out.add(new Token(Type.RP,")"));p++;continue;}
                if(c=='\''||c=='"'){char q=c;int a=++p;while(p<s.length()&&s.charAt(p)!=q)p++;if(p==s.length())throw new IllegalArgumentException("Unclosed quote.");out.add(new Token(Type.VALUE,s.substring(a,p++)));continue;}
                if("=!<>".indexOf(c)>=0){int a=p++;if(p<s.length()&&s.charAt(p)=='=')p++;out.add(new Token(Type.OP,s.substring(a,p)));continue;}
                if(Character.isLetterOrDigit(c)||c=='_'||c=='.'||c=='-'){int a=p++;while(p<s.length()&&(Character.isLetterOrDigit(s.charAt(p))||"_.-".indexOf(s.charAt(p))>=0))p++;String w=s.substring(a,p);if(w.equalsIgnoreCase("contains"))out.add(new Token(Type.OP,"contains"));else out.add(new Token(Character.isDigit(w.charAt(0))?Type.VALUE:Type.WORD,w));continue;}
                throw new IllegalArgumentException("Invalid query character: "+c);
            }
            out.add(new Token(Type.END,"<end>")); return out;
        }
    }
}
