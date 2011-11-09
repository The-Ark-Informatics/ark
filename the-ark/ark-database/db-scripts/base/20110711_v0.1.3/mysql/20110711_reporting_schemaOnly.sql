SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `report_output_format`
-- ----------------------------
DROP TABLE IF EXISTS `report_output_format`;
CREATE TABLE `report_output_format` (
  `ID` int(11) NOT NULL auto_increment,
  `NAME` varchar(45) NOT NULL,
  `DESCRIPTION` varchar(250) default NULL,
  PRIMARY KEY  (`ID`),
  UNIQUE KEY `NAME_UNIQUE` (`NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for `report_template`
-- ----------------------------
DROP TABLE IF EXISTS `report_template`;
CREATE TABLE `report_template` (
  `ID` int(11) NOT NULL auto_increment,
  `NAME` varchar(100) NOT NULL,
  `DESCRIPTION` varchar(1024) default NULL,
  `TEMPLATE_PATH` varchar(255) NOT NULL,
  `MODULE_ID` int(11) default NULL,
  `FUNCTION_ID` int(11) default NULL,
  PRIMARY KEY  (`ID`),
  UNIQUE KEY `NAME_UNIQUE` (`NAME`),
  KEY `FK_REPORTTEMPLATE_ARKMODULE` (`MODULE_ID`),
  KEY `FK_REPORTTEMPLATE_ARKFUNCTION` (`FUNCTION_ID`),
  CONSTRAINT `FK_REPORTTEMPLATE_ARKFUNCTION` FOREIGN KEY (`FUNCTION_ID`) REFERENCES `study`.`ark_function` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_REPORTTEMPLATE_ARKMODULE` FOREIGN KEY (`MODULE_ID`) REFERENCES `study`.`ark_module` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;