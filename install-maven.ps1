# Maven Installation Script for Windows (PowerShell)
# Run as Administrator: Right-click PowerShell → Run as administrator

# Check Java installation
Write-Host "Checking Java installation..." -ForegroundColor Cyan
$javaVersion = Invoke-Expression "java -version 2>&1"
if ($LASTEXITCODE -ne 0) {
    Write-Host "ERROR: Java is not installed!" -ForegroundColor Red
    Write-Host "Download Java 17 from: https://www.oracle.com/java/technologies/downloads/" -ForegroundColor Yellow
    exit 1
}
Write-Host "✓ Java found: $(($javaVersion | Select-Object -First 1))" -ForegroundColor Green
Write-Host ""

# Create Maven directory
$mavenPath = "C:\Maven"
$mavenHome = "$mavenPath\apache-maven-3.9.6"

Write-Host "Setting up Maven in: $mavenHome" -ForegroundColor Cyan

if (-not (Test-Path $mavenPath)) {
    New-Item -ItemType Directory -Path $mavenPath -Force | Out-Null
    Write-Host "✓ Created directory: $mavenPath" -ForegroundColor Green
}

# Check if Maven already installed
if (Test-Path $mavenHome) {
    Write-Host "✓ Maven already installed at: $mavenHome" -ForegroundColor Green
} else {
    # Download Maven
    Write-Host "Downloading Apache Maven 3.9.6..." -ForegroundColor Cyan
    $url = "https://dlcdn.apache.org/maven/maven-3/3.9.6/binaries/apache-maven-3.9.6-bin.zip"
    $zipPath = "$mavenPath\maven.zip"

    try {
        [System.Net.ServicePointManager]::SecurityProtocol = [System.Net.SecurityProtocolType]::Tls12
        $client = New-Object System.Net.WebClient
        $client.DownloadFile($url, $zipPath)
        Write-Host "✓ Downloaded Maven" -ForegroundColor Green
    } catch {
        Write-Host "ERROR: Failed to download Maven" -ForegroundColor Red
        Write-Host $_.Exception.Message -ForegroundColor Red
        exit 1
    }

    # Extract Maven
    Write-Host "Extracting Maven..." -ForegroundColor Cyan
    Expand-Archive -Path $zipPath -DestinationPath $mavenPath -Force
    Remove-Item $zipPath -Force
    Write-Host "✓ Maven extracted" -ForegroundColor Green
}

# Set environment variables
Write-Host "Setting environment variables..." -ForegroundColor Cyan
[Environment]::SetEnvironmentVariable("MAVEN_HOME", $mavenHome, [EnvironmentVariableTarget]::User)
$currentPath = [Environment]::GetEnvironmentVariable("PATH", [EnvironmentVariableTarget]::User)
if ($currentPath -notlike "*$mavenHome\bin*") {
    [Environment]::SetEnvironmentVariable("PATH", "$currentPath;$mavenHome\bin", [EnvironmentVariableTarget]::User)
}
Write-Host "✓ Environment variables set" -ForegroundColor Green

# Update current session
$env:MAVEN_HOME = $mavenHome
$env:PATH = "$env:PATH;$mavenHome\bin"

Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "✓ Maven installation complete!" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "To use Maven immediately, run:" -ForegroundColor Yellow
Write-Host "  `$env:PATH += ';$mavenHome\bin'" -ForegroundColor White
Write-Host ""
Write-Host "To verify installation:" -ForegroundColor Yellow
Write-Host "  mvn -v" -ForegroundColor White
Write-Host ""
Write-Host "IMPORTANT: Close and reopen Git Bash for changes to take effect" -ForegroundColor Yellow
Write-Host ""
Write-Host "Then navigate to your project and run:" -ForegroundColor Yellow
Write-Host "  cd C:\dev-shop" -ForegroundColor White
Write-Host "  mvn clean package -DskipTests" -ForegroundColor White
Write-Host "  docker-compose up" -ForegroundColor White

