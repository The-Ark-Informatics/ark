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
