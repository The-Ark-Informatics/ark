use study;
alter table study.custom_field_group  add unique (name,study_id,ark_function_id);
