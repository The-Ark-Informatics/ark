INSERT INTO `study`.`gender_type` (`ID`, `NAME`) VALUES ('3', 'Transgender');
INSERT INTO `study`.`gender_type` (`ID`, `NAME`) VALUES ('4', 'Intersex');

ALTER TABLE `study`.`person` 
DROP FOREIGN KEY `fk_person_gender_type`;

DELETE FROM `study`.`gender_type` WHERE `ID`='0';

ALTER TABLE `study`.`gender_type` 
CHANGE COLUMN `ID` `ID` INT(11) NOT NULL AUTO_INCREMENT ;

INSERT INTO `study`.`gender_type` (`ID`, `NAME`) VALUES ('5', 'Unknown');

UPDATE study.person set GENDER_TYPE_ID=5 where GENDER_TYPE_ID=0;

ALTER TABLE `study`.`person` 
ADD CONSTRAINT `fk_person_gender_type`
  FOREIGN KEY (`GENDER_TYPE_ID`)
  REFERENCES `study`.`gender_type` (`ID`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;


