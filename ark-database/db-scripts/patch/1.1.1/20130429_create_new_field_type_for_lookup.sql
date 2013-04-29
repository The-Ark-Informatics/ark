INSERT INTO `study`.`field_type` (`ID`, `NAME`) VALUES ('4', 'LOOKUP');

ALTER TABLE `study`.`field_type` ADD COLUMN `VISIBLE` TINYINT NOT NULL DEFAULT '1'  ;

UPDATE `study`.`field_type` SET `VISIBILE`='0' WHERE `ID`='4';

ALTER TABLE `study`.`field_type` CHANGE COLUMN `VISIBILE` `VISIBLE` TINYINT(4) NOT NULL DEFAULT '1'  ;

UPDATE `reporting`.`demographic_field` SET `FIELD_TYPE_ID`='4' WHERE `ID`='10';

