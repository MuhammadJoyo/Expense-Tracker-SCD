# Testing Report
## Test Framework
JUnit 5 with Maven Surefire.
## Executed Tests
| Test | Expected result |
|---|---|
| Valid expense creation | Expense created |
| Negative amount | Rejected |
| Empty title | Rejected |
| Equality using same ID | Equal objects |
## Verified Execution
Command: mvn clean test
Result: 4 tests run, 0 failures, 0 errors, 0 skipped.
## Additional Manual Tests
The Swing application was launched successfully. The dashboard displayed a saved expense and calculated the total.
Add/edit/delete/search/filter and restart persistence should be manually verified before final submission.