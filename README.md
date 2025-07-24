# Hotel Reservation System

A comprehensive hotel management system built with Java Swing and MySQL using MySQL Connector J 9.3.0.

## Features

### 🏨 Complete Hotel Management
- **Room Management** - Add, edit, delete rooms with availability tracking
- **Guest Management** - Complete guest profile management with search
- **Reservation System** - Full booking management with check-in/out
- **Billing Module** - Comprehensive billing with payment tracking
- **Dashboard** - Real-time statistics and quick actions

### 🔧 Technical Features
- **MySQL Database** - Complete relational database with foreign keys
- **Java Swing GUI** - Professional desktop interface
- **CRUD Operations** - Full Create, Read, Update, Delete functionality
- **Data Validation** - Input validation and error handling
- **Auto Database Setup** - Automatic table creation and sample data

## Prerequisites

- **Java 11 or higher**
- **Maven 3.6+**
- **MySQL Server 8.0+**
- **MySQL Connector J 9.3.0** (included in dependencies)

## Database Setup

1. **Install MySQL Server**
   \`\`\`bash
   # On Ubuntu/Debian
   sudo apt update
   sudo apt install mysql-server
   
   # On Windows - Download from MySQL website
   # On macOS - Use Homebrew
   brew install mysql
   \`\`\`

2. **Start MySQL Service**
   \`\`\`bash
   # Linux
   sudo systemctl start mysql
   
   # macOS
   brew services start mysql
   
   # Windows - Use Services or MySQL Workbench
   \`\`\`

3. **Create Database User (Optional)**
   \`\`\`sql
   CREATE USER 'hotel_user'@'localhost' IDENTIFIED BY 'hotel_password';
   GRANT ALL PRIVILEGES ON hotel_db.* TO 'hotel_user'@'localhost';
   FLUSH PRIVILEGES;
   \`\`\`

## Installation & Setup

### Method 1: Using Maven

1. **Clone the repository**
   \`\`\`bash
   git clone <repository-url>
   cd hotel-reservation-system
   \`\`\`

2. **Configure Database Connection**
   Edit `src/main/java/com/hotel/util/DatabaseConnection.java`:
   \`\`\`java
   private static final String URL = "jdbc:mysql://localhost:3306/hotel_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
   private static final String USERNAME = "root";  // Your MySQL username
   private static final String PASSWORD = "password";  // Your MySQL password
   \`\`\`

3. **Build and Run**
   \`\`\`bash
   # Compile the project
   mvn clean compile
   
   # Run the application
   mvn exec:java -Dexec.mainClass="com.hotel.HotelReservationSystemApp"
   
   # Or build JAR and run
   mvn clean package
   java -jar target/hotel-reservation-system-1.0.0-jar-with-dependencies.jar
   \`\`\`

### Method 2: Using IDE

1. **Import Project**
   - Open your IDE (IntelliJ IDEA, Eclipse, NetBeans)
   - Import as Maven project
   - Wait for dependencies to download

2. **Add MySQL Connector J 9.3.0**
   - If not using Maven, download `mysql-connector-j-9.3.0.jar`
   - Add to project classpath/libraries

3. **Configure Database**
   - Update database credentials in `DatabaseConnection.java`

4. **Run Application**
   - Run `HotelReservationSystemApp.main()` method

## Database Schema

The application automatically creates these tables:

\`\`\`sql
-- Rooms table
CREATE TABLE Rooms (
    room_id INT PRIMARY KEY AUTO_INCREMENT,
    room_number VARCHAR(10) NOT NULL UNIQUE,
    room_type VARCHAR(50),
    price DECIMAL(10,2),
    status VARCHAR(20) DEFAULT 'Available'
);

-- Guests table
CREATE TABLE Guests (
    guest_id INT PRIMARY KEY AUTO_INCREMENT,
    full_name VARCHAR(100),
    phone VARCHAR(20),
    id_passport VARCHAR(50),
    address TEXT
);

-- Reservations table
CREATE TABLE Reservations (
    reservation_id INT PRIMARY KEY AUTO_INCREMENT,
    guest_id INT,
    room_id INT,
    check_in_date DATE,
    check_out_date DATE,
    status VARCHAR(20) DEFAULT 'Confirmed',
    FOREIGN KEY (guest_id) REFERENCES Guests(guest_id),
    FOREIGN KEY (room_id) REFERENCES Rooms(room_id)
);

-- Additional tables: Billing, CheckInOut, RoomServiceItems, etc.
\`\`\`

## Usage Guide

### 1. Starting the Application
- Launch the application using one of the methods above
- The splash screen will show while initializing the database
- If database connection fails, check your MySQL server and credentials

### 2. Dashboard
- View real-time statistics (total rooms, available rooms, guests, etc.)
- Quick navigation to different modules

### 3. Room Management
- **Add Room**: Fill form and click "Add Room"
- **Edit Room**: Select room from table, modify details, click "Update Room"
- **Delete Room**: Select room and click "Delete Room"
- **Room Status**: Available, Occupied, Maintenance, Out of Order

### 4. Guest Management
- **Add Guest**: Enter guest details and click "Add Guest"
- **Search**: Use the search functionality to find guests
- **Update/Delete**: Select guest from table to modify or remove

### 5. Reservation Management
- **New Reservation**: Select guest and room, set dates, click "Add Reservation"
- **Check-In**: Select reservation and click "Check In"
- **Check-Out**: Select reservation and click "Check Out"
- **Status Tracking**: Confirmed → Checked-In → Checked-Out

### 6. Billing (Coming Soon)
- Generate bills for completed stays
- Track payments and methods
- Print/export bills

## Troubleshooting

### Database Connection Issues
\`\`\`
Error: "Database connection failed"
Solutions:
1. Ensure MySQL server is running
2. Check username/password in DatabaseConnection.java
3. Verify MySQL port (default: 3306)
4. Check firewall settings
\`\`\`

### MySQL Connector Issues
\`\`\`
Error: "MySQL Connector J driver not found"
Solutions:
1. Ensure mysql-connector-j-9.3.0.jar is in classpath
2. If using Maven, run: mvn clean install
3. Check Maven dependencies in pom.xml
\`\`\`

### Java Version Issues
\`\`\`
Error: "Unsupported class file major version"
Solutions:
1. Ensure Java 11+ is installed
2. Check JAVA_HOME environment variable
3. Verify Maven is using correct Java version
\`\`\`

## Project Structure

\`\`\`
src/
├── main/
│   └── java/
│       └── com/
│           └── hotel/
│               ├── HotelReservationSystemApp.java  # Main application
│               ├── dao/                            # Data Access Objects
│               │   ├── GuestDAO.java
│               │   ├── RoomDAO.java
│               │   └── ReservationDAO.java
│               ├── gui/                            # GUI Components
│               │   ├── MainDashboard.java
│               │   └── panels/
│               │       ├── DashboardPanel.java
│               │       ├── RoomPanel.java
│               │       ├── GuestPanel.java
│               │       ├── ReservationPanel.java
│               │       └── BillingPanel.java
│               ├── model/                          # Data Models
│               │   ├── Room.java
│               │   ├── Guest.java
│               │   ├── Reservation.java
│               │   └── Bill.java
│               └── util/                           # Utilities
│                   └── DatabaseConnection.java
├── pom.xml                                        # Maven configuration
└── README.md                                      # This file
\`\`\`

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/new-feature`)
3. Commit your changes (`git commit -am 'Add new feature'`)
4. Push to the branch (`git push origin feature/new-feature`)
5. Create a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Support

For support or questions:
1. Check the troubleshooting section above
2. Create an issue in the repository
3. Contact the development team

---

**Note**: This is a demonstration project showcasing Java Swing GUI development with MySQL database integration using the latest MySQL Connector J 9.3.0.
