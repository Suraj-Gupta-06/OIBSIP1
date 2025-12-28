@echo off
REM Compilation Script for ATM Banking System (Windows)

echo ===============================================
echo    ATM Banking System - Compilation Script   
echo ===============================================
echo.

REM Create bin directory if it doesn't exist
if not exist "bin" (
    echo Creating bin directory...
    mkdir bin
)

REM Compile all Java files
echo Compiling Java source files...
javac -d bin src\com\atm\*.java src\com\atm\model\*.java src\com\atm\service\*.java src\com\atm\repository\*.java src\com\atm\exception\*.java src\com\atm\util\*.java

REM Check compilation status
if %ERRORLEVEL% EQU 0 (
    echo.
    echo [SUCCESS] Compilation successful!
    echo.
    echo To run the application, use:
    echo   run.bat
    echo.
    echo Or manually:
    echo   java -cp bin com.atm.ATMApplication
    echo.
) else (
    echo.
    echo [ERROR] Compilation failed. Please check the errors above.
    echo.
)

pause
