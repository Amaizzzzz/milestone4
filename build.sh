#!/bin/bash

# Clean and package the application using Maven
mvn clean package

# Copy the generated WAR file to Tomcat's webapps directory
cp target/game-database.war /Users/gucenzhang/tomcat/apache-tomcat-10.1.18/webapps/

# Copy servlet dependencies
cp /Users/gucenzhang/tomcat/apache-tomcat-10.1.18/lib/servlet-api.jar tomcat-webapp/WEB-INF/lib/
cp /Users/gucenzhang/tomcat/apache-tomcat-10.1.18/lib/jsp-api.jar tomcat-webapp/WEB-INF/lib/

# Compile Java classes
javac -cp "tomcat-webapp/WEB-INF/lib/*" \
    -d tomcat-webapp/WEB-INF/classes \
    src/main/java/cs5200project/model/*.java \
    src/main/java/cs5200project/dal/*.java \
    src/main/java/cs5200project/servlet/*.java

# Create WAR file
cd tomcat-webapp
jar -cvf ../game-database.war *
cd .. 