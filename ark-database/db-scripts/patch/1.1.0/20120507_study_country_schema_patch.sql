USE study;
ALTER TABLE `study`.`country` 
ADD COLUMN `ALPHA_3_CODE` VARCHAR(45) NOT NULL  AFTER `COUNTRY_CODE` , 
ADD COLUMN `NUMERIC_CODE` VARCHAR(45) NOT NULL  AFTER `ALPHA_3_CODE` , 
ADD COLUMN `OFFICIAL_NAME` VARCHAR(45) NOT NULL COMMENT 'Correct Name, Probably not used often\n'  AFTER `NUMERIC_CODE` , 
CHANGE COLUMN `ID` `ID` INT(11) NOT NULL AUTO_INCREMENT COMMENT 'LEGACY ID, \nkeep table structures similar\n'  , 
CHANGE COLUMN `NAME` `NAME` VARCHAR(100) NOT NULL COMMENT 'Common / \nColloquial Name'  , 
DROP COLUMN `DESCRIPTION` , 
CHANGE COLUMN `COUNTRY_CODE` `COUNTRY_CODE` VARCHAR(2) NOT NULL COMMENT 'Official as used in local match ups, unique\n';


ALTER TABLE `study`.`country_state` 
DROP COLUMN state,
ADD COLUMN `TYPE` VARCHAR(45) NOT NULL 
	COMMENT 'what they call a \'state\', \'province\' , etc\ncan be multiple for a country\neg; au has state and territory' , 
ADD COLUMN `NAME` VARCHAR(255) NOT NULL  AFTER `TYPE` , 
ADD COLUMN `CODE` VARCHAR(45) NOT NULL  AFTER `NAME` , 
ADD COLUMN `SHORT_NAME` VARCHAR(56) NOT NULL  AFTER `CODE` ;


ALTER TABLE `study`.`country_state` RENAME TO  `study`.`state` ;

ALTER TABLE `study`.`address` DROP FOREIGN KEY `fk_address_state` ;
ALTER TABLE `study`.`address` CHANGE COLUMN `COUNTRY_STATE_ID` `STATE_ID` INT(11) NULL DEFAULT NULL  , 
  ADD CONSTRAINT `fk_address_state`
  FOREIGN KEY (`STATE_ID` )
  REFERENCES `study`.`state` (`ID` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION
, DROP INDEX `fk_address_state` 
, ADD INDEX `fk_address_state` USING BTREE (`STATE_ID` ASC) ;
