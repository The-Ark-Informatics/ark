ALTER TABLE `study`.`country` 
ADD COLUMN `ALPHA_3_CODE` VARCHAR(45) NOT NULL  AFTER `COUNTRY_CODE` , 
ADD COLUMN `NUMERIC_CODE` VARCHAR(45) NOT NULL  AFTER `ALPHA_3_CODE` , 
ADD COLUMN `OFFICIAL_NAME` VARCHAR(45) NOT NULL COMMENT 'Correct Name, Probably not used often\n'  AFTER `NUMERIC_CODE` , 
CHANGE COLUMN `ID` `ID` INT(11) NOT NULL AUTO_INCREMENT COMMENT 'LEGACY ID, \nkeep table structures similar\n'  , 
CHANGE COLUMN `NAME` `NAME` VARCHAR(100) NOT NULL COMMENT 'Common / \nColloquial Name'  , 
CHANGE COLUMN `DESCRIPTION` `DESCRIPTION` VARCHAR(250) NOT NULL DEFAULT 'Description not provided'  , 
CHANGE COLUMN `COUNTRY_CODE` `COUNTRY_CODE` VARCHAR(2) NOT NULL COMMENT 'Official as used in local match ups, unique\n'
-- , 
-- ADD UNIQUE INDEX `COUNTRY_CODE_UNIQUE` (`COUNTRY_CODE` ASC) 
;



ALTER TABLE `study`.`country_state` 
ADD COLUMN `TYPE` VARCHAR(45) NOT NULL 
	COMMENT 'what they call a \'state\', \'province\' , etc\ncan be multiple for a country\neg; au has state and territory'  AFTER `STATE` , 
ADD COLUMN `NAME` VARCHAR(255) NOT NULL  AFTER `TYPE` , 
ADD COLUMN `CODE` VARCHAR(45) NOT NULL  AFTER `NAME` , 
CHANGE COLUMN `STATE` `STATE` VARCHAR(100) NOT NULL 
	COMMENT 'a legacy field  which may be kept as a colloquialism reference or removed and named something more detailed'  ;

