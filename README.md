# hd-spring-mowers

# run tests only 
    mvn clean test

# Run with mvn exec plugin
    mvn exec:java -Dexec.args="--inputFile=src/test/resources/input.txt --outputFile=./batchOutput.txt"

## Application parameters
    --inputFile is mandatory
    --outputFile is optional; if missing will write to stdout

# How to package to JAR
    mvn clean package

# Execute JAR directly 
Package to JAR then run:

    java -jar target/sb-les-tondeuses-0.0.1-SNAPSHOT.jar --inputFile=src/test/resources/input.txt

# package the app to a Docker image
Package to JAR then run:

    docker build -t the-spring-mowers:latest .

# Run the Docker image you just created
    docker run the-spring-mowers:latest