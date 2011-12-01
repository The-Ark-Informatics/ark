USE study;
INSERT INTO `study`.`ark_user_role`
(`ARK_USER_ID`,
`ARK_ROLE_ID`,
`ARK_MODULE_ID`,
`STUDY_ID`)
SELECT ark_user_id, ark_role_id, 6 as ark_module_id, null as study_id FROM ark_user_role WHERE ark_role_id in (2,3);
