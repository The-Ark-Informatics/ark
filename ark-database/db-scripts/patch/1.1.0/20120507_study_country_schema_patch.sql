USE study;

UPDATE `study`.`country` SET country_code = 'UK' WHERE name = 'UNITED KINGDOM';
UPDATE `study`.`country` SET country_code = 'CA' WHERE name = 'CANADA';

ALTER TABLE `study`.`country` 
ADD COLUMN `ALPHA_3_CODE` VARCHAR(45) NOT NULL  AFTER `COUNTRY_CODE` , 
ADD COLUMN `NUMERIC_CODE` VARCHAR(45) NOT NULL  AFTER `ALPHA_3_CODE` , 
ADD COLUMN `OFFICIAL_NAME` VARCHAR(45) NOT NULL COMMENT 'Correct Name, Probably not used often\n'  AFTER `NUMERIC_CODE` , 
CHANGE COLUMN `ID` `ID` INT(11) NOT NULL AUTO_INCREMENT COMMENT 'LEGACY ID, \nkeep table structures similar\n'  , 
CHANGE COLUMN `NAME` `NAME` VARCHAR(100) NOT NULL COMMENT 'Common / \nColloquial Name'  , 
DROP COLUMN `DESCRIPTION` , 
CHANGE COLUMN `COUNTRY_CODE` `COUNTRY_CODE` VARCHAR(2) NOT NULL COMMENT 'Official as used in local match ups, unique\n';

ALTER TABLE `study`.`country_state` 
ADD COLUMN `TYPE` VARCHAR(45) NOT NULL 
	COMMENT 'what they call a \'state\', \'province\' , etc\ncan be multiple for a country\neg; au has state and territory' , 
ADD COLUMN `NAME` VARCHAR(255) NOT NULL  AFTER `TYPE` , 
ADD COLUMN `CODE` VARCHAR(45) NOT NULL  AFTER `NAME` , 
ADD COLUMN `SHORT_NAME` VARCHAR(56) NOT NULL  AFTER `CODE` ;

UPDATE `study`.`country_state` SET short_name = state;

ALTER TABLE `study`.`country_state` RENAME TO  `study`.`state` ;

-- Update existing used Australian States
update study.state SET type = "State", code = "AU-NSW", name = "New South Wales" WHERE country_id = (select id from country where country_code="AU" ) AND short_name = 'NSW';
update study.state SET type = "State", code = "AU-QLD", name = "Queensland" WHERE country_id = (select id from country where country_code="AU" ) AND short_name = 'QLD';
update study.state SET type = "State", code = "AU-SA", name = "South Australia" WHERE country_id = (select id from country where country_code="AU" AND short_name = 'SA';
update study.state SET type = "State", code = "AU-TAS", name = "Tasmania" WHERE country_id = (select id from country where country_code="AU" AND short_name = 'TAS';
update study.state SET type = "State", code = "AU-VIC", name = "Victoria" WHERE country_id = (select id from country where country_code="AU" AND short_name = 'VIC';
update study.state SET type = "State", code = "AU-WA", name = "Western Australia" WHERE country_id = (select id from country where country_code="AU" AND short_name = 'WA';        
update study.state SET type = "Territory", code = "AU-ACT", name = "Australian Capital Territory" WHERE country_id = (select id from country where country_code="AU" AND short_name = 'ACT';
update study.state SET type = "Territory", code = "AU-NT", name = "Northern Territory" WHERE country_id = (select id from country where country_code="AU" AND short_name = 'NT';

ALTER TABLE `study`.`state` DROP COLUMN state;

ALTER TABLE `study`.`address` DROP FOREIGN KEY `fk_address_state` ;
ALTER TABLE `study`.`address` CHANGE COLUMN `COUNTRY_STATE_ID` `STATE_ID` INT(11) NULL DEFAULT NULL  , 
  ADD CONSTRAINT `fk_address_state`
  FOREIGN KEY (`STATE_ID` )
  REFERENCES `study`.`state` (`ID` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION
, DROP INDEX `fk_address_state` 
, ADD INDEX `fk_address_state` USING BTREE (`STATE_ID` ASC) ;
