USE study;

-- Insert default ARK Super Administrator. NOTE: LDAP_USER_NAME MUST match the name in LDAP
INSERT INTO ark_user (ID, LDAP_USER_NAME) VALUES (1, 'replace_with_ldapname');

-- Create default ark_user_role(s) for the new Super Administrator
INSERT INTO ark_user_role
SELECT id as `ID`, 1 as `ARK_USER_ID`, 1 `ARK_ROLE_ID`, id as `ARK_MODULE_ID` FROM `study`.`ark_module`;