CREATE TABLE `spark`.`data_source_type` (
  `ID` INT(11) NOT NULL AUTO_INCREMENT,
  `NAME` VARCHAR(45) NULL,
  `DESCRIPTION` VARCHAR(255) NULL,
  PRIMARY KEY (`ID`));

ALTER TABLE `spark`.`data_source` 
CHANGE COLUMN `TYPE` `TYPE` INT(11) NULL DEFAULT NULL ,
ADD INDEX `fk_data_source_type_id_idx` (`TYPE` ASC);
ALTER TABLE `spark`.`data_source` 
ADD CONSTRAINT `fk_data_source_type_id`
  FOREIGN KEY (`TYPE`)
  REFERENCES `spark`.`data_source_type` (`ID`)
  ON DELETE SET NULL
  ON UPDATE CASCADE;



