-- Create Lims Custom Filed Category Tab----------------------------------------
use study;

set @module_id=(select id from ark_module where name='Datasets');

delete from ark_module_function where ark_module_id= @module_id and ark_function_id=(select id from ark_function where name='DATASET_CATEGORY_UPLOAD');


	
   




