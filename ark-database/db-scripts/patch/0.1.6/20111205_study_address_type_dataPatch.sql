USE study;
UPDATE `study`.`address_type` SET `NAME`='RESIDENTIAL' WHERE `ID`='1';
INSERT INTO `study`.`address_type` (`ID`, `NAME`) VALUES (3, 'POSTAL');