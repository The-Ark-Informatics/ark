USE lims;

-- Biotransaction are against a biospecimen, and the biospecimen already knows which study it is attached to.
-- So no need have the study on this table
ALTER TABLE `lims`.`bio_transaction` DROP COLUMN `SOURCESTUDY_ID` , DROP COLUMN `STUDY_ID` ;
ALTER TABLE `lims`.`bio_transaction` DROP COLUMN `STUDY` ;

-- Support soft-deletes (now mapped to a Boolean on the entity)
ALTER TABLE `lims`.`bio_transaction` CHANGE COLUMN `DELETED` `DELETED` TINYINT(4) NULL DEFAULT NULL  ;
