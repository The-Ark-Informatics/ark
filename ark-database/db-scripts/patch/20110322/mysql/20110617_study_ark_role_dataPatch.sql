use study;
UPDATE `study`.`ark_role` SET `NAME`='Study Administrator' WHERE `ID`='2';
UPDATE `study`.`ark_role` SET `NAME`='Study Read-Only user' WHERE `ID`='3';
UPDATE `study`.`ark_role` SET `NAME`='Subject Administrator' WHERE `ID`='4';
UPDATE `study`.`ark_role` SET `NAME`='Subject Data Manager' WHERE `ID`='5';
INSERT INTO `study`.`ark_role` (`ID`, `NAME`) VALUES (6, 'Subject Read-Only user');
