USE study;

-- Insert default ARK Super Administrator. NOTE: LDAP_USER_NAME MUST match the name in LDAP
INSERT INTO ark_user (ID, LDAP_USER_NAME) VALUES (1, 'replace_with_ldapname');

-- Create default ark_user_role(s) for the new Super Administrator (id=1)
INSERT INTO `study`.`ark_user_role`
(`ID`,
`ARK_USER_ID`,
`ARK_ROLE_ID`,
`ARK_MODULE_ID`,
`STUDY_ID`)
SELECT id as `ID`, 1 as `ARK_USER_ID`, 1 `ARK_ROLE_ID`, id as `ARK_MODULE_ID`, NULL as `STUDY_ID` FROM `study`.`ark_module`;