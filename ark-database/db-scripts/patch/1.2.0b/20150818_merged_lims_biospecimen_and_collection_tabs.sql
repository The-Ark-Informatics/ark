use study;
set @module_id=(SELECT ID FROM ark_module where ark_module.name='LIMS');

-- Delete all module fuction combination ----------
DELETE FROM ark_module_function WHERE ARK_MODULE_ID=@module_id and ARK_FUNCTION_ID=(SELECT ID FROM ark_function WHERE name='LIMS_COLLECTION_CUSTOM_FIELD');
DELETE FROM ark_module_function WHERE ARK_MODULE_ID=@module_id and ARK_FUNCTION_ID=(SELECT ID FROM ark_function WHERE name='BIOSPECIMEN_CUSTOM_FIELD');
DELETE FROM ark_module_function WHERE ARK_MODULE_ID=@module_id and ARK_FUNCTION_ID=(SELECT ID FROM ark_function WHERE name='BIOCOLLECTION_CUSTOM_FIELD_UPLOAD');
DELETE FROM ark_module_function WHERE ARK_MODULE_ID=@module_id and ARK_FUNCTION_ID=(SELECT ID FROM ark_function WHERE name='BIOSPECIMEN_CUSTOM_FIELD_UPLOAD');

-- Delete all role policy combination ------------------
DELETE FROM ark_role_policy_template WHERE ARK_MODULE_ID=@module_id and ARK_FUNCTION_ID=(SELECT ID FROM ark_function WHERE name='LIMS_COLLECTION_CUSTOM_FIELD');
DELETE FROM ark_role_policy_template WHERE ARK_MODULE_ID=@module_id and ARK_FUNCTION_ID=(SELECT ID FROM ark_function WHERE name='BIOSPECIMEN_CUSTOM_FIELD');
DELETE FROM ark_role_policy_template WHERE ARK_MODULE_ID=@module_id and ARK_FUNCTION_ID=(SELECT ID FROM ark_function WHERE name='BIOCOLLECTION_CUSTOM_FIELD_UPLOAD');
DELETE FROM ark_role_policy_template WHERE ARK_MODULE_ID=@module_id and ARK_FUNCTION_ID=(SELECT ID FROM ark_function WHERE name='BIOSPECIMEN_CUSTOM_FIELD_UPLOAD');


DELETE FROM ark_function WHERE name='LIMS_COLLECTION_CUSTOM_FIELD';
DELETE FROM ark_function WHERE name='BIOSPECIMEN_CUSTOM_FIELD';
DELETE FROM ark_function WHERE name='BIOCOLLECTION_CUSTOM_FIELD_UPLOAD';
DELETE FROM ark_function WHERE name='BIOSPECIMEN_CUSTOM_FIELD_UPLOAD';



insert into ark_function(name,description,ark_function_type_id,resource_key) 
	values('LIMS_CUSTOM_FIELD','Manage Custom Fields for LIMS.',(SELECT id FROM ark_function_type as af where af.NAME='NON-REPORT'),'tab.module.lims.limscustomfield'); 
insert into ark_function(name,description,ark_function_type_id,resource_key) 
	values('LIMS_CUSTOM_FIELD_UPLOAD','Manage Custom Field Upload for LIMS.',(SELECT id FROM ark_function_type as af where af.NAME='NON-REPORT'),'tab.module.lims.limscustomfieldupload'); 


-- get sequence value
set @sequence=(select function_sequence from ark_module_function where ark_module_id=@module_id and ark_function_id=(select id from ark_function where name='LIMS_CUSTOM_FIELD_CATEGORY'));

-- insert value
insert into ark_module_function(ark_module_id,ark_function_id,function_sequence) values(@module_id,(select id from ark_function where name='LIMS_CUSTOM_FIELD'),@sequence+1);

insert into ark_module_function(ark_module_id,ark_function_id,function_sequence) values(@module_id,(select id from ark_function where name='LIMS_CUSTOM_FIELD_UPLOAD'),@sequence+2);






