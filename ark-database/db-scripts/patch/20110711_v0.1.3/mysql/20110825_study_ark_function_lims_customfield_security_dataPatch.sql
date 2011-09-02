USE study;

-- Add new functions for LIMS collection Custom Field management and data entry
INSERT INTO `study`.`ark_function` (`ID`, `NAME`, `DESCRIPTION`, `ARK_FUNCTION_TYPE_ID`, `RESOURCE_KEY`) VALUES (35, 'LIMS_COLLECTION_CUSTOM_FIELD', 'Manage Custom Fields for LIMS collections.', 1, 'tab.module.lims.collectioncustomfield');
INSERT INTO `study`.`ark_function` (`ID`, `NAME`, `DESCRIPTION`, `ARK_FUNCTION_TYPE_ID`, `RESOURCE_KEY`) VALUES (36, 'LIMS_COLLECTION_CUSTOM_DATA', 'Data entry for LIMS collection Custom Fields.', 1, 'tab.module.lims.collectioncustomdata');

-- Add new functions for LIMS Biospecimen Custom Field management and data entry
INSERT INTO `study`.`ark_function` (`ID`, `NAME`, `DESCRIPTION`, `ARK_FUNCTION_TYPE_ID`, `RESOURCE_KEY`) VALUES (37, 'BIOSPECIMEN_CUSTOM_FIELD', 'Manage Custom Fields for Biospecimens.', 1, 'tab.module.lims.biospecimencustomfield');
INSERT INTO `study`.`ark_function` (`ID`, `NAME`, `DESCRIPTION`, `ARK_FUNCTION_TYPE_ID`, `RESOURCE_KEY`) VALUES (38, 'BIOSPECIMEN_CUSTOM_DATA', 'Data entry for Biospecimen Custom Fields.', 1, 'tab.module.lims.biospecimencustomdata');

-- Re-order sequence of tabs on LIMS
-- which leaves a gap for LIMS Collection CF data at seq=3 and LIMS Biospecimen CF data at seq=5, so we can insert all new functions as required...
UPDATE `study`.`ark_module_function` SET `FUNCTION_SEQUENCE`=6 WHERE `ID`='21';  -- LIMS Inventory sub-tab to make way for LIMS Biospecimen move
UPDATE `study`.`ark_module_function` SET `FUNCTION_SEQUENCE`=4 WHERE `ID`='20';  -- LIMS Biospecimen sub-tab to make way for new tabs
-- Add the new functions to the LIMS module (arkModule=5) and
INSERT INTO `study`.`ark_module_function` (`ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `FUNCTION_SEQUENCE`) VALUES (32, 5, 36, 3);  -- LIMS Collection CF data 
INSERT INTO `study`.`ark_module_function` (`ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `FUNCTION_SEQUENCE`) VALUES (33, 5, 38, 5);  -- Biospeciment CF data
INSERT INTO `study`.`ark_module_function` (`ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `FUNCTION_SEQUENCE`) VALUES (34, 5, 35, 7);  -- LIMS Collection CF mgr
INSERT INTO `study`.`ark_module_function` (`ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `FUNCTION_SEQUENCE`) VALUES (35, 5, 37, 8);  -- Biospecimen CF mgr

-- Verify the changes to sub-tabs (i.e. new functions are in the LIMS module and modified sequence)
SELECT *
  FROM `study`.`ark_module_function` amf
 INNER JOIN `study`.`ark_function` af
    ON amf.ark_function_id = af.id
 WHERE ark_module_id = 5
 ORDER BY amf.function_sequence;


-- Add the permission for the new LIMS_COLLECTION_CUSTOM_FIELD and LIMS_COLLECTION_CUSTOM_DATA functions to the ArkRolePolicyTemplate
-- *LIMS Admin role* 
INSERT INTO `study`.`ark_role_policy_template`
(`ID`, `ARK_ROLE_ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `ARK_PERMISSION_ID`)
VALUES
(192, 
12, -- LIMS Admin role
5, -- LIMS module
35, -- LIMS_COLLECTION_CUSTOM_FIELD management function
1 -- Create permission
);

INSERT INTO `study`.`ark_role_policy_template`
(`ID`, `ARK_ROLE_ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `ARK_PERMISSION_ID`)
VALUES
(193, 
12, -- LIMS Admin role
5, -- LIMS module
35, -- LIMS_COLLECTION_CUSTOM_FIELD management function
2 -- Read permission
);

