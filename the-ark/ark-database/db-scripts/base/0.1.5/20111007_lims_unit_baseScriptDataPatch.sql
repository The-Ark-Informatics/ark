/*
 * Base 0.1.5 script data patch to fix a data reference table missed by that base script!
 * Only required to be applied if the database was started from the base 0.1.5 scripts.
 * (NB: do not require any code changes that would affect ark-0.1.5-SNAPSHOT.war)
 */

USE `lims`;

-- This [lims].[unit] table was accidentally missed in the dump
INSERT INTO `unit` (`ID`, `NAME`, `DESCRIPTION`) VALUES (1,'mm',NULL),(2,'ug/L',NULL),(3,'Years',NULL),(4,'mµ/l',NULL),(5,'bpm',NULL),(6,'g/L',NULL),(7,'fL',NULL),(8,'feet',NULL),(9,'IU/L',NULL),(10,'kg',NULL),(11,'U',NULL),(12,'µV',NULL),(13,'Days',NULL),(14,'mg/l',NULL),(15,'Age',NULL),(16,'cm',NULL),(17,'mL',NULL),(18,'Iµ/mL',NULL),(19,'pg',NULL),(20,'row 2',NULL),(21,'grams',NULL),(22,'pred',NULL),(23,'Gy',NULL),(24,'Hours',NULL),(25,'µ/L',NULL),(26,'Mins',NULL),(27,'%',NULL),(28,'mS',NULL),(29,'mm/hr',NULL),(30,'mg/dl',NULL),(31,'mn',NULL),(33,'mg/L',NULL),(34,'kgm2',NULL),(35,'mm Hg',NULL),(36,'kg/m2',NULL),(37,'Pipes',NULL),(38,'L',NULL),(39,'S',NULL),(40,'m',NULL),(41,'fl',NULL),(42,'hours',NULL),(43,'mm/hg',NULL);

