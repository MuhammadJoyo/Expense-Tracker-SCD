# Code Review Checklist

Use this checklist during peer/instructor review and record the reviewer and date.

| Review item | What to inspect | Result |
|---|---|---|
| Input validation | Invalid title, amount and category are rejected | Pending peer review |
| Model invariants | Expense ID/title/amount/category/date remain valid | Constructor checks present |
| Immutability | Expense fields are final; no setters | Pass |
| Equality | equals/hashCode use the same ID field | Pass |
| Repository abstraction | Service depends on ExpenseRepository, not file implementation | Pass |
| Persistence | CSV read/write and malformed data handling | Needs additional tests |
| Concurrency | synchronized methods protect in-process repository operations | Partial |
| UI thread | Swing UI is created with SwingUtilities.invokeLater | Pass |
| Query parser | Invalid fields/operators/quotes fail with a clear exception | Add more negative tests |
| Build | Maven test and package complete successfully | Run before submission |

Reviewer: ____________________
Date: ____________________
Findings / actions: ____________________
