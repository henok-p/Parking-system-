# Basic Parking System

A Java-based parking management system that demonstrates core concepts of resource management, state tracking, and database integration using PostgreSQL.

## Features

- Parking spot management with database persistence
- Vehicle parking and unparking functionality
- Real-time parking availability tracking
- Command-line interface for system interaction
- Data Access Object (DAO) pattern implementation

## Project Structure

```
basic-parking-system/
├── src/
│   └── main/
│       └── java/
│           └── com/
│               └── parking/
│                   ├── model/      # Domain models
│                   ├── dao/        # Data Access Objects
│                   ├── db/         # Database management
│                   └── ui/         # User interface
└── pom.xml
```

## Prerequisites

- Java 17 or higher
- PostgreSQL 14 or higher
- Maven 3.8 or higher

## Setup Instructions

1. Clone the repository
2. Create a PostgreSQL database named `parking_db`
3. Update database connection details in `DatabaseManager.java`
4. Run `mvn clean install` to build the project
5. Execute the application using `java -cp target/basic-parking-system-1.0-SNAPSHOT.jar com.parking.App`

## Usage

The application provides a command-line interface with the following options:
1. Park Vehicle
2. Unpark Vehicle
3. Display Available Spots
4. Display Occupied Spots
5. Show Total Available Spots
6. Exit

## Technologies Used

- Java
- PostgreSQL
- JDBC
- Maven
- Java Swing (for future GUI implementation)
