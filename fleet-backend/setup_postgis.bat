@echo off
REM PostGIS Setup Script for Fleet Logistics
REM 
REM This script runs the PostGIS setup SQL commands in PostgreSQL
REM 
REM Prerequisites:
REM 1. PostgreSQL must be installed and running
REM 2. psql must be in your PATH
REM 3. Database credentials must match application.properties
REM
REM Usage: setup_postgis.bat
REM

echo ========================================
echo PostGIS Setup for Fleet Logistics
echo ========================================
echo.

REM Database connection settings (match application.properties)
set DB_HOST=localhost
set DB_PORT=5432
set DB_NAME=fleet_logistics
set DB_USER=postgres
set DB_PASSWORD=art3ZH9gMzxjDR1L

echo Step 1: Creating database if not exists...
psql -h %DB_HOST% -p %DB_PORT% -U %DB_USER% -c "CREATE DATABASE %DB_NAME%;" 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo Database might already exist, continuing...
)

echo.
echo Step 2: Enabling PostGIS extension...
psql -h %DB_HOST% -p %DB_PORT% -U %DB_USER% -d %DB_NAME% -c "CREATE EXTENSION IF NOT EXISTS postgis;"

echo.
echo Step 3: Adding spatial columns to tables...
psql -h %DB_HOST% -p %DB_PORT% -U %DB_USER% -d %DB_NAME% -f "%~dp0setup_postgis.sql"

echo.
echo Step 4: Verifying PostGIS installation...
psql -h %DB_HOST% -p %DB_PORT% -U %DB_USER% -d %DB_NAME% -c "SELECT postgis_full_version();"

echo.
echo ========================================
echo PostGIS setup complete!
echo ========================================
echo.
echo You can now run the Spring Boot application.
pause
