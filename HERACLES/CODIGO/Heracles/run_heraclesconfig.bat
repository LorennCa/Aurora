REM Modifique este archivo de acuerdo con el nombre del servicio.
@ECHO OFF
mvn clean && mvn install && java -jar -Dspring.profiles.active=dev target\SeeConfigService-1.0.jar