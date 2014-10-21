INTRODUCTION:
-- The Casper project was created to migrate the existing file attachments in the Ark database to server local file system. 

-- This tool only copy the existing subject file attachments and correspondence file attachments to local file system in the execution machine.

-- Later the database administrator can permanently remove these attachments from the Ark database.

STEPS:
(1) Patch the Ark database with following scripts
/trunk/ark-database/db-scripts/patch/1.2.0/20140915_add_file_id_to_subject_file_table_and_enable_null_for_payload_column.sql
/trunk/ark-database/db-scripts/patch/1.2.0/20141006_add_checksum_and_fileid_columns_to_correspondence.sql      

(2) Update src/main/resources/casper.properties 

(3) Create casper JAR with dependencies:

mvn clean compile assembly:single

(4) Execute JAR file: 

java -jar casper-1.0-SNAPSHOT-jar-with-dependencies.jar