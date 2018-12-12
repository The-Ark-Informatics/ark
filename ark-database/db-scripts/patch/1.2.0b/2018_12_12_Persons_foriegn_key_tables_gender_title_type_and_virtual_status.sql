ALTER TABLE `study`.`gender_type` 
CHANGE COLUMN `ID` `ID` INT(11) NOT NULL AUTO_INCREMENT ;

INSERT INTO `study`.`gender_type` (`ID`, `NAME`) VALUES ('5', 'Unknown');

UPDATE study.person set GENDER_TYPE_ID=5 where GENDER_TYPE_ID=0;

DELETE FROM `study`.`gender_type` WHERE `ID`='0';

