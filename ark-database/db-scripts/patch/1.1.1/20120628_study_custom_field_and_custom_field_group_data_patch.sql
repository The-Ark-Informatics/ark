-- this is intended to create uniformity in how all pheno data fields and groups are seen and accessed through the pheno module

update custom_field_group set ark_function_id = 14 where ark_function_id = 12;

update custom_field set ark_function_id = 14 where ark_function_id = 12;
