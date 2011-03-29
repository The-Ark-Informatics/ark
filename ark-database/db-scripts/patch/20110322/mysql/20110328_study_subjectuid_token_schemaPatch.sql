CREATE TABLE `subjectuid_token` (
  `ID` int(11) NOT NULL auto_increment,
  `NAME` varchar(25) NOT NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=latin1;

INSERT INTO `study`.`subjectuid_token` (`ID`, `NAME`) VALUES ( 1, '-' );
INSERT INTO `study`.`subjectuid_token` (`ID`, `NAME`) VALUES ( 2, '@' );
INSERT INTO `study`.`subjectuid_token` (`ID`, `NAME`) VALUES ( 3, '#' );
INSERT INTO `study`.`subjectuid_token` (`ID`, `NAME`) VALUES ( 4, ':' );
INSERT INTO `study`.`subjectuid_token` (`ID`, `NAME`) VALUES ( 5, '*' );
INSERT INTO `study`.`subjectuid_token` (`ID`, `NAME`) VALUES ( 6, '|' );
INSERT INTO `study`.`subjectuid_token` (`ID`, `NAME`) VALUES ( 7, '_' );
INSERT INTO `study`.`subjectuid_token` (`ID`, `NAME`) VALUES ( 8, '+' );

UPDATE `study`.`study` SET `SUBJECTUID_TOKEN` = 1 WHERE `SUBJECTUID_TOKEN` = '-';

ALTER TABLE `study`.`study` CHANGE COLUMN `SUBJECTUID_TOKEN` `SUBJECTUID_TOKEN_ID` INT(11) NULL DEFAULT NULL  , 
  ADD CONSTRAINT `fk_study_subjectuid_token`
  FOREIGN KEY (`SUBJECTUID_TOKEN_ID` )
  REFERENCES `study`.`subjectuid_token` (`ID` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;