INSERT INTO `study`.`ark_role_policy_template`
(`ID`, `ARK_ROLE_ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `ARK_PERMISSION_ID`)
VALUES
(194, 
12, -- LIMS Admin role
5, -- LIMS module
35, -- LIMS_COLLECTION_CUSTOM_FIELD management function
3 -- Update permission
);

INSERT INTO `study`.`ark_role_policy_template`
(`ID`, `ARK_ROLE_ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `ARK_PERMISSION_ID`)
VALUES
(195, 
12, -- LIMS Admin role
5, -- LIMS module
36, -- LIMS_COLLECTION_CUSTOM_DATA entry function
1 -- Create permission
);

INSERT INTO `study`.`ark_role_policy_template`
(`ID`, `ARK_ROLE_ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `ARK_PERMISSION_ID`)
VALUES
(196, 
12, -- LIMS Admin role
5, -- LIMS module
36, -- LIMS_COLLECTION_CUSTOM_DATA entry function
2 -- Read permission
);

INSERT INTO `study`.`ark_role_policy_template`
(`ID`, `ARK_ROLE_ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `ARK_PERMISSION_ID`)
VALUES
(197, 
12, -- LIMS Admin role
5, -- LIMS module
36, -- LIMS_COLLECTION_CUSTOM_DATA entry function
3 -- Update permission
);

INSERT INTO `study`.`ark_role_policy_template`
(`ID`, `ARK_ROLE_ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `ARK_PERMISSION_ID`)
VALUES
(198, 
12, -- LIMS Admin role
5, -- LIMS module
36, -- LIMS_COLLECTION_CUSTOM_DATA entry function
4 -- Delete permission (this won't actually be used, but is there to reflect that it is allowed via update)
);

-- *LIMS Manager role*
INSERT INTO `study`.`ark_role_policy_template`
(`ID`, `ARK_ROLE_ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `ARK_PERMISSION_ID`)
VALUES
(199, 
10, -- LIMS Mgr role
5, -- LIMS module
36, -- LIMS_COLLECTION_CUSTOM_DATA entry function
1 -- Create permission
);

INSERT INTO `study`.`ark_role_policy_template`
(`ID`, `ARK_ROLE_ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `ARK_PERMISSION_ID`)
VALUES
(200, 
10, -- LIMS Mgr role
5, -- LIMS module
36, -- LIMS_COLLECTION_CUSTOM_DATA entry function
2 -- Read permission
);

INSERT INTO `study`.`ark_role_policy_template`
(`ID`, `ARK_ROLE_ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `ARK_PERMISSION_ID`)
VALUES
(201, 
10, -- LIMS Mgr role
5, -- LIMS module
36, -- LIMS_COLLECTION_CUSTOM_DATA entry function
3 -- Update permission
);

INSERT INTO `study`.`ark_role_policy_template`
(`ID`, `ARK_ROLE_ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `ARK_PERMISSION_ID`)
VALUES
(202, 
10, -- LIMS Mgr role
5, -- LIMS module
36, -- LIMS_COLLECTION_CUSTOM_DATA entry function
4 -- Delete permission (this won't actually be used, but is there to reflect that it is allowed via update)
);

-- *LIMS Read-only role*
INSERT INTO `study`.`ark_role_policy_template`
(`ID`, `ARK_ROLE_ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `ARK_PERMISSION_ID`)
VALUES
(203, 
9, -- LIMS Mgr role
5, -- LIMS module
36, -- LIMS_COLLECTION_CUSTOM_DATA entry function
2 -- Read permission
);

-- Verify the updates have been correctly implemented in the database
--                            | LIMS_COLLECTION_CUSTOM_FIELD fn=35 | LIMS_COLLECTION_DATA fn=36
-- ===========================+====================================+===========================
-- *LIMS Admin* role=12       |       CRU                          |     CRUD
-- *LIMS Data Mgr* role=10    |        -                           |     CRUD
-- *LIMS Read-Only* role=9    |        -                           |      R
SELECT * 
  FROM `study`.`ark_role_policy_template` arpt
 INNER JOIN `study`.`ark_function` af
    ON af.id = arpt.ark_function_id
 INNER JOIN `study`.`ark_role` ar
    ON ar.id = arpt.ark_role_id
 WHERE arpt.ark_function_id = 35
    OR arpt.ark_function_id = 36;
    
    
-- Add the permission for the new BIOSPECIMEN_CUSTOM_FIELD and BIOSPECIMEN_CUSTOM_DATA functions to the ArkRolePolicyTemplate
-- *LIMS Admin role* 
INSERT INTO `study`.`ark_role_policy_template`
(`ID`, `ARK_ROLE_ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `ARK_PERMISSION_ID`)
VALUES
(204, 
12, -- LIMS Admin role
5, -- LIMS module
37, -- BIOSPECIMEN_CUSTOM_FIELD management function
1 -- Create permission
);

INSERT INTO `study`.`ark_role_policy_template`
(`ID`, `ARK_ROLE_ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `ARK_PERMISSION_ID`)
VALUES
(205, 
12, -- LIMS Admin role
5, -- LIMS module
37, -- BIOSPECIMEN_CUSTOM_FIELD management function
2 -- Read permission
);

