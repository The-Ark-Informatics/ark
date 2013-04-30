INSERT INTO `study`.`field_type` (`ID`, `NAME`) VALUES ('4', 'LOOKUP');

ALTER TABLE `study`.`field_type` ADD COLUMN `VISIBLE` TINYINT NOT NULL DEFAULT '1'  ;

UPDATE `study`.`field_type` SET `VISIBILE`='0' WHERE `ID`='4';

ALTER TABLE `study`.`field_type` CHANGE COLUMN `VISIBILE` `VISIBLE` TINYINT(4) NOT NULL DEFAULT '1'  ;

/* change all of these to lookup fields */
UPDATE `reporting`.`demographic_field` SET `FIELD_TYPE_ID`='4' WHERE `ID`='10';
UPDATE `reporting`.`demographic_field` SET `FIELD_TYPE_ID`='4' WHERE `ID`='11';
UPDATE `reporting`.`demographic_field` SET `FIELD_TYPE_ID`='4' WHERE `ID`='13';
UPDATE `reporting`.`demographic_field` SET `FIELD_TYPE_ID`='4' WHERE `ID`='29';
UPDATE `reporting`.`demographic_field` SET `FIELD_TYPE_ID`='4' WHERE `ID`='30';
UPDATE `reporting`.`demographic_field` SET `FIELD_TYPE_ID`='4' WHERE `ID`='31';
UPDATE `reporting`.`demographic_field` SET `FIELD_TYPE_ID`='4' WHERE `ID`='32';
UPDATE `reporting`.`demographic_field` SET `FIELD_TYPE_ID`='4' WHERE `ID`='33';
UPDATE `reporting`.`demographic_field` SET `FIELD_TYPE_ID`='4' WHERE `ID`='34';
UPDATE `reporting`.`demographic_field` SET `FIELD_TYPE_ID`='4' WHERE `ID`='35';
UPDATE `reporting`.`demographic_field` SET `FIELD_TYPE_ID`='4' WHERE `ID`='36';
UPDATE `reporting`.`demographic_field` SET `FIELD_TYPE_ID`='4' WHERE `ID`='37';
UPDATE `reporting`.`demographic_field` SET `FIELD_TYPE_ID`='4' WHERE `ID`='38';
UPDATE `reporting`.`demographic_field` SET `FIELD_TYPE_ID`='4' WHERE `ID`='41';
UPDATE `reporting`.`demographic_field` SET `FIELD_TYPE_ID`='4' WHERE `ID`='42';
UPDATE `reporting`.`demographic_field` SET `FIELD_TYPE_ID`='4' WHERE `ID`='48';
UPDATE `reporting`.`demographic_field` SET `FIELD_TYPE_ID`='4' WHERE `ID`='26';
UPDATE `reporting`.`demographic_field` SET `FIELD_TYPE_ID`='4' WHERE `ID`='27';
UPDATE `reporting`.`demographic_field` SET `FIELD_TYPE_ID`='4' WHERE `ID`='51';
UPDATE `reporting`.`demographic_field` SET `FIELD_TYPE_ID`='4' WHERE `ID`='46';


