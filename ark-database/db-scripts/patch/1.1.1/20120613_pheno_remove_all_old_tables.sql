-- dropping a bunch of unused tables/views from pheno
use pheno;
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
drop table pheno.file_format;
drop table pheno.field_upload;
drop view pheno.field_upload_v;
drop view pheno.field_summary;

SET foreign_key_checks = 1;

-- new self explanatory status
INSERT INTO `pheno`.`questionnaire_status` (`NAME`, `DESCRIPTION`) VALUES ('Uploaded From File', 'The Questionnaire data has been update from file, with no further action taken since then.');

-- keep pheno_collection, pheno_data, questionnaire_status, status