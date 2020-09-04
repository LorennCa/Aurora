@ECHO OFF
mvn clean && mvn install && java -jar -Dspring.profiles.active=dev target\FileService-1.0.jar