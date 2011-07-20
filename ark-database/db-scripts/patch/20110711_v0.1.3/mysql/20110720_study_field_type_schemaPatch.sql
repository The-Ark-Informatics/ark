use study;
CREATE TABLE `field_type` (
  `ID` int(11) NOT NULL auto_increment,
  `NAME` varchar(50) NOT NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB;


INSERT INTO `study`.`field_type` (`ID`, `NAME`) VALUES (1, 'CHARACTER');
INSERT INTO `study`.`field_type` (`ID`, `NAME`) VALUES (2, 'NUMBER');
INSERT INTO `study`.`field_type` (`ID`, `NAME`) VALUES (3, 'DATE');

