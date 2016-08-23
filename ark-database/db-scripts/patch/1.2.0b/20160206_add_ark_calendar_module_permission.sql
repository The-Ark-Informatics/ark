INSERT INTO `study`.`ark_module` (`NAME`, `DESCRIPTION`, `ENABLED`) VALUES ('Calendar', 'Calendar Module', '1');

INSERT INTO `study`.`ark_user_role` (`ARK_USER_ID`, `ARK_ROLE_ID`, `ARK_MODULE_ID`) 
VALUES ((SELECT `ID` FROM `STUDY`.`ARK_USER` WHERE `LDAP_USER_NAME` = 'arksuperuser@ark.org.au'), 
(SELECT `ID` FROM `STUDY`.`ARK_ROLE` WHERE `NAME` = 'Super Administrator'), 
(SELECT `ID` FROM `STUDY`.`ARK_MODULE` WHERE `NAME` = 'Calendar'));

INSERT INTO `study`.`ark_role` (`NAME`) VALUES ('Calendar Administrator');
INSERT INTO `study`.`ark_role` (`NAME`) VALUES ('Calendar Data Manager');
INSERT INTO `study`.`ark_role` (`NAME`) VALUES ('Calendar Read-Only User');

INSERT INTO `study`.`ark_module_role` (`ARK_MODULE_ID`, `ARK_ROLE_ID`) VALUES ((select id from `study`.`ark_module` where name = 'Calendar'), (select id from `study`.`ark_role` where name = 'Calendar Administrator'));
INSERT INTO `study`.`ark_module_role` (`ARK_MODULE_ID`, `ARK_ROLE_ID`) VALUES ((select id from `study`.`ark_module` where name = 'Calendar'), (select id from `study`.`ark_role` where name = 'Calendar Data Manager'));
INSERT INTO `study`.`ark_module_role` (`ARK_MODULE_ID`, `ARK_ROLE_ID`) VALUES ((select id from `study`.`ark_module` where name = 'Calendar'), (select id from `study`.`ark_role` where name = 'Calendar Read-Only User'));





