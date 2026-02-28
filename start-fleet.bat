@echo off
REM Fleet Admin - Quick Start Script
REM 
REM This script starts all infrastructure services and runs the application
REM
REM Usage: start-fleet.bat

echo ========================================
echo Fleet Admin - Quick Start
echo ========================================
echo.

REM Check if Docker is running
docker info >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Docker is not running or not installed.
    echo Please start Docker Desktop and try again.
    pause
    exit /b 1
)

echo Step 1: Starting infrastructure services (PostgreSQL, Kafka, Redis)...
docker-compose up -d

echo.
echo Step 2: Waiting for services to be healthy...
timeout /t 15 /nobreak > nul

echo.
echo Step 3: Starting Spring Boot Backend...
start "Fleet Backend" cmd /k "cd /d %~dp0fleet-backend && mvnw spring-boot:run"

echo.
echo Step 4: Starting Frontend (will open in browser)...
start "Fleet Frontend" cmd /k "cd /d %~dp0fleet-frontend && npm run dev"

echo.
echo ========================================
echo All services starting!
echo ========================================
echo.
echo Services:
echo   - PostgreSQL:  localhost:5432
echo   - Kafka:       localhost:9092
echo   - Redis:       localhost:6379
echo   - Backend:     localhost:8080
echo   - Frontend:    localhost:5173
echo.
echo NOTE: Wait ~30 seconds for backend to fully start
echo.
pause
