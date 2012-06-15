USE lims;
ALTER TABLE `lims`.`biospecimen` CHANGE COLUMN `BIOSPECIMEN_ID` `BIOSPECIMEN_UID` VARCHAR(50) NOT NULL  
, DROP INDEX `fk_biospecimen_biospecimen_idx` 
, ADD INDEX `fk_biospecimen_biospecimen_idx` (`BIOSPECIMEN_UID` ASC) ;
