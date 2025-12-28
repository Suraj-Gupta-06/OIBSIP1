@echo off
REM Run Script for ATM Banking System (Windows)

echo ===============================================
echo      Starting ATM Banking System...           
echo ===============================================
echo.

REM Check if compiled
if not exist "bin" (
    echo Application not compiled. Running compilation...
    call compile.bat
    if %ERRORLEVEL% NEQ 0 (
        echo Compilation failed. Exiting...
        pause
        exit /b 1
    )
)

REM Check if bin directory is empty
dir /b bin 2>nul | findstr "^" >nul
if %ERRORLEVEL% NEQ 0 (
    echo Bin directory is empty. Running compilation...
    call compile.bat
    if %ERRORLEVEL% NEQ 0 (
        echo Compilation failed. Exiting...
        pause
        exit /b 1
    )
)

REM Run the application
java -cp bin com.atm.ATMApplication

pause
