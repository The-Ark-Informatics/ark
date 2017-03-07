CREATE TABLE `config`.`setting_file` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `FILENAME` text NOT NULL,
  `PAYLOAD` longblob,
  `CHECKSUM` varchar(50) NOT NULL,
  `FILE_ID` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;