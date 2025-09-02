# Learning Tracker Application - Startup Script
Write-Host "Starting Learning Tracker Application..." -ForegroundColor Green

# Check if Java is installed
try {
    $javaVersion = java -version 2>&1
    Write-Host "Java is installed: $javaVersion" -ForegroundColor Green
} catch {
    Write-Host "Error: Java is not installed or not in PATH. Please install Java and try again." -ForegroundColor Red
    exit 1
}

# Check if Node.js is installed
try {
    $nodeVersion = node -v
    Write-Host "Node.js is installed: $nodeVersion" -ForegroundColor Green
} catch {
    Write-Host "Error: Node.js is not installed or not in PATH. Please install Node.js and try again." -ForegroundColor Red
    exit 1
}

# Define the backend and frontend directories
$backendDir = Join-Path $PSScriptRoot "backend"
$frontendDir = Join-Path $PSScriptRoot "frontend"

# Function to check if a port is in use
function Test-PortInUse {
    param (
        [int]$Port
    )
    $portInUse = $false
    $connections = netstat -ano | Select-String -Pattern ":$Port\s"
    
    if ($connections -ne $null) {
        $portInUse = $true
    }
    
    return $portInUse
}

# Check if required ports are available
$backendPort = 8082
$frontendPort = 5173

if (Test-PortInUse -Port $backendPort) {
    Write-Host "Error: Port $backendPort is already in use. Please close the application using this port and try again." -ForegroundColor Red
    exit 1
}

if (Test-PortInUse -Port $frontendPort) {
    Write-Host "Error: Port $frontendPort is already in use. Please close the application using this port and try again." -ForegroundColor Red
    exit 1
}

# Start the backend
Write-Host "`nStarting backend server (Spring Boot)..." -ForegroundColor Cyan
Set-Location -Path $backendDir

# Start the backend in a new PowerShell window
$backendCommand = "Set-Location -Path '$backendDir'; Write-Host 'Starting backend server on port $backendPort...' -ForegroundColor Cyan; ./gradlew bootRun; Read-Host 'Press Enter to exit'"
Start-Process powershell -ArgumentList "-Command $backendCommand" -WindowStyle Normal

# Wait for backend to start
Write-Host "Waiting for backend to start..." -ForegroundColor Yellow
$backendStarted = $false
$maxAttempts = 30
$attempt = 0

while (-not $backendStarted -and $attempt -lt $maxAttempts) {
    $attempt++
    Write-Host "Checking if backend is running (attempt $attempt/$maxAttempts)..."
    try {
        $response = Invoke-WebRequest -Uri "http://localhost:$backendPort/api/test" -Method GET -TimeoutSec 2 -ErrorAction SilentlyContinue
        if ($response.StatusCode -eq 200) {
            $backendStarted = $true
            Write-Host "Backend server is running!" -ForegroundColor Green
            Write-Host "Response: $($response.Content)" -ForegroundColor Green
        }
    } catch {
        Start-Sleep -Seconds 2
    }
}

if (-not $backendStarted) {
    Write-Host "Warning: Could not confirm if backend server is running. Proceeding anyway..." -ForegroundColor Yellow
}

# Start the frontend
Write-Host "`nStarting frontend server (React)..." -ForegroundColor Cyan
Set-Location -Path $frontendDir

# Check if node_modules exists and run npm install if needed
if (-not (Test-Path -Path "node_modules")) {
    Write-Host "Installing frontend dependencies..." -ForegroundColor Yellow
    npm install
}

# Start the frontend in a new PowerShell window
$frontendCommand = "Set-Location -Path '$frontendDir'; Write-Host 'Starting frontend server on port $frontendPort...' -ForegroundColor Cyan; npm run dev; Read-Host 'Press Enter to exit'"
Start-Process powershell -ArgumentList "-Command $frontendCommand" -WindowStyle Normal

# Reset location to script directory
Set-Location -Path $PSScriptRoot

# Wait for frontend to start
Write-Host "Waiting for frontend to start..." -ForegroundColor Yellow
$frontendStarted = $false
$maxAttempts = 15
$attempt = 0

while (-not $frontendStarted -and $attempt -lt $maxAttempts) {
    $attempt++
    Write-Host "Checking if frontend is running (attempt $attempt/$maxAttempts)..."
    try {
        $response = Invoke-WebRequest -Uri "http://localhost:$frontendPort" -Method GET -TimeoutSec 1 -ErrorAction SilentlyContinue
        if ($response.StatusCode -eq 200) {
            $frontendStarted = $true
            Write-Host "Frontend server is running!" -ForegroundColor Green
        }
    } catch {
        Start-Sleep -Seconds 2
    }
}

if (-not $frontendStarted) {
    Write-Host "Warning: Could not confirm if frontend server is running. Proceeding anyway..." -ForegroundColor Yellow
}

# Launch the application in the default browser
Write-Host "`nLaunching application in your default browser..." -ForegroundColor Green
Start-Process "http://localhost:$frontendPort"

Write-Host "`nLearning Tracker Application is now running!" -ForegroundColor Green
Write-Host "Backend API: http://localhost:$backendPort" -ForegroundColor Cyan
Write-Host "Frontend UI: http://localhost:$frontendPort" -ForegroundColor Cyan
Write-Host "API Documentation: http://localhost:$backendPort/swagger-ui.html" -ForegroundColor Cyan

Write-Host "`nTo stop the application, close the PowerShell windows running the backend and frontend servers." -ForegroundColor Yellow
