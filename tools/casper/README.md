INTRODUCTION:
-- The Casper project was created to migrate the existing file attachments in the Ark database to server local file system. 

-- This tool only copy the existing subject file attachments and correspondence file attachments to local file system in the execution machine.

-- Later the database administrator can permanently remove these attachments from the Ark database.

STEPS:
(1) Update src/main/resources/casper.properties with Database connection settings and base directory location.

(2) Create casper JAR with dependencies:

mvn clean compile assembly:single

(3) Execute JAR file: Please make sure before you execute this you must have the user permission to the folder to create sub directories(/opt/data/arkFileData).

java -jar casper-1.0-SNAPSHOT-jar-with-dependencies.jar

(4) Set permissions to tomcat user to access the base directory and it's sub directories.According to the step 3 the permiss may set for the current logged in user not tomcat7
	
Ex: chown -R tomcat7:tomcat7 /opt/data/arkFileData
