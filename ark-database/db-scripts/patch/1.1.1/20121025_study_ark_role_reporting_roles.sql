INSERT INTO `study`.`ark_function`
(
`NAME`,
`DESCRIPTION`,
`ARK_FUNCTION_TYPE_ID`,
`RESOURCE_KEY`) 
VALUES
('DATA_EXTRACTION', 'Advanced search for data extraction', '1', 'tab.reporting.dataextraction');

INSERT INTO `study`.`ark_module_function`
(
`ARK_MODULE_ID`,
`ARK_FUNCTION_ID`,
`FUNCTION_SEQUENCE`)
VALUES ((SELECT id FROM study.ark_module WHERE name = 'Reporting'), (SELECT id FROM study.ark_function WHERE name = 'DATA_EXTRACTION'), 1 );

INSERT INTO `study`.`ark_role`
(
`NAME`,
`DESCRIPTION`)
VALUES
(
'Reporting Administrator',
'Reporting Administrator'
),
(
'Reporting Data Manager',
'Reporting Data Manager'
),
(
'Reporting Read-Only User',
'Reporting Read-Only User'
);


INSERT INTO `study`.`ark_module_role`
(
`ARK_MODULE_ID`,
`ARK_ROLE_ID`)
VALUES
(
(select id from study.ark_module where name='Reporting'),
(select id from study.ark_role where name='Reporting Administrator')
),
(
(select id from study.ark_module where name='Reporting'),
(select id from study.ark_role where name='Reporting Data Manager')
),
(
(select id from study.ark_module where name='Reporting'),
(select id from study.ark_role where name='Reporting Read-Only User')
);


INSERT INTO `study`.`ark_role_policy_template`
(
`ARK_ROLE_ID`,
`ARK_MODULE_ID`,
`ARK_FUNCTION_ID`,
`ARK_PERMISSION_ID`)
VALUES
(
(select id from study.ark_role where name='Reporting Administrator'),
(select id from study.ark_module where name='Reporting'),
(select id from study.ark_function where name='DATA_EXTRACTION'),
(select id from study.ark_permission where name='CREATE')
),
(
(select id from study.ark_role where name='Reporting Administrator'),
(select id from study.ark_module where name='Reporting'),
(select id from study.ark_function where name='DATA_EXTRACTION'),
(select id from study.ark_permission where name='READ')
),
(
(select id from study.ark_role where name='Reporting Administrator'),
(select id from study.ark_module where name='Reporting'),
(select id from study.ark_function where name='DATA_EXTRACTION'),
(select id from study.ark_permission where name='UPDATE')
),
(
(select id from study.ark_role where name='Reporting Administrator'),
(select id from study.ark_module where name='Reporting'),
(select id from study.ark_function where name='DATA_EXTRACTION'),
(select id from study.ark_permission where name='DELETE')
),
(
(select id from study.ark_role where name='Reporting Data Manager'),
(select id from study.ark_module where name='Reporting'),
(select id from study.ark_function where name='DATA_EXTRACTION'),
(select id from study.ark_permission where name='CREATE')
),
(
(select id from study.ark_role where name='Reporting Data Manager'),
(select id from study.ark_module where name='Reporting'),
(select id from study.ark_function where name='DATA_EXTRACTION'),
(select id from study.ark_permission where name='READ')
),
(
(select id from study.ark_role where name='Reporting Data Manager'),
(select id from study.ark_module where name='Reporting'),
(select id from study.ark_function where name='DATA_EXTRACTION'),
(select id from study.ark_permission where name='UPDATE')
),
(
(select id from study.ark_role where name='Reporting Data Manager'),
(select id from study.ark_module where name='Reporting'),
(select id from study.ark_function where name='DATA_EXTRACTION'),
(select id from study.ark_permission where name='DELETE')
),
(
(select id from study.ark_role where name='Reporting Read-only User'),
(select id from study.ark_module where name='Reporting'),
(select id from study.ark_function where name='DATA_EXTRACTION'),
(select id from study.ark_permission where name='READ')
);

-- Ensure all studies linked to reporting module
INSERT INTO study.link_study_arkmodule (study_id, ark_module_id)
SELECT id, (SELECT id FROM ark_module WHERE name ='Reporting')
FROM study;

-- Ensure all users assigned default reporting role
 