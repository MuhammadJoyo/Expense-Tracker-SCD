# Java Expense Tracker

A desktop-based Expense Tracker developed in Java using Swing.

## Features
- Add, update and delete expenses.
- View and search expenses.
- Filter expenses by category.
- Smart Search with advanced query expressions.
- CSV-based data storage.
- Input validation.
- Expense summaries using Java Streams.
- Background task processing.
- TCP localhost networking demonstration.

## Technologies
- Java
- Java Swing
- Maven
- JUnit 5
- ANTLR 4
- Git and GitHub

## Requirements
- Java JDK
- Maven
- Windows

## Run the Application
Open PowerShell in the project directory and execute:

    .\run.bat

## Smart Search Examples

    amount > 500
    category == FOOD
    amount > 500 AND category == FOOD
    (category == FOOD OR category == EDUCATION) AND amount >= 500

The `query:` prefix is optional.

## Run Tests

    mvn clean test

## Documentation
See the `docs` folder for:
- Problem Analysis
- Project Proposal
- SRS
- Architecture
- UML
- Testing Report
- SCD Concepts
- Code Review Checklist

## Limitations
- Uses local CSV storage.
- Designed for local single-user use.
- Networking is demonstrated using localhost.

## Author
Software Construction and Development Academic Project