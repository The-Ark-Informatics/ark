-- dropping a bunch of unused tables from pheno

SET foreign_key_checks = 0;
drop table pheno.collection;
drop table pheno.collection_upload;
drop table pheno.delimiter_type;
drop table pheno.field;
drop table pheno.field_collection;
drop table pheno.field_data;
drop table pheno.field_data_log;
drop table pheno.field_field_group;
drop table pheno.field_group;
drop table pheno.field_group_upload;
drop table pheno.field_summary;
drop table pheno.field_type;
drop table pheno.field_upload;
drop table pheno.field_upload_v;
drop table pheno.file_format;
SET foreign_key_checks = 1;

-- keep pheno_collection, pheno_data, questionnaire_status, status
