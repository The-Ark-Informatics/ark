USE lims;
ALTER TABLE `lims`.`collection` RENAME TO  `lims`.`biocollection` ;
ALTER TABLE `lims`.`biospecimen` DROP FOREIGN KEY `fk_biospecimen_collection` ;
ALTER TABLE `lims`.`biospecimen` CHANGE COLUMN `COLLECTION_ID` `BIOCOLLECTION_ID` INT(11) NOT NULL  , 
  ADD CONSTRAINT `fk_biospecimen_biocollection`
  FOREIGN KEY (`BIOCOLLECTION_ID` )
  REFERENCES `lims`.`biocollection` (`ID` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION
, DROP INDEX `fk_biospecimen_collection` 
, ADD INDEX `fk_biospecimen_biocollection` (`BIOCOLLECTION_ID` ASC) ;