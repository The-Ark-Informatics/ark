delete from study.ark_module_function
where ark_module_id = (Select id from study.ark_module where name='lims')
        and ark_function_id in (select id from study.ark_function where name in ('LIMS_SUBJECT','BIOSPECIMEN'));