INSERT INTO `study`.`ark_role_policy_template`
(`ID`, `ARK_ROLE_ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `ARK_PERMISSION_ID`)
VALUES
(206, 
12, -- LIMS Admin role
5, -- LIMS module
37, -- BIOSPECIMEN_CUSTOM_FIELD management function
3 -- Update permission
);

INSERT INTO `study`.`ark_role_policy_template`
(`ID`, `ARK_ROLE_ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `ARK_PERMISSION_ID`)
VALUES
(207, 
12, -- LIMS Admin role
5, -- LIMS module
38, -- BIOSPECIMEN_CUSTOM_DATA entry function
1 -- Create permission
);

INSERT INTO `study`.`ark_role_policy_template`
(`ID`, `ARK_ROLE_ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `ARK_PERMISSION_ID`)
VALUES
(208, 
12, -- LIMS Admin role
5, -- LIMS module
38, -- BIOSPECIMEN_CUSTOM_DATA entry function
2 -- Read permission
);

INSERT INTO `study`.`ark_role_policy_template`
(`ID`, `ARK_ROLE_ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `ARK_PERMISSION_ID`)
VALUES
(209, 
12, -- LIMS Admin role
5, -- LIMS module
38, -- BIOSPECIMEN_CUSTOM_DATA entry function
3 -- Update permission
);

INSERT INTO `study`.`ark_role_policy_template`
(`ID`, `ARK_ROLE_ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `ARK_PERMISSION_ID`)
VALUES
(210, 
12, -- LIMS Admin role
5, -- LIMS module
38, -- BIOSPECIMEN_CUSTOM_DATA entry function
4 -- Delete permission (this won't actually be used, but is there to reflect that it is allowed via update)
);

-- *LIMS Manager role*
INSERT INTO `study`.`ark_role_policy_template`
(`ID`, `ARK_ROLE_ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `ARK_PERMISSION_ID`)
VALUES
(211, 
10, -- LIMS Mgr role
5, -- LIMS module
38, -- BIOSPECIMEN_CUSTOM_DATA entry function
1 -- Create permission
);

INSERT INTO `study`.`ark_role_policy_template`
(`ID`, `ARK_ROLE_ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `ARK_PERMISSION_ID`)
VALUES
(212, 
10, -- LIMS Mgr role
5, -- LIMS module
38, -- BIOSPECIMEN_CUSTOM_DATA entry function
2 -- Read permission
);

INSERT INTO `study`.`ark_role_policy_template`
(`ID`, `ARK_ROLE_ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `ARK_PERMISSION_ID`)
VALUES
(213, 
10, -- LIMS Mgr role
5, -- LIMS module
38, -- BIOSPECIMEN_CUSTOM_DATA entry function
3 -- Update permission
);

INSERT INTO `study`.`ark_role_policy_template`
(`ID`, `ARK_ROLE_ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `ARK_PERMISSION_ID`)
VALUES
(214, 
10, -- LIMS Mgr role
5, -- LIMS module
38, -- BIOSPECIMEN_CUSTOM_DATA entry function
4 -- Delete permission (this won't actually be used, but is there to reflect that it is allowed via update)
);

-- *LIMS Read-only role*
INSERT INTO `study`.`ark_role_policy_template`
(`ID`, `ARK_ROLE_ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `ARK_PERMISSION_ID`)
VALUES
(215, 
9, -- LIMS Mgr role
5, -- LIMS module
38, -- BIOSPECIMEN_CUSTOM_DATA entry function
2 -- Read permission
);


-- Verify the updates have been correctly implemented in the database
--                            | BIOSPECIMEN_CUSTOM_FIELD fn=37 | BIOSPECIMEN_DATA fn=38
-- ===========================+================================+===========================
-- *LIMS Admin* role=12       |       CRU                      |     CRUD
-- *LIMS Data Mgr* role=10    |        -                       |     CRUD
-- *LIMS Read-Only* role=9    |        -                       |      R
SELECT * 
  FROM `study`.`ark_role_policy_template` arpt
 INNER JOIN `study`.`ark_function` af
    ON af.id = arpt.ark_function_id
 INNER JOIN `study`.`ark_role` ar
    ON ar.id = arpt.ark_role_id
 WHERE arpt.ark_function_id = 37
    OR arpt.ark_function_id = 38;
    
