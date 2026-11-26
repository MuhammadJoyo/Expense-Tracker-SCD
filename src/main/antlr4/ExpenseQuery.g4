grammar ExpenseQuery;

@header { package com.expensetracker.query.generated; }

query       : expression EOF ;
expression  : term (OR term)* ;
term        : factor (AND factor)* ;
factor      : LPAREN expression RPAREN
            | field operator value ;
field       : AMOUNT | CATEGORY | TITLE ;
operator    : GT | GE | LT | LE | EQ | NE | CONTAINS ;
value       : NUMBER | IDENTIFIER | STRING ;

AMOUNT      : 'amount' ;
CATEGORY    : 'category' ;
TITLE       : 'title' ;
AND         : [aA][nN][dD] ;
OR          : [oO][rR] ;
CONTAINS    : 'contains' ;
GE          : '>=' ; LE : '<=' ; EQ : '==' ; NE : '!=' ;
GT          : '>' ; LT : '<' ;
LPAREN      : '(' ; RPAREN : ')' ;
NUMBER      : [0-9]+ ('.' [0-9]+)? ;
IDENTIFIER  : [a-zA-Z_][a-zA-Z_0-9-]* ;
STRING      : '"' (~["\\] | '\\' .)* '"' | '\'' (~['\\] | '\\' .)* '\'' ;
WS          : [ \t\r\n]+ -> skip ;
