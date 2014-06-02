/* 

this is a script to delete any tables we don't see being useful anymore

*/

/* drop tables that should not have data - STUDY*/
drop table study.temp ;
drop view study.study_user_role_permission_view ;
drop view study.role_policy ;-- delete from  study.role_policy ;  -- THIS IS A VIEW...CAN IT BE DELETED
drop table  study.question_answer ;  -- CAN THIS BE DELETED?
drop table   study.registration_status ; -- CAN THIS BE DELETED?
drop table    study.csv_blob ;  -- TODO CHECK IF STILL NEEDED - maybe drop and all entities
drop table    study.link_site_contact ; -- TODO CHECK IF STILL NEEDED - drop and all entities


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

