ALTER TABLE `study`.`correspondences` ADD COLUMN `PERSON_ID` INT(11) NOT NULL  AFTER `ID` ,
  ADD CONSTRAINT `correspondences_person_id`
  FOREIGN KEY (`PERSON_ID` )
  REFERENCES `study`.`person` (`ID` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION
, ADD INDEX `correspondences_person_id` (`PERSON_ID` ASC) ;

