# Software Requirements Specification
## 1. Introduction
Expense Tracker manages personal expense records through a desktop interface.
## 2. Functional Requirements
- FR1: The system shall allow users to add expenses.
- FR2: The system shall display saved expenses.
- FR3: The system shall allow expense editing.
- FR4: The system shall allow expense deletion.
- FR5: The system shall search by title or category.
- FR6: The system shall filter by category.
- FR7: The system shall calculate total expenses.
- FR8: The system shall persist records locally.
## 3. Non-Functional Requirements
- Input validation must prevent invalid expense records.
- The interface should be easy to use.
- Expense data should remain available after restarting.
- Repository operations should protect shared in-process access.
- The application should be maintainable through layered code.
## 4. Users
The current version is designed for a single local user.
## 5. Constraints
The current version uses local file storage and does not provide authentication, cloud synchronization or network-based multi-user access.