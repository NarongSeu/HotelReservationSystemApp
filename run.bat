@echo off
REM Hotel Reservation System Launcher Script for Windows

echo Hotel Reservation System Launcher
echo =================================

REM Check if Java is installed
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ Java is not installed or not in PATH
    echo Please install Java 11 or higher
    pause
    exit /b 1
)

echo ✅ Java detected

REM Check if Maven is available
mvn -version >nul 2>&1
if %errorlevel% equ 0 (
    echo ✅ Maven detected - Using Maven to run
    echo Compiling and running application...
    mvn clean compile exec:java -Dexec.mainClass="com.hotel.HotelReservationSystemApp"
) else (
    echo ⚠️  Maven not found - Checking for compiled JAR...
    
    REM Look for JAR file
    for /r target %%i in (*jar-with-dependencies.jar) do (
        echo ✅ Found JAR file: %%i
        echo Running application...
        java -jar "%%i"
        goto :end
    )
    
    echo ❌ No JAR file found and Maven not available
    echo Please either:
    echo 1. Install Maven and run: mvn clean package
    echo 2. Or compile manually with javac
    pause
    exit /b 1
)

:end
pause
