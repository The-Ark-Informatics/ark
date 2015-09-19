-- Create Lims Custom Filed Category Tab----------------------------------------
use study;
insert into ark_function(name,description,ark_function_type_id,resource_key) 
	values('LIMS_CUSTOM_FIELD_CATEGORY','Manage Custom Fields Category for LIMS.',(SELECT id FROM ark_function_type as af where af.NAME='NON-REPORT'),'tab.module.lims.limscustomfieldcategory'); 


set @module_id=(select id from ark_module where name='LIMS');

-- Insert custom field category tab with sequence maintaining ---------------------------------- 
-- get sequence value
set @sequence=(select function_sequence from ark_module_function where ark_module_id=@module_id and ark_function_id=(select id from ark_function where name='LIMS_COLLECTION_CUSTOM_FIELD'));

-- insert value
insert into ark_module_function(ark_module_id,ark_function_id,function_sequence) values(@module_id,(select id from ark_function where name='LIMS_CUSTOM_FIELD_CATEGORY'),@sequence);


-- increase the sequence by one after the insert field

update  ark_module_function set   function_sequence=function_sequence+1
where ark_module_id=@module_id and ark_function_id=(select id from ark_function where name='LIMS_COLLECTION_CUSTOM_FIELD');

update  ark_module_function set   function_sequence=function_sequence+1
where ark_module_id=@module_id and ark_function_id=(select id from ark_function where name='BIOSPECIMEN_CUSTOM_FIELD');

update  ark_module_function set   function_sequence=function_sequence+1
where ark_module_id=@module_id and ark_function_id=(select id from ark_function where name='BIOCOLLECTION_CUSTOM_FIELD_UPLOAD');

update  ark_module_function set   function_sequence=function_sequence+1
where ark_module_id=@module_id and ark_function_id=(select id from ark_function where name='BIOSPECIMEN_CUSTOM_FIELD_UPLOAD');

update  ark_module_function set   function_sequence=function_sequence+1
where ark_module_id=@module_id and ark_function_id=(select id from ark_function where name='BIOSPECIMEN_UPLOAD');

update  ark_module_function set   function_sequence=function_sequence+1
where ark_module_id=@module_id and ark_function_id=(select id from ark_function where name='BARCODE_LABEL');

update  ark_module_function set   function_sequence=function_sequence+1
where ark_module_id=@module_id and ark_function_id=(select id from ark_function where name='BARCODE_PRINTER');

	
   




