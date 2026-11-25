# System Architecture
## Architecture Style
Layered architecture.
## Layers
1. Model: Expense and ExpenseCategory.
2. Repository: ExpenseRepository and FileExpenseRepository.
3. Service: ExpenseService.
4. Presentation: Java Swing Main class.
5. Storage: Local CSV file at data/expenses.csv.
## Data Flow
User -> Swing GUI -> ExpenseService -> ExpenseRepository -> CSV File
## Design Principles
- Encapsulation
- Abstraction
- Separation of concerns
- Interface-based programming
- Immutable expense objects
## Concurrency
Repository methods use synchronized methods to protect operations within the same application process. This does not provide cross-process file locking.