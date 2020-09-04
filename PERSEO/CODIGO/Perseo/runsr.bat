@ECHO OFF
REM requires Java 8 o later; modify active profile accordingly.
mvn clean && mvn install && java -jar -Dspring.profiles.active=dev target\ServiceRegistry-1.0.jar