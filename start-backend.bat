@echo off
REM =============================
REM Start Backend API Script
REM =============================

REM Navigate to backend folder relative to this script’s location
cd /d "%~dp0api\GameGuesserAPI"

REM Run the API in a new command window
start cmd /k "dotnet run"
