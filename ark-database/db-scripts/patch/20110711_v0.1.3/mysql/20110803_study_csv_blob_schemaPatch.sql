USE study;

DROP TABLE IF EXISTS `csv_blob`;
CREATE TABLE `csv_blob` (
  `ID` int(11) NOT NULL auto_increment,
  `CSV_BLOB` longblob NOT NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='Table containing BLOB references of CSV files for import/upl';

