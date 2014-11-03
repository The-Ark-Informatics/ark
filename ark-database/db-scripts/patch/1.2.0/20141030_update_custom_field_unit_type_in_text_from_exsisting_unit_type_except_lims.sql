use study;

UPDATE custom_field c 
	inner join unit_type u on c.UNIT_TYPE_ID=u.id  
set c.UNIT_TYPE_IN_TEXT=u.name  
where c.ARK_FUNCTION_ID not in (select id from ark_function a where  a.name='LIMS_COLLECTION' or a.name='BIOSPECIMEN');

Update custom_field c 
set UNIT_TYPE_ID=null 
where c.ARK_FUNCTION_ID not in (select id from ark_function a where  a.name='LIMS_COLLECTION' or a.name='BIOSPECIMEN');
