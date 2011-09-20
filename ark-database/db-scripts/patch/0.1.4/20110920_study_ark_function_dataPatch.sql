use study;
INSERT INTO `study`.`ark_function` (`ID`, `NAME`, `DESCRIPTION`, `ARK_FUNCTION_TYPE_ID`, `RESOURCE_KEY`) VALUES (39, 'MANAGE_QUESTIONNAIRE', 'Create,Edit and Remove Questionnaires. Allows to add Custom Fields to Custom Field Group.', 1, 'tab.module.phenotypic.manage.questionnaire');

/* Associate the Ark Function 39 Manage Questionnaire with Phenotypic Module */
INSERT INTO `study`.`ark_module_function` (`ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `FUNCTION_SEQUENCE`) VALUES (36, 3, 39, 7);


