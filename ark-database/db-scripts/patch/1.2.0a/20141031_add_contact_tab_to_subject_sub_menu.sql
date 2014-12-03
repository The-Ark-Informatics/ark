-- Create Contact tab in the ark_function table.------
use study;
insert into ark_function(name,description,ark_function_type_id,resource_key) 
	values('CONTACT','Manage the phone and address details.',(SELECT id FROM ark_function_type as af where af.NAME='NON-REPORT'),'tab.module.person.contact'); 
-- Update(Replace) the "contact" with "phone" tab. 
Update ark_module_function 
set ARK_FUNCTION_ID=(select id from ark_function where name='CONTACT')
where (ARK_MODULE_ID=(select id from ark_module where name='Subject') AND (ARK_FUNCTION_ID=(select id from ark_function where name='PHONE')));

-- Remove the "Address" tab from the menu.
Delete from ark_module_function where (ARK_MODULE_ID=(select id from ark_module where name='Subject') AND (ARK_FUNCTION_ID=(select id from ark_function where name='ADDRESS')));

