INSERT INTO `study`.`ark_module_role` (`ARK_MODULE_ID`, `ARK_ROLE_ID`) 
VALUES ((SELECT `ID` FROM `STUDY`.`ARK_MODULE` WHERE `NAME` ='GENOMICS'), (SELECT `ID` FROM `STUDY`.`ark_role` WHERE `NAME` ='Genomics Administrator'));

INSERT INTO `study`.`ark_module_role` (`ARK_MODULE_ID`, `ARK_ROLE_ID`) 
VALUES ((SELECT `ID` FROM `STUDY`.`ARK_MODULE` WHERE `NAME` ='GENOMICS'), (SELECT `ID` FROM `STUDY`.`ark_role` WHERE `NAME` ='Genomics Data Manager'));

INSERT INTO `study`.`ark_module_role` (`ARK_MODULE_ID`, `ARK_ROLE_ID`) 
VALUES ((SELECT `ID` FROM `STUDY`.`ARK_MODULE` WHERE `NAME` ='GENOMICS'), (SELECT `ID` FROM `STUDY`.`ark_role` WHERE `NAME` ='Genomics Read-Only user'));
