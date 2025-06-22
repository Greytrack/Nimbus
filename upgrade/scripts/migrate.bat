@echo off
setlocal

REM Obtain the directory where the current script is located
set "basepath=%~dp0"

REM find the bin directory
set "current_dir=%basepath%"
:find_bin_dir
if exist "%current_dir%\bin" (
    set "rootpath=%current_dir%"
) else (
    set "parent_dir=%current_dir%.."
    if "%parent_dir%"=="%current_dir%" (
        echo Error: bin directory not found.
        exit /b 1
    )
    set "current_dir=%parent_dir%"
    goto find_bin_dir
)

echo rootpath: %rootpath%

set "migrations_dir=%rootpath%\src\main\resources\flyway\migration"

REM config file path
set "CONFIG_FILE=%basepath%.env"

REM read the config file
if exist "%CONFIG_FILE%" (
    for /f "usebackq tokens=*" %%a in ("%CONFIG_FILE%") do (
        set "%%a"
    )
) else (
    echo config file not found: %CONFIG_FILE%
    exit /b 1
)

REM select the executable file according to the system architecture
if /i "%PROCESSOR_ARCHITECTURE%"=="AMD64" (
    set "EXECUTABLE=%rootpath%\bin\migrate_windows_amd64.exe"
) else if /i "%PROCESSOR_ARCHITECTURE%"=="ARM64" (
    set "EXECUTABLE=%rootpath%\bin\migrate_windows_arm64.exe"
) else (
    echo Error: Unsupported architecture: %PROCESSOR_ARCHITECTURE%
    exit /b 1
)

if not exist "%EXECUTABLE%" (
    echo Error: Executable not found: %EXECUTABLE%
    exit /b 1
)

:menu
cls
echo === Main Menu ===
echo Please select the command to execute:
echo 1. Reset database
echo 2. Execute migration
echo 9. Exit
set /p choice=Please enter the serial number: 

if "%choice%"=="1" (
    goto reset_database
) else if "%choice%"=="2" (
    goto execute_migration
) else if "%choice%"=="9" (
    echo Exiting program...
    exit /b 0
) else (
    echo invalid choice
    timeout /t 2 >nul
    goto menu
)

:reset_database
cls
echo === Reset Database ===
echo Please select the environment:
echo 1. local
echo 2. dev
echo 3. test
echo 4. prod
echo 5. custom
echo 9. Back to main menu
set /p env_choice=Please enter the serial number: 

if "%env_choice%"=="1" (
    set "db_url=%local%"
) else if "%env_choice%"=="2" (
    set "db_url=%dev%"
) else if "%env_choice%"=="3" (
    set "db_url=%test%"
) else if "%env_choice%"=="4" (
    set "db_url=%prod%"
) else if "%env_choice%"=="5" (
    set /p db_url=Please enter the database link: 
) else if "%env_choice%"=="9" (
    goto menu
) else (
    echo invalid choice
    timeout /t 2 >nul
    goto reset_database
)

"%EXECUTABLE%" reset --db="%db_url%"
echo.
echo Press any key to return to main menu...
pause >nul
goto menu

:execute_migration
cls
echo === Execute Migration ===
echo Please select the environment:
echo 1. local
echo 2. dev
echo 3. test
echo 4. prod
echo 5. custom
echo 9. Back to main menu
set /p env_choice=Please enter the serial number: 

if "%env_choice%"=="1" (
    set "db_url=%local%"
) else if "%env_choice%"=="2" (
    set "db_url=%dev%"
) else if "%env_choice%"=="3" (
    set "db_url=%test%"
) else if "%env_choice%"=="4" (
    set "db_url=%prod%"
) else if "%env_choice%"=="5" (
    set /p db_url=Please enter the database link: 
) else if "%env_choice%"=="9" (
    goto menu
) else (
    echo invalid choice
    timeout /t 2 >nul
    goto execute_migration
)

set /p version=Please enter the version number (default is empty): 
set /p dir=Please enter the SQL script directory (default is %migrations_dir%): 
if "%dir%"=="" set "dir=%migrations_dir%"
set /p dry_run=Whether to test the database script ([y/n]default is n): 
if "%dry_run%"=="" set "dry_run=n"

if /i "%dry_run%"=="y" (
    "%EXECUTABLE%" exec %version% --db="%db_url%" --dir="%dir%" --dry-run
) else (
    "%EXECUTABLE%" exec %version% --db="%db_url%" --dir="%dir%"
)
echo.
echo Press any key to return to main menu...
pause >nul
goto menu 