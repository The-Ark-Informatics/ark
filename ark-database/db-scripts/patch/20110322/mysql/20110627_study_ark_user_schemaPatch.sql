use study;
ALTER TABLE `study`.`ark_user` DROP FOREIGN KEY `ark_user_ibfk_1` ;
ALTER TABLE `study`.`ark_user` DROP COLUMN `STUDY_ID` 
, DROP INDEX `STUDY_ID` ;

