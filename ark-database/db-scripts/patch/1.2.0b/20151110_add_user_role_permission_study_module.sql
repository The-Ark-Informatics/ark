-- This will allow following permission for study module 
-- Please check for permissin table for followings
--  '1', 'CREATE'
--  '2', 'READ',
--  '3', 'UPDATE',
--  '4', 'DELETE',

USE study;
DELIMITER $$
DROP PROCEDURE IF EXISTS `temp_proc_add_user_role_permission_study`$$
CREATE PROCEDURE `temp_proc_add_user_role_permission_study`(IN ark_function_id LONG)
BEGIN

DECLARE ark_role_id,ark_module_id LONG; 

DECLARE ark_permission_id int DEFAULT 1;

set ark_role_id=(SELECT id FROM study.ark_role where name='Subject Administrator');

set ark_module_id=(SELECT id FROM study.ark_module where name='Study');

    WHILE ark_permission_id < 4 DO
   	insert into study.ark_role_policy_template(ark_role_id,ark_module_id,ark_function_id,ark_permission_id) 
	values(ark_role_id,ark_module_id,ark_function_id,ark_permission_id);
         set ark_permission_id = ark_permission_id + 1;
    END WHILE;

END; 
$$


set @func_study=(SELECT id FROM study.ark_function where name='STUDY');
CALL temp_proc_add_user_role_permission_study(@func_study);

set @func_study_comp=(SELECT id FROM study.ark_function where name='STUDY_COMPONENT');
CALL temp_proc_add_user_role_permission_study(@func_study_comp);

set @func_study_cusf_cat=(SELECT id FROM study.ark_function where name='SUBJECT_CUSTOM_FIELD_CATEGORY');
CALL temp_proc_add_user_role_permission_study(@func_study_cusf_cat);

set @func_study_cusf=(SELECT id FROM study.ark_function where name='SUBJECT_CUSTOM_FIELD');
CALL temp_proc_add_user_role_permission_study(@func_study_cusf);

set @func_study_cusf_upload=(SELECT id FROM study.ark_function where name='SUBJECT_CUSTOM_FIELD_UPLOAD');
CALL temp_proc_add_user_role_permission_study(@func_study_cusf_upload);

DROP PROCEDURE IF EXISTS `temp_proc_add_user_role_permission_study`$$



