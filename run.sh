#!/bin/bash
# Hotel Reservation System Launcher Script for Unix/Linux/macOS

echo "Hotel Reservation System Launcher"
echo "================================="

# Check if Java is installed
if ! command -v java &> /dev/null; then
    echo "❌ Java is not installed or not in PATH"
    echo "Please install Java 11 or higher"
    exit 1
fi

# Check Java version
JAVA_VERSION=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2 | cut -d'.' -f1-2)
echo "Java version detected: $JAVA_VERSION"

# Check if Maven is available
if command -v mvn &> /dev/null; then
    echo "✅ Maven detected - Using Maven to run"
    echo "Compiling and running application..."
    mvn clean compile exec:java -Dexec.mainClass="com.hotel.HotelReservationSystemApp"
else
    echo "⚠️  Maven not found - Checking for compiled JAR..."
    
    # Look for JAR file
    JAR_FILE=$(find target -name "*jar-with-dependencies.jar" 2>/dev/null | head -n 1)
    
    if [ -n "$JAR_FILE" ]; then
        echo "✅ Found JAR file: $JAR_FILE"
        echo "Running application..."
        java -jar "$JAR_FILE"
    else
        echo "❌ No JAR file found and Maven not available"
        echo "Please either:"
        echo "1. Install Maven and run: mvn clean package"
        echo "2. Or compile manually with javac"
        exit 1
    fi
fi
