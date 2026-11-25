# UML and Design
## Class Diagram
```mermaid
classDiagram
    class Expense {
      -int id
      -String title
      -double amount
      -String category
      -LocalDate date
      +getId()
      +getTitle()
      +getAmount()
      +equals()
      +hashCode()
    }
    class ExpenseRepository {
      <<interface>>
      +add()
      +findAll()
      +findById()
      +delete()
      +update()
    }
    class FileExpenseRepository {
      +add()
      +findAll()
      +findById()
      +delete()
      +update()
    }
    class ExpenseService {
      +addExpense()
      +getAll()
      +deleteExpense()
      +updateExpense()
      +search()
      +getTotal()
    }
    ExpenseRepository <|.. FileExpenseRepository
    ExpenseService --> ExpenseRepository
    ExpenseService --> Expense
    Main --> ExpenseService
```
## Use Case Summary
Actor: Local User
- Add expense
- View expenses
- Edit expense
- Delete expense
- Search expense
- Filter expenses
- View total spending
## Data Design
Expense fields:
ID, Title, Amount, Category, Date.
Storage: Local CSV file.