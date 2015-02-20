INSERT INTO `study`.`ark_module` (`NAME`, `DESCRIPTION`) VALUES ('Genomics', 'Genomics Module');

INSERT INTO `study`.`ark_user_role` (`ARK_USER_ID`, `ARK_ROLE_ID`, `ARK_MODULE_ID`) 
VALUES ((SELECT `ID` FROM `STUDY`.`ARK_USER` WHERE `LDAP_USER_NAME` = 'arksuperuser@ark.org.au'), (SELECT `ID` FROM `STUDY`.`ARK_ROLE` WHERE `NAME` = 'Super Administrator'), (SELECT `ID` FROM `STUDY`.`ARK_MODULE` WHERE `NAME` = 'Genomics'));
