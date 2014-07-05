/* 

this is a script to delete any tables we don't see being useful anymore

*/

/* select * from tables that should not have data - STUDY*/
select * from  study.temp ;
select * from  study.study_user_role_permission_view ;
select * from  study.role_policy ;-- delete from  study.role_policy ;  -- THIS IS A VIEW...CAN IT BE DELETED
select * from   study.question_answer ;  -- CAN THIS BE DELETED?
select * from    study.registration_status ; -- CAN THIS BE DELETED?
select * from     study.csv_blob ;  -- TODO CHECK IF STILL NEEDED - maybe select * from and all entities
select * from     study.link_site_contact ; -- TODO CHECK IF STILL NEEDED - select * from and all entities
drop table study.temp ;
-- not convinced this is a good idea!!!!!!  drop view study.study_user_role_permission_view ;
-- not convinced this is a good idea!!!!!!  drop view study.role_policy ;-- delete from  study.role_policy ;  -- THIS IS A VIEW...CAN IT BE DELETED
drop table  study.question_answer ;  -- CAN THIS BE DELETED?
drop table   study.registration_status ; -- CAN THIS BE DELETED?
drop table    study.csv_blob ;  -- TODO CHECK IF STILL NEEDED - maybe drop and all entities
drop table    study.link_site_contact ; -- TODO CHECK IF STILL NEEDED - drop and all entities


/* select * from tables that should not have data - LIMS*/
select * from  lims.biodata ;
select * from  lims.biodata_field_group ;
select * from  lims.biodata_field_lov ;
select * from  lims.biodata_unit ;
select * from  lims.biodata_type ;
select * from  lims.biodata_lov_list ;
select * from  lims.biodata_group_criteria ;
select * from  lims.biodata_group ;
select * from  lims.biodata_field ;
select * from  lims.biodata_criteria ;
select * from  lims.tmp_bio_transaction ;
select * from   lims.tmp_biospecimen ;
select * from   lims.tmp_biospecimen_custom_field_data ;



/* drop tables that should not have data - LIMS*/
drop table lims.biodata ;
drop table lims.biodata_field_group ;
drop table lims.biodata_field_lov ;
drop table lims.biodata_unit ;
drop table lims.biodata_type ;
drop table lims.biodata_lov_list ;
drop table lims.biodata_group_criteria ;
drop table lims.biodata_group ;
drop table lims.biodata_field ;
drop table lims.biodata_criteria ;
drop table lims.tmp_bio_transaction ;
drop  table lims.tmp_biospecimen ;
drop  table lims.tmp_biospecimen_custom_field_data ;

