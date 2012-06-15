-- Insert first Super User as a valid account (replace the value for 'LDAP_USER_NAME' if necessary)
INSERT INTO `study`.`ark_user` (`ID`, `LDAP_USER_NAME`) VALUES (1, 'arksupersuser@ark.org.au');

-- Set up the permissions for the first Super User (ark_role_id = 1)
INSERT INTO `study`.`ark_user_role` (ID,ARK_USER_ID,ARK_ROLE_ID,ARK_MODULE_ID,STUDY_ID) VALUES (1,1,1,1,NULL);
INSERT INTO `study`.`ark_user_role` (ID,ARK_USER_ID,ARK_ROLE_ID,ARK_MODULE_ID,STUDY_ID) VALUES (2,1,1,2,NULL);
INSERT INTO `study`.`ark_user_role` (ID,ARK_USER_ID,ARK_ROLE_ID,ARK_MODULE_ID,STUDY_ID) VALUES (3,1,1,3,NULL);
INSERT INTO `study`.`ark_user_role` (ID,ARK_USER_ID,ARK_ROLE_ID,ARK_MODULE_ID,STUDY_ID) VALUES (4,1,1,4,NULL);
INSERT INTO `study`.`ark_user_role` (ID,ARK_USER_ID,ARK_ROLE_ID,ARK_MODULE_ID,STUDY_ID) VALUES (5,1,1,5,NULL);
-- NB: ark_module_id = 6 (Reporting) omitted, because reporting relies on permissions defined in other modules.
INSERT INTO `study`.`ark_user_role` (ID,ARK_USER_ID,ARK_ROLE_ID,ARK_MODULE_ID,STUDY_ID) VALUES (7,1,1,7,NULL);
