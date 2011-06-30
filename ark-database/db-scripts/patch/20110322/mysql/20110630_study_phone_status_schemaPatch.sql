use study;
CREATE  TABLE `study`.`phone_status` (
  `ID` INT NOT NULL AUTO_INCREMENT ,
  `NAME` VARCHAR(255) NULL ,
  `DESCRIPTION` VARCHAR(500) NULL ,
  PRIMARY KEY (`ID`) )
ENGINE = InnoDB;

INSERT INTO `study`.`phone_status` (`ID`, `NAME`) VALUES (1, 'Current');
INSERT INTO `study`.`phone_status` (`ID`, `NAME`) VALUES (2, 'Current Alternative');
INSERT INTO `study`.`phone_status` (`ID`, `NAME`) VALUES (3, 'Current Under Investigation');
INSERT INTO `study`.`phone_status` (`ID`, `NAME`) VALUES (4, 'Valid Past');
INSERT INTO `study`.`phone_status` (`ID`, `NAME`) VALUES (5, 'Incorrect or Disconnected');


