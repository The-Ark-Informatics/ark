-- Adds new BIOCOLLECTION_UID column, storing NAME data in it, adn adds index
ALTER TABLE `lims`.`biocollection` ADD COLUMN `BIOCOLLECTION_UID` VARCHAR(50) NULL  AFTER `ID` ;
UPDATE `lims`.`biocollection` SET BIOCOLLECTION_UID = NAME;
ALTER TABLE `lims`.`biocollection` CHANGE COLUMN `NAME` `NAME` VARCHAR(50) NULL  ;
ALTER TABLE `lims`.`biocollection` CHANGE COLUMN `BIOCOLLECTION_UID` `BIOCOLLECTION_UID` VARCHAR(50) NOT NULL;
ALTER TABLE `lims`.`biocollection` ADD INDEX `fk_collection_biocollection_uid_idx` (`BIOCOLLECTION_UID` ASC);