use lims;
CREATE TABLE `barcode_command` (
`ID`  int(11) NOT NULL AUTO_INCREMENT ,
`PRINTER_ID`  int(11) NOT NULL ,
`COMMAND`  varchar(45) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL ,
`DESCRIPTION`  text CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL ,
`MEMORY`  varchar(45) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL ,
PRIMARY KEY (`ID`)
)ENGINE=InnoDB DEFAULT CHARSET=latin1;
