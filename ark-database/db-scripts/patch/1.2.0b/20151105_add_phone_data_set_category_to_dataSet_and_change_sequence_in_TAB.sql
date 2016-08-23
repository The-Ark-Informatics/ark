-- Create Data Set Category Tab----------------------------------------
use study;
insert into ark_function(name,description,ark_function_type_id,resource_key) 
	values('DATASET_CATEGORY','Phenotypic Data set category use case. This is represented by the Dataset Category tab, under the main Phenotypic(Dataset) Tab.',(SELECT id FROM ark_function_type as af where af.NAME='NON-REPORT'),'tab.module.phenotypic.category'); 

insert into ark_function(name,description,ark_function_type_id,resource_key) 
	values('DATASET_CATEGORY_UPLOAD','Phenotypic Data set category upload use case. This is represented by the Dataset Category tab, under the main Phenotypic(Dataset) Tab.',(SELECT id FROM ark_function_type as af where af.NAME='NON-REPORT'),'tab.module.phenotypic.categoryupload'); 

set @module_id=(select id from ark_module where name='Datasets');




-- Insert Data Set category tab with sequence maintaining ---------------------------------- 

-- insert value
insert into ark_module_function(ark_module_id,ark_function_id,function_sequence) values(@module_id,(select id from ark_function where name='DATASET_CATEGORY'),1);
insert into ark_module_function(ark_module_id,ark_function_id,function_sequence) values(@module_id,(select id from ark_function where name='DATASET_CATEGORY_UPLOAD'),2);


-- increase the sequence by one after the insert field of dataset category and data set category upload.

update  ark_module_function set   function_sequence=3
where ark_module_id=@module_id and ark_function_id=(select id from ark_function where name='DATA_DICTIONARY');

update  ark_module_function set   function_sequence=4
where ark_module_id=@module_id and ark_function_id=(select id from ark_function where name='DATA_DICTIONARY_UPLOAD');

update  ark_module_function set   function_sequence=5
where ark_module_id=@module_id and ark_function_id=(select id from ark_function where name='PHENO_COLLECTION');

update  ark_module_function set   function_sequence=6
where ark_module_id=@module_id and ark_function_id=(select id from ark_function where name='FIELD_DATA');

update  ark_module_function set   function_sequence=7
where ark_module_id=@module_id and ark_function_id=(select id from ark_function where name='FIELD_DATA_UPLOAD');


