@echo off
cd /d "%~dp0"
call mvn -q compile
if errorlevel 1 pause & exit /b 1
javac -cp target\classes -d target\classes Main.java
if errorlevel 1 pause & exit /b 1
java -cp target\classes Main
pause
