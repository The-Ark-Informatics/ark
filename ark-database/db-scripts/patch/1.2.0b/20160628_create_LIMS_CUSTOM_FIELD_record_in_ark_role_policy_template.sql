INSERT INTO `study`.`ark_role_policy_template` 
	(`ARK_ROLE_ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `ARK_PERMISSION_ID`)
VALUES
	((select id from ark_role where name = "LIMS Administrator"), (select id from ark_module where name = "LIMS"), (select id from ark_function where name = "LIMS_CUSTOM_FIELD"), 4);

