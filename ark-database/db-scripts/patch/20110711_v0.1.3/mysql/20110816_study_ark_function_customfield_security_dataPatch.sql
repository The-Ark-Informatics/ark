USE study;

-- Adjust the original SUBJECT_CUSTOM to SUBJECT_CUSTOM_FIELD (management of the CFs available for all subjects)
UPDATE `study`.`ark_function` SET `NAME`='SUBJECT_CUSTOM_FIELD', `RESOURCE_KEY`='tab.module.subject.subjectcustomfield' WHERE `ID`='11';
-- Add a new function SUBJECT_CUSTOM_DATA (data entry for subject-specific CF data)
INSERT INTO `study`.`ark_function` (`ID`, `NAME`, `DESCRIPTION`, `ARK_FUNCTION_TYPE_ID`, `RESOURCE_KEY`) VALUES (34, 'SUBJECT_CUSTOM_DATA', 'Data entry for Subject Custom Fields.', 1, 'tab.module.subject.subjectcustomdata');

-- Add the new function to the Subject module and also re-arrange the sequence of the sub-menu tabs
INSERT INTO `study`.`ark_module_function` (`ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `FUNCTION_SEQUENCE`) VALUES (31, 2, 34, 2);
UPDATE `study`.`ark_module_function` SET `FUNCTION_SEQUENCE`=9 WHERE `ID`='11';
UPDATE `study`.`ark_module_function` SET `FUNCTION_SEQUENCE`=8 WHERE `ID`='10';
UPDATE `study`.`ark_module_function` SET `FUNCTION_SEQUENCE`=6 WHERE `ID`='9';
UPDATE `study`.`ark_module_function` SET `FUNCTION_SEQUENCE`=5 WHERE `ID`='8';
UPDATE `study`.`ark_module_function` SET `FUNCTION_SEQUENCE`=4 WHERE `ID`='7';
UPDATE `study`.`ark_module_function` SET `FUNCTION_SEQUENCE`=3 WHERE `ID`='6';
UPDATE `study`.`ark_module_function` SET `FUNCTION_SEQUENCE`=7 WHERE `ID`='12';

-- Add the permission for the new SUBJECT_CUSTOM_DATA function to the ArkRolePolicyTemplate
-- *Subject Admin role* 
INSERT INTO `study`.`ark_role_policy_template`
(`ID`, `ARK_ROLE_ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `ARK_PERMISSION_ID`)
VALUES
(184, 
4, -- Subject Admin role
2, -- Subject module
34, -- SUBJECT_CUSTOM_DATA entry function
1 -- Create permission
);
INSERT INTO `study`.`ark_role_policy_template`
(`ID`, `ARK_ROLE_ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `ARK_PERMISSION_ID`)
VALUES
(185, 
4, -- Subject Admin role
2, -- Subject module
34, -- Subject Custom Data entry function
2 -- Read permission
);
INSERT INTO `study`.`ark_role_policy_template`
(`ID`, `ARK_ROLE_ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `ARK_PERMISSION_ID`)
VALUES
(186, 
4, -- Subject Admin role
2, -- Subject module
34, -- Subject Custom Data entry function
3 -- Update permission
);
INSERT INTO `study`.`ark_role_policy_template`
(`ID`, `ARK_ROLE_ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `ARK_PERMISSION_ID`)
VALUES
(187, 
4, -- Subject Admin role
2, -- Subject module
34, -- Subject Custom Data entry function
4 -- Delete permission (this won't actually be used, but is there to reflect that it is possible)
);

-- *Subject Data Manager* role
INSERT INTO `study`.`ark_role_policy_template`
(`ID`, `ARK_ROLE_ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `ARK_PERMISSION_ID`)
VALUES
(188, 
5, -- Subject Data Manager role
2, -- Subject module
34, -- SUBJECT_CUSTOM_DATA entry function
1 -- Create permission
);
INSERT INTO `study`.`ark_role_policy_template`
(`ID`, `ARK_ROLE_ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `ARK_PERMISSION_ID`)
VALUES
(189, 
5, -- Subject Data Manager role
2, -- Subject module
34, -- Subject Custom Data entry function
2 -- Read permission
);
INSERT INTO `study`.`ark_role_policy_template`
(`ID`, `ARK_ROLE_ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `ARK_PERMISSION_ID`)
VALUES
(190, 
5, -- Subject Data Manager role
2, -- Subject module
34, -- Subject Custom Data entry function
3 -- Update permission
);
INSERT INTO `study`.`ark_role_policy_template`
(`ID`, `ARK_ROLE_ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `ARK_PERMISSION_ID`)
VALUES
(191, 
5, -- Subject Data Manager role
2, -- Subject module
34, -- Subject Custom Data entry function
4 -- Delete permission (this won't actually be used, but is there to reflect that it is possible)
);

-- Remap an existing permission for *Subject Read-Only* role to the newly created function
UPDATE `study`.`ark_role_policy_template` SET `ARK_FUNCTION_ID`=34 WHERE `ID`='69';

-- Verify the updates have been correctly implemented in the database
--                            | SUBJECT_CUSTOM_FIELD fn=11 | SUBJECT_CUSTOM_DATA fn=34
-- ===========================+============================+===========================
-- *Subject Admin* role=4     |       CRU                  |     CRUD
-- *Subject Data Mgr* role=5  |        -                   |     CRUD
-- *Subject Read-Only* role=6 |        -                   |      R
SELECT * 
  FROM `study`.`ark_role_policy_template` arpt
 INNER JOIN `study`.`ark_function` af
    ON af.id = arpt.ark_function_id
 INNER JOIN `study`.`ark_role` ar
    ON ar.id = arpt.ark_role_id
 WHERE arpt.ark_function_id = 11
    OR arpt.ark_function_id = 34;