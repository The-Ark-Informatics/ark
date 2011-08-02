USE study;
CREATE OR REPLACE VIEW `study`.`study_user_role_permission_view` AS 
select distinct `study`.`study`.`NAME` AS `studyName`,`study`.`ark_user`.`LDAP_USER_NAME` AS `userName`,`study`.`ark_role`.`NAME` AS `roleName`,`study`.`ark_module`.`NAME` AS `moduleName`,max(if((`arpt`.`ARK_PERMISSION_ID` = 1),_utf8'Y',_utf8'N')) AS `create`,max(if((`arpt`.`ARK_PERMISSION_ID` = 2),_utf8'Y',_utf8'N')) AS `read`,max(if((`arpt`.`ARK_PERMISSION_ID` = 3),_utf8'Y',_utf8'N')) AS `update`,max(if((`arpt`.`ARK_PERMISSION_ID` = 4),_utf8'Y',_utf8'N')) AS `delete` 
from ((((((`study`.`ark_role_policy_template` `arpt` join `study`.`ark_role`) join `study`.`ark_user_role`) join `study`.`ark_user`) join `study`.`ark_module`) join `study`.`ark_permission` `ap`) join `study`.`study`) 
where ((`arpt`.`ARK_ROLE_ID` = `study`.`ark_role`.`ID`) 
and (`arpt`.`ARK_MODULE_ID` = `study`.`ark_module`.`ID`) 
and (`arpt`.`ARK_PERMISSION_ID` = `ap`.`ID`) 
and (`arpt`.`ARK_ROLE_ID` = `study`.`ark_user_role`.`ARK_ROLE_ID`) 
and (`arpt`.`ARK_MODULE_ID` = `study`.`ark_user_role`.`ARK_MODULE_ID`) 
and (`study`.`ark_user_role`.`ARK_ROLE_ID` = `study`.`ark_role`.`ID`) 
and (`study`.`ark_user_role`.`ARK_MODULE_ID` = `study`.`ark_module`.`ID`) 
and (`study`.`ark_user_role`.`ARK_USER_ID` = `study`.`ark_user`.`ID`) 
and (`study`.`ark_user_role`.`STUDY_ID` = `study`.`study`.`ID`)
) 
group by `study`.`study`.`NAME`,`study`.`ark_user`.`LDAP_USER_NAME`,`study`.`ark_role`.`NAME`,`study`.`ark_module`.`NAME` 
order by `study`.`ark_user_role`.`STUDY_ID`,`study`.`ark_user`.`LDAP_USER_NAME`,`study`.`ark_role`.`ID`;

INSERT INTO `study`.`ark_function` (`ID`, `NAME`, `DESCRIPTION`, `ARK_FUNCTION_TYPE_ID`) VALUES(33, 'REPORT_STUDY_USER_ROLE_PERMISSIONS', 'Study User Role Permissions Report lists all user role and permissions for the study in context.', 2);
INSERT INTO `reporting`.`report_template` (`ID`, `NAME`, `DESCRIPTION`, TEMPLATE_PATH, MODULE_ID, FUNCTION_ID) VALUES (5, 'Study User Role Permissions Report', 'This report lists all user role and permissions for the study in context.', 'StudyUserRolePermissions.jrxml', 1, 33);


