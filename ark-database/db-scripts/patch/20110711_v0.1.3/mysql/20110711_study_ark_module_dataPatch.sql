USE study;

-- Add Admin module
INSERT INTO `study`.`ark_module` (`ID`, `NAME`) VALUES (7, 'Admin');

-- Add Admin rolePolicyTemplate function
INSERT INTO `study`.`ark_function` (`ID`, `NAME`, `DESCRIPTION`, `ARK_FUNCTION_TYPE_ID`, `RESOURCE_KEY`) VALUES (28, 'ROLE_POLICY_TEMPLATE', 'Allows CRUD operations on the global role policy templates for the Ark application', 1, 'tab.module.admin.rolePolicyTemplate');

-- Add Admin module/function
INSERT INTO `study`.`ark_module_function` (`ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `FUNCTION_SEQUENCE`) VALUES (28, 7, 28, 1);

-- Add user/role
INSERT INTO `study`.`ark_user_role` (`ARK_USER_ID`, `ARK_ROLE_ID`, `ARK_MODULE_ID`, `STUDY_ID`) VALUES (1, 1, 7, NULL);

-- Module and Function sub-menus as functions
INSERT INTO `study`.`ark_function` (`ID`, `NAME`, `DESCRIPTION`, `ARK_FUNCTION_TYPE_ID`, `RESOURCE_KEY`) VALUES (29, 'MODULE', 'Allows CRUD operations on the ark_module table for the Ark application', 1, 'tab.module.admin.module');
INSERT INTO `study`.`ark_function` (`ID`, `NAME`, `DESCRIPTION`, `ARK_FUNCTION_TYPE_ID`, `RESOURCE_KEY`) VALUES (30, 'FUNCTION', 'Allows CRUD operations on the ark_function table for the Ark application', 1, 'tab.module.admin.function');
UPDATE `study`.`ark_function` SET `DESCRIPTION`='Allows CRUD operations on the ark_role_policy_template table for the Ark application' WHERE `ID`='28';

-- Add module and function to, and reorder
INSERT INTO `study`.`ark_module_function` (`ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `FUNCTION_SEQUENCE`) VALUES (29, 7, 29, 1);
INSERT INTO `study`.`ark_module_function` (`ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `FUNCTION_SEQUENCE`) VALUES (30, 7, 30, 2);
UPDATE `study`.`ark_module_function` SET `FUNCTION_SEQUENCE`=3 WHERE `ID`='28';

