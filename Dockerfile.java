# Stage 1: Build stage
FROM hitaishi0622/ping:latest AS build

# Set the working directory
WORKDIR /app

# Update the package list and install OpenJDK 17 and necessary packages
RUN apt-get update && apt-get install -y openjdk-17-jdk

# Copy the source code to the container
COPY . /app

# Compile the Java application
RUN javac Test.java

# Stage 2: Runtime stage
FROM ubuntu:latest

# Update the package list and install OpenJDK 17 and other necessary packages
RUN apt-get update && apt-get install -y \
    openjdk-17-jdk \
    curl \
    iputils-ping \
    net-tools \
    iproute2

# Verify Java installation
RUN java -version

# Set the working directory
WORKDIR /app

# Copy the compiled Java application from the build stage
COPY --from=build /app /app

# Command to run the Java application and display the IP address
CMD /bin/bash -c "echo 'Container IP address:' && ip addr show eth0 | grep 'inet\b' | awk '{print $2}' | cut -d/ -f1 && java Test"
