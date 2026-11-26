@echo off
cd /d "%~dp0"
call mvn -q compile
if errorlevel 1 (
    echo Maven compilation failed.
    pause
    exit /b 1
)
javac -cp "target\classes" -d "target\classes" Main.java
if errorlevel 1 (
    echo Main.java compilation failed.
    pause
    exit /b 1
)
java -cp "target\classes" Main
