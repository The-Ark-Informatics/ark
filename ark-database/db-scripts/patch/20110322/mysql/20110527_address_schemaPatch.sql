USE study;

ALTER TABLE `study`.`address` DROP FOREIGN KEY `fk_address_preferred_mailing_address_id` ;
ALTER TABLE `study`.`address` CHANGE COLUMN `PREFERRED_MAILING_ADDRESS_ID` `PREFERRED_MAILING_ADDRESS` INT(11) NOT NULL  
, DROP INDEX `fk_address_preferred_mailing_address_id` 
, ADD INDEX `fk_address_preferred_mailing_address_id` USING BTREE (`PREFERRED_MAILING_ADDRESS` ASC) ;

-- Patch data (FK ID for 'No' = 2)
UPDATE `study`.`address` AS a
SET a.PREFERRED_MAILING_ADDRESS = 0
WHERE a.PREFERRED_MAILING_ADDRESS = 2;
