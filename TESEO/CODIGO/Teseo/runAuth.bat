@ECHO OFF
mvn clean && mvn install && java -jar -Dspring.profiles.active=dev target\AuthenticatorSEE-1.0.jar