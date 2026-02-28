@echo off
REM Fleet Admin - Stop Script
REM 
REM This script stops all services and cleans up

echo ========================================
echo Fleet Admin - Stopping Services
echo ========================================
echo.

echo Stopping Docker containers...
docker-compose down

echo.
echo ========================================
echo All services stopped!
echo ========================================
