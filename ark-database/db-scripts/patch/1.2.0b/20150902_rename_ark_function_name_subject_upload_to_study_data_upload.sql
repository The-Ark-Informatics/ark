use study;
set @ark_function_id=(SELECT ID FROM ark_function where ark_function.name='SUBJECT_UPLOAD');

update ark_function 
set ark_function.name='STUDY_DATA_UPLOAD',
	ark_function.description='All the study related data uploads.Demographic,Pedigree,Subject attachment,Consent Data',
	ark_function.resource_key='tab.module.study.studyDataUpload'
where id=@ark_function_id;
	
