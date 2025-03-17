# My Maven Project

This is a simple Maven project that demonstrates the structure and usage of a basic Java application.

## Project Structure

```
projet
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── example
│   │   │           └── App.java
│   │   └── resources
│   │       └── application.properties
│   ├── test
│       └── java
│           └── com
│               └── example
│                   └── AppTest.java
├── pom.xml
└── README.md
```

## Prerequisites

- Java Development Kit (JDK) 8 or higher
- Apache Maven 3.6 or higher

## Building the Project

To build the project, navigate to the project directory and run the following command:

```
mvn clean install
```

## Running the Application

After building the project, you can run the application using the following command:

```
mvn exec:java
```

## Running Tests

To run the tests, use the following command:

```
mvn test
```

## Configuration

Configuration properties for the application can be found in the `src/main/resources/application.properties` file. You can modify this file to change application settings.

## License

This project is licensed under the MIT License. See the LICENSE file for more details.
