@echo off
echo Starting Learning Tracker Application...

:: Run the PowerShell script with execution policy bypass
powershell -ExecutionPolicy Bypass -File "%~dp0start_app.ps1"

pause
