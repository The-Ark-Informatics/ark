use study;

INSERT INTO `ark_function` (`NAME`, `DESCRIPTION`, `ARK_FUNCTION_TYPE_ID`, `RESOURCE_KEY`)
VALUES
	('SETTINGS', 'New study level system settings', 1, 'tab.module.admin.settings');

set @module=(select id from `ark_module` where name="Admin");
set @seq=(select function_sequence from `ark_module_function` where ark_module_id=@module and ark_function_id=(select id from ark_function where name="Audit"));

INSERT INTO `ark_module_function` ( `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `FUNCTION_SEQUENCE`)
VALUES
	(@module, (select id from `ark_function` where name="SETTINGS"), @seq+1);

