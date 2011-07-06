use study;
ALTER TABLE `study`.`link_subject_study` DROP COLUMN `DATE_LAST_KNOWN_ALIVE` ;

ALTER TABLE `study`.`person` ADD COLUMN `DATE_LAST_KNOWN_ALIVE` DATE NULL  AFTER `OTHER_EMAIL` ;


ALTER TABLE `study`.`link_subject_study` 
  ADD CONSTRAINT `fk_link_subject_study_1`
  FOREIGN KEY (`CONSENT_DOWNLOADED` )
  REFERENCES `study`.`yes_no` (`ID` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION
, ADD INDEX `fk_link_subject_study_1` (`CONSENT_DOWNLOADED` ASC) ;



