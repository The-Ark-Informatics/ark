------------------------ Create subject custom filed category tab----------------------------------------
use study;
insert into ark_function(name,description,ark_function_type_id,resource_key) 
	values('SUBJECT_CUSTOM_FIELD_CATEGORY','Manage Custom Fields Category for Subjects.',(SELECT id FROM ark_function_type as af where af.NAME='NON-REPORT'),'tab.module.subject.subjectcustomfieldcategory'); 


----- Insert custom field category tab with sequence maintaining ---------------------------------- 
-- get sequence value
set @sequence=(select function_sequence from ark_module_function where ark_module_id=(select id from ark_module where name='Study')
and ark_function_id=(select id from ark_function where name='SUBJECT_CUSTOM_FIELD'));

--insert value
insert into ark_module_function(ark_module_id,ark_function_id,function_sequence) values((select id from ark_module where name='Study'),(select id from ark_function where name='SUBJECT_CUSTOM_FIELD_CATEGORY'),@sequence);


--- increase the sequence by one after the insert field
set @currentseq_1=(select function_sequence from ark_module_function 
where ark_module_id=(select id from ark_module where name='Study')and ark_function_id=(select id from ark_function where name='SUBJECT_CUSTOM_FIELD'));

update  ark_module_function set   function_sequence=@currentseq_1+1
where ark_module_id=(select id from ark_module where name='Study')and ark_function_id=(select id from ark_function where name='SUBJECT_CUSTOM_FIELD');

set @currentseq_2=(select function_sequence from ark_module_function 
where ark_module_id=(select id from ark_module where name='Study')and ark_function_id=(select id from ark_function where name='SUBJECT_CUSTOM_FIELD_UPLOAD'));

update  ark_module_function set   function_sequence=@currentseq_2+1
where ark_module_id=(select id from ark_module where name='Study')and ark_function_id=(select id from ark_function where name='SUBJECT_CUSTOM_FIELD_UPLOAD');

set @currentseq_3=(select function_sequence from ark_module_function 
where ark_module_id=(select id from ark_module where name='Study')and ark_function_id=(select id from ark_function where name='SUBJECT_UPLOAD'));				

update  ark_module_function set   function_sequence=@currentseq_3+1
where ark_module_id=(select id from ark_module where name='Study')and ark_function_id=(select id from ark_function where name='SUBJECT_UPLOAD');				
   




