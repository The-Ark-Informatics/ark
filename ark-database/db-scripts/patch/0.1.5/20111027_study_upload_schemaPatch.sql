use study;
ALTER TABLE `study`.`upload` ADD COLUMN `UPLOAD_TYPE` VARCHAR(45) NULL COMMENT 'Not defined as a FK. This model is borrowing the idea from phenotypic old upload class.'  AFTER `ARK_FUNCTION_ID` ;

