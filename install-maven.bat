@echo off
REM =============================================================================
REM Maven Installation Script for Windows
REM This script downloads and installs Maven automatically
REM =============================================================================

echo.
echo Installing Maven for Windows...
echo.

REM Check if Java is installed
java -version >nul 2>&1
if errorlevel 1 (
    echo ERROR: Java is not installed!
    echo Please install Java 17 first from: https://www.oracle.com/java/technologies/downloads/
    pause
    exit /b 1
)

echo ✓ Java found

REM Check if Maven folder exists
if exist "C:\Maven" (
    echo Maven folder already exists at C:\Maven
) else (
    echo Creating Maven directory...
    mkdir C:\Maven
)

REM Download Maven if not exists
if not exist "C:\Maven\apache-maven-3.9.6" (
    echo Downloading Maven 3.9.6...
    cd C:\Maven

    REM Using PowerShell to download
    powershell -Command "(New-Object System.Net.ServicePointManager).SecurityProtocol = [System.Net.ServicePointManager]::SecurityProtocol -bor [System.Net.SecurityProtocolType]::Tls12; (New-Object System.Net.WebClient).DownloadFile('https://dlcdn.apache.org/maven/maven-3/3.9.6/binaries/apache-maven-3.9.6-bin.zip', 'maven.zip')"

    if exist "maven.zip" (
        echo Extracting Maven...
        powershell -Command "Expand-Archive -Path 'maven.zip' -DestinationPath '.' -Force"
        del maven.zip
        echo ✓ Maven extracted
    ) else (
        echo ERROR: Failed to download Maven
        pause
        exit /b 1
    )
) else (
    echo ✓ Maven already installed
)

REM Set MAVEN_HOME and add to PATH
setx MAVEN_HOME "C:\Maven\apache-maven-3.9.6"
setx PATH "%PATH%;C:\Maven\apache-maven-3.9.6\bin"

echo.
echo ✓ Installation complete!
echo.
echo IMPORTANT: Close and reopen your Git Bash terminal for changes to take effect
echo.
echo To verify installation, run:
echo   mvn -v
echo.
pause

