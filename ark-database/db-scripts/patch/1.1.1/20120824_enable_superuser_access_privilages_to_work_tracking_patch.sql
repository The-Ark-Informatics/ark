INSERT INTO `study`.`ark_user_role` 
(`ARK_USER_ID`, `ARK_ROLE_ID`, `ARK_MODULE_ID`) 
VALUES ((select id from study.ark_user where ldap_user_name='arksuperuser@ark.org.au'), 
	(select id from study.ark_role where name= 'Super Administrator'), 
	(select id from study.ark_module where name='Work Tracking')
);
