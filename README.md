# Parking Lot Application
Represents solution for the [problem](problem-statement.md).

## Prerequisites

* Java 11

## How to build (with tests)

```
./mvnw clean install
```

## How to run

```
java -jar target/parking-lot-1.0-SNAPSHOT.jar input-example.txt
```

## Usage

In order to view usage help message run the program without args

```
java -jar target/parking-lot-1.0-SNAPSHOT.jar
```

You may also specify `-h` or `--help` option

```
java -jar target/parking-lot-1.0-SNAPSHOT.jar -h
```

Usage:

```
Usage: ParkingLotApplication <file>
Prints Parking lot output to STDOUT
<file>     The text file with commands to process:

Command    args regexp
create     (?<createSize>[1-9]\d*)
leave      (?<leavePlate>[a-zA-Z\-\d]+)(?:\s+)(?<leaveHours>[1-9]\d*)
park       (?<parkPlate>[a-zA-Z\-\d]+)
status
```

## Configuration

Default application setting file is [application.yaml](src/main/resources/application.yaml)
You can use your own configuration by specify it the following way:

```
java -jar target/parking-lot-1.0-SNAPSHOT.jar input-example.txt --spring.config.location=<new_configuration_file>
```

You may also override any particular properties

```
java -jar target/parking-lot-1.0-SNAPSHOT.jar input-example.txt --app.calculator.fixed-price=100 --app.processor.plate-regexp=\[A-Z\\-\\d\]+
```

## Logging
Application logs are written to `parking-lot-app.log` file.
