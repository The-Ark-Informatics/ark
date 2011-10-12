use study;

INSERT INTO `study`.`ark_role_policy_template` (`ID`, `ARK_ROLE_ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `ARK_PERMISSION_ID`) VALUES (5, 2, 1, 1, 2);
INSERT INTO `study`.`ark_role_policy_template` (`ID`, `ARK_ROLE_ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `ARK_PERMISSION_ID`) VALUES (6, 2, 1, 1, 3);

INSERT INTO `study`.`ark_role_policy_template` (`ID`, `ARK_ROLE_ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `ARK_PERMISSION_ID`) VALUES (51, 4, 2, 11, 1);
INSERT INTO `study`.`ark_role_policy_template` (`ID`, `ARK_ROLE_ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `ARK_PERMISSION_ID`) VALUES (52, 4, 2, 11, 2);
INSERT INTO `study`.`ark_role_policy_template` (`ID`, `ARK_ROLE_ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `ARK_PERMISSION_ID`) VALUES (53, 4, 2, 11, 3);

/* Subject Data manager access to Subject Module/Management of Subjects */
INSERT INTO `study`.`ark_role_policy_template` (`ID`, `ARK_ROLE_ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `ARK_PERMISSION_ID`) VALUES (54, 5, 2, 5, 1);
INSERT INTO `study`.`ark_role_policy_template` (`ID`, `ARK_ROLE_ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `ARK_PERMISSION_ID`) VALUES (55, 5, 2, 5, 3);
/* Access to CRUD for Subject Data Manger for Phone */
INSERT INTO `study`.`ark_role_policy_template` (`ID`, `ARK_ROLE_ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `ARK_PERMISSION_ID`) VALUES (56, 5, 2, 6, 1);
INSERT INTO `study`.`ark_role_policy_template` (`ID`, `ARK_ROLE_ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `ARK_PERMISSION_ID`) VALUES (57, 5, 2, 6, 3);
INSERT INTO `study`.`ark_role_policy_template` (`ID`, `ARK_ROLE_ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `ARK_PERMISSION_ID`) VALUES (58, 5, 2, 6, 4);

UPDATE `study`.`ark_role_policy_template` SET `ARK_PERMISSION_ID`=4 WHERE `ID`='57';
DELETE FROM `study`.`ark_role_policy_template` WHERE `ID`='58';

INSERT INTO `study`.`ark_role_policy_template` (`ID`, `ARK_ROLE_ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `ARK_PERMISSION_ID`) VALUES (58, 5, 2, 7, 1);
INSERT INTO `study`.`ark_role_policy_template` (`ID`, `ARK_ROLE_ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `ARK_PERMISSION_ID`) VALUES (59, 5, 2, 7, 4);
/* Subject Data Manager CD for Attachments */
INSERT INTO `study`.`ark_role_policy_template` (`ID`, `ARK_ROLE_ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `ARK_PERMISSION_ID`) VALUES (60, 5, 2, 8, 1);
INSERT INTO `study`.`ark_role_policy_template` (`ID`, `ARK_ROLE_ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `ARK_PERMISSION_ID`) VALUES (61, 5, 2, 8, 4);

/* Subject Data Manager for CD for Consents */
INSERT INTO `study`.`ark_role_policy_template` (`ID`, `ARK_ROLE_ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `ARK_PERMISSION_ID`) VALUES (62, 5, 2, 9, 1);
INSERT INTO `study`.`ark_role_policy_template` (`ID`, `ARK_ROLE_ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `ARK_PERMISSION_ID`) VALUES (63, 5, 2, 9, 4);

/* Subject Read Only User */

INSERT INTO `study`.`ark_role_policy_template` (`ID`, `ARK_ROLE_ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `ARK_PERMISSION_ID`) VALUES (64, 6, 2, 5, 2);
INSERT INTO `study`.`ark_role_policy_template` (`ID`, `ARK_ROLE_ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `ARK_PERMISSION_ID`) VALUES (65, 6, 2, 6, 2);
INSERT INTO `study`.`ark_role_policy_template` (`ID`, `ARK_ROLE_ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `ARK_PERMISSION_ID`) VALUES (66, 6, 2, 7, 2);
INSERT INTO `study`.`ark_role_policy_template` (`ID`, `ARK_ROLE_ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `ARK_PERMISSION_ID`) VALUES (67, 6, 2, 8, 2);
INSERT INTO `study`.`ark_role_policy_template` (`ID`, `ARK_ROLE_ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `ARK_PERMISSION_ID`) VALUES (68, 6, 2, 9, 2);
INSERT INTO `study`.`ark_role_policy_template` (`ID`, `ARK_ROLE_ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `ARK_PERMISSION_ID`) VALUES (69, 6, 2, 11, 2);

/* Study Read-Only user cannot update Study component */
DELETE FROM `study`.`ark_role_policy_template` WHERE `ID`='17';

/* Script to Look at the Ark Role Policy Template */
SELECT
       ar.name AS Role, 
       am.name AS Module, 
       af.name AS FunctionGroup, 
	   ap.name AS Permission,
       af.description AS Function
  FROM `study`.`ark_role_policy_template` AS arpt
 INNER JOIN
       `study`.`ark_role` AS ar
    ON arpt.ark_role_id = ar.id
 INNER JOIN
       `study`.`ark_permission` AS ap
    ON arpt.ark_permission_id = ap.id
 LEFT JOIN
       `study`.`ark_module` AS am
    ON arpt.ark_module_id = am.id
 LEFT JOIN
       `study`.`ark_function` AS af
    ON arpt.ark_function_id = af.id
 ORDER BY ar.id, af.id,ap.id;
