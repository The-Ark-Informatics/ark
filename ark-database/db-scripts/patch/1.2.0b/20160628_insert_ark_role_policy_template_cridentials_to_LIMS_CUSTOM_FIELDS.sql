INSERT INTO `study`.`ark_role_policy_template` 
	(`ARK_ROLE_ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `ARK_PERMISSION_ID`)
VALUES
	((select id from ark_role where name = "LIMS Data Manager"), (select id from ark_module where name = "LIMS"), (select id from ark_function where name = "LIMS_CUSTOM_FIELD"), 1),
	((select id from ark_role where name = "LIMS Data Manager"), (select id from ark_module where name = "LIMS"), (select id from ark_function where name = "LIMS_CUSTOM_FIELD"), 2),
	((select id from ark_role where name = "LIMS Data Manager"), (select id from ark_module where name = "LIMS"), (select id from ark_function where name = "LIMS_CUSTOM_FIELD"), 3),
	((select id from ark_role where name = "LIMS Data Manager"), (select id from ark_module where name = "LIMS"), (select id from ark_function where name = "LIMS_CUSTOM_FIELD_CATEGORY"), 1),
	((select id from ark_role where name = "LIMS Data Manager"), (select id from ark_module where name = "LIMS"), (select id from ark_function where name = "LIMS_CUSTOM_FIELD_CATEGORY"), 2),
	((select id from ark_role where name = "LIMS Data Manager"), (select id from ark_module where name = "LIMS"), (select id from ark_function where name = "LIMS_CUSTOM_FIELD_CATEGORY"), 3),
	((select id from ark_role where name = "LIMS Data Manager"), (select id from ark_module where name = "LIMS"), (select id from ark_function where name = "LIMS_CUSTOM_FIELD_UPLOAD"), 1),
	((select id from ark_role where name = "LIMS Data Manager"), (select id from ark_module where name = "LIMS"), (select id from ark_function where name = "LIMS_CUSTOM_FIELD_UPLOAD"), 2),
	((select id from ark_role where name = "LIMS Data Manager"), (select id from ark_module where name = "LIMS"), (select id from ark_function where name = "LIMS_CUSTOM_FIELD_UPLOAD"), 3);;
