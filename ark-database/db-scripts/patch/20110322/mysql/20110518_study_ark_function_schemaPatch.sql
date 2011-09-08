USE study;
DROP TABLE IF EXISTS `ark_function`;
CREATE TABLE `ark_function` (
  `ID` int(11) NOT NULL auto_increment,
  `NAME` varchar(255) default NULL,
  `DESCRIPTION` varchar(1000) default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB;
