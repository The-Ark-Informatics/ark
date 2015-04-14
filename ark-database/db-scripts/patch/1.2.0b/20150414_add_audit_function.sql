INSERT INTO `ark_function` (`NAME`, `DESCRIPTION`, `ARK_FUNCTION_TYPE_ID`, `RESOURCE_KEY`)
VALUES
	('AUDIT', NULL, 1, 'tab.module.admin.audit');

INSERT INTO `ark_module_function` (`ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `FUNCTION_SEQUENCE`)
VALUES
	((select id from `ark_module` where name="Admin"), (select id from `ark_function` where name="Audit"), 7);