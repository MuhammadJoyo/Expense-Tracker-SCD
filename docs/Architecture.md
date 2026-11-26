# System Architecture
## Architecture Overview
The Expense Tracker follows a layered architecture.
## Main Components
1. **Presentation Layer**
   - Java Swing GUI.
   - Handles user input and displays expenses.
2. **Service Layer**
   - ExpenseService manages application operations.
   - Supports adding, updating, deleting, searching and filtering expenses.
   - Uses Java Streams for map, filter and reduce operations.
3. **Repository Layer**
   - ExpenseRepository defines the repository interface.
   - FileExpenseRepository stores and retrieves expenses from a CSV file.
4. **Model Layer**
   - Expense represents an expense record.
   - ExpenseCategory represents expense categories.
   - Expense objects are immutable.
5. **Validation and Query Layer**
   - ExpenseValidator validates input.
   - ExpenseQuery processes advanced search expressions.
   - ExpenseQuery.g4 defines the query grammar and ANTLR generates parser classes.
6. **Concurrency Layer**
   - ConcurrentTaskProcessor uses a BlockingQueue and worker thread.
   - ReentrantLock protects the completed task counter.
7. **Networking Layer**
   - ExpenseServer demonstrates TCP communication on localhost.
   - It responds to a PING message with PONG.
8. **Recursion Layer**
   - RecursiveExpenseSearch searches expense records recursively.
## Application Flow
User
  -> Swing GUI
  -> ExpenseService
  -> ExpenseRepository
  -> FileExpenseRepository
  -> CSV File
## Design Notes
- Repository interface separates storage from business logic.
- Immutable Expense objects reduce accidental state changes.
- Validation protects against invalid input.
- JUnit tests verify important modules.
- Concurrency and networking modules demonstrate SCD concepts.
## Limitations
- Storage uses a local CSV file instead of a database.
- Networking is a localhost demonstration.
- The application is intended for local single-user use.