INSERT INTO `study`.`ark_role`
(
`NAME`,
`DESCRIPTION`)
VALUES
(
'Work Tracking Administrator',
'Work Tracking Administrator'
),
(
'Work Tracking Data Manager',
'Work Tracking Data Manager'
),
(
'Work Tracking Read-Only User',
'Work Tracking Read-Only User'
);


INSERT INTO `study`.`ark_module_role`
(
`ARK_MODULE_ID`,
`ARK_ROLE_ID`)
VALUES
(
(select id from study.ark_module where name='Work Tracking'),
(select id from study.ark_role where name='Work Tracking Administrator')
),
(
(select id from study.ark_module where name='Work Tracking'),
(select id from study.ark_role where name='Work Tracking Data Manager')
),
(
(select id from study.ark_module where name='Work Tracking'),
(select id from study.ark_role where name='Work Tracking Read-Only User')
);


INSERT INTO `study`.`ark_role_policy_template`
(
`ARK_ROLE_ID`,
`ARK_MODULE_ID`,
`ARK_FUNCTION_ID`,
`ARK_PERMISSION_ID`)
VALUES
(
(select id from study.ark_role where name='Work Tracking Administrator'),
(select id from study.ark_module where name='Work Tracking'),
(select id from study.ark_function where name='RESEARCHER'),
(select id from study.ark_permission where name='CREATE')
),
(
(select id from study.ark_role where name='Work Tracking Administrator'),
(select id from study.ark_module where name='Work Tracking'),
(select id from study.ark_function where name='RESEARCHER'),
(select id from study.ark_permission where name='READ')
),
(
(select id from study.ark_role where name='Work Tracking Administrator'),
(select id from study.ark_module where name='Work Tracking'),
(select id from study.ark_function where name='RESEARCHER'),
(select id from study.ark_permission where name='UPDATE')
),
(
(select id from study.ark_role where name='Work Tracking Administrator'),
(select id from study.ark_module where name='Work Tracking'),
(select id from study.ark_function where name='RESEARCHER'),
(select id from study.ark_permission where name='DELETE')
),
(
(select id from study.ark_role where name='Work Tracking Administrator'),
(select id from study.ark_module where name='Work Tracking'),
(select id from study.ark_function where name='BILLABLE_ITEM_TYPE'),
(select id from study.ark_permission where name='CREATE')
),
(
(select id from study.ark_role where name='Work Tracking Administrator'),
(select id from study.ark_module where name='Work Tracking'),
(select id from study.ark_function where name='BILLABLE_ITEM_TYPE'),
(select id from study.ark_permission where name='READ')
),
(
(select id from study.ark_role where name='Work Tracking Administrator'),
(select id from study.ark_module where name='Work Tracking'),
(select id from study.ark_function where name='BILLABLE_ITEM_TYPE'),
(select id from study.ark_permission where name='UPDATE')
),
(
(select id from study.ark_role where name='Work Tracking Administrator'),
(select id from study.ark_module where name='Work Tracking'),
(select id from study.ark_function where name='BILLABLE_ITEM_TYPE'),
(select id from study.ark_permission where name='DELETE')
),
(
(select id from study.ark_role where name='Work Tracking Administrator'),
(select id from study.ark_module where name='Work Tracking'),
(select id from study.ark_function where name='WORK_REQUEST'),
(select id from study.ark_permission where name='CREATE')
),
(
(select id from study.ark_role where name='Work Tracking Administrator'),
(select id from study.ark_module where name='Work Tracking'),
(select id from study.ark_function where name='WORK_REQUEST'),
(select id from study.ark_permission where name='READ')
),
(
(select id from study.ark_role where name='Work Tracking Administrator'),
(select id from study.ark_module where name='Work Tracking'),
(select id from study.ark_function where name='WORK_REQUEST'),
(select id from study.ark_permission where name='UPDATE')
),
(
(select id from study.ark_role where name='Work Tracking Administrator'),
(select id from study.ark_module where name='Work Tracking'),
(select id from study.ark_function where name='WORK_REQUEST'),
(select id from study.ark_permission where name='DELETE')
),
(
(select id from study.ark_role where name='Work Tracking Administrator'),
(select id from study.ark_module where name='Work Tracking'),
(select id from study.ark_function where name='BILLABLE_ITEM'),
(select id from study.ark_permission where name='CREATE')
),
(
(select id from study.ark_role where name='Work Tracking Administrator'),
(select id from study.ark_module where name='Work Tracking'),
(select id from study.ark_function where name='BILLABLE_ITEM'),
(select id from study.ark_permission where name='READ')
),
(
(select id from study.ark_role where name='Work Tracking Administrator'),
(select id from study.ark_module where name='Work Tracking'),
(select id from study.ark_function where name='BILLABLE_ITEM'),
(select id from study.ark_permission where name='UPDATE')
),
(
(select id from study.ark_role where name='Work Tracking Administrator'),
(select id from study.ark_module where name='Work Tracking'),
(select id from study.ark_function where name='BILLABLE_ITEM'),
(select id from study.ark_permission where name='DELETE')
),
(
(select id from study.ark_role where name='Work Tracking Data Manager'),
(select id from study.ark_module where name='Work Tracking'),
(select id from study.ark_function where name='RESEARCHER'),
(select id from study.ark_permission where name='CREATE')
),
(
(select id from study.ark_role where name='Work Tracking Data Manager'),
(select id from study.ark_module where name='Work Tracking'),
(select id from study.ark_function where name='RESEARCHER'),
(select id from study.ark_permission where name='READ')
),
(
(select id from study.ark_role where name='Work Tracking Data Manager'),
(select id from study.ark_module where name='Work Tracking'),
(select id from study.ark_function where name='RESEARCHER'),
(select id from study.ark_permission where name='UPDATE')
),
(
(select id from study.ark_role where name='Work Tracking Data Manager'),
(select id from study.ark_module where name='Work Tracking'),
(select id from study.ark_function where name='BILLABLE_ITEM_TYPE'),
(select id from study.ark_permission where name='CREATE')
),
(
(select id from study.ark_role where name='Work Tracking Data Manager'),
(select id from study.ark_module where name='Work Tracking'),
(select id from study.ark_function where name='BILLABLE_ITEM_TYPE'),
(select id from study.ark_permission where name='READ')
),
(
(select id from study.ark_role where name='Work Tracking Data Manager'),
(select id from study.ark_module where name='Work Tracking'),
(select id from study.ark_function where name='BILLABLE_ITEM_TYPE'),
(select id from study.ark_permission where name='UPDATE')
),
(
(select id from study.ark_role where name='Work Tracking Data Manager'),
(select id from study.ark_module where name='Work Tracking'),
(select id from study.ark_function where name='WORK_REQUEST'),
(select id from study.ark_permission where name='CREATE')
),
(
(select id from study.ark_role where name='Work Tracking Data Manager'),
(select id from study.ark_module where name='Work Tracking'),
(select id from study.ark_function where name='WORK_REQUEST'),
(select id from study.ark_permission where name='READ')
),
(
(select id from study.ark_role where name='Work Tracking Data Manager'),
(select id from study.ark_module where name='Work Tracking'),
(select id from study.ark_function where name='WORK_REQUEST'),
(select id from study.ark_permission where name='UPDATE')
),
(
(select id from study.ark_role where name='Work Tracking Data Manager'),
(select id from study.ark_module where name='Work Tracking'),
(select id from study.ark_function where name='BILLABLE_ITEM'),
(select id from study.ark_permission where name='CREATE')
),
(
(select id from study.ark_role where name='Work Tracking Data Manager'),
(select id from study.ark_module where name='Work Tracking'),
(select id from study.ark_function where name='BILLABLE_ITEM'),
(select id from study.ark_permission where name='READ')
),
(
(select id from study.ark_role where name='Work Tracking Data Manager'),
(select id from study.ark_module where name='Work Tracking'),
(select id from study.ark_function where name='BILLABLE_ITEM'),
(select id from study.ark_permission where name='UPDATE')
),
(
(select id from study.ark_role where name='Work Tracking Read-Only User'),
(select id from study.ark_module where name='Work Tracking'),
(select id from study.ark_function where name='RESEARCHER'),
(select id from study.ark_permission where name='READ')
),
(
(select id from study.ark_role where name='Work Tracking Read-Only User'),
(select id from study.ark_module where name='Work Tracking'),
(select id from study.ark_function where name='BILLABLE_ITEM_TYPE'),
(select id from study.ark_permission where name='READ')
),
(
(select id from study.ark_role where name='Work Tracking Read-Only User'),
(select id from study.ark_module where name='Work Tracking'),
(select id from study.ark_function where name='WORK_REQUEST'),
(select id from study.ark_permission where name='READ')
),
(
(select id from study.ark_role where name='Work Tracking Read-Only User'),
(select id from study.ark_module where name='Work Tracking'),
(select id from study.ark_function where name='BILLABLE_ITEM'),
(select id from study.ark_permission where name='READ')
);

