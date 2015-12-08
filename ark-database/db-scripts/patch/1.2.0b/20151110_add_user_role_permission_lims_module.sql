-- This will allow following permission for lims module 
-- Please check for permissin table for followings
--  '1', 'CREATE'
--  '2', 'READ',
--  '3', 'UPDATE',
--  '4', 'DELETE',

USE study;
DELIMITER $$
DROP PROCEDURE IF EXISTS `temp_proc_add_user_role_permission_lims`$$
CREATE PROCEDURE `temp_proc_add_user_role_permission_lims`(IN ark_function_id LONG)
BEGIN

DECLARE ark_permission_id int DEFAULT 1;

DECLARE ark_role_id,ark_module_id LONG; 


set ark_role_id=(SELECT id FROM study.ark_role where name='LIMS Administrator');

set ark_module_id=(SELECT id FROM study.ark_module where name='LIMS');

    WHILE ark_permission_id < 4 DO
   	insert into study.ark_role_policy_template(ark_role_id,ark_module_id,ark_function_id,ark_permission_id) 
	values(ark_role_id,ark_module_id,ark_function_id,ark_permission_id);
         set ark_permission_id = ark_permission_id + 1;
    END WHILE;

END; 
$$


set @func_lims_cat=(SELECT id FROM study.ark_function where name='LIMS_CUSTOM_FIELD_CATEGORY');
CALL temp_proc_add_user_role_permission_lims(@func_lims_cat);

set @func_lims_cf=(SELECT id FROM study.ark_function where name='LIMS_CUSTOM_FIELD');
CALL temp_proc_add_user_role_permission_lims(@func_lims_cf);

set @func_lims_cf_upload=(SELECT id FROM study.ark_function where name='LIMS_CUSTOM_FIELD_UPLOAD');
CALL temp_proc_add_user_role_permission_lims(@func_lims_cf_upload);

DROP PROCEDURE IF EXISTS `temp_proc_add_user_role_permission_lims`$$





