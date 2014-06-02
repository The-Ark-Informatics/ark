
select * from study.ark_user_role ;

delete from  study.address ;
delete from  study.ark_user_role where ark_user_id <> 1;
delete from  study.ark_user  where id <> 1;
select * from study.ark_user_role;
delete from  study.audit_history ;
delete from  study.consent ;
delete from  study.consent_file ;
delete from  study.correspondences ;
delete from  study.custom_field ;
delete from  study.custom_field_display ;
delete from  study.custom_field_group ;
delete from  study.custom_field_upload ;
delete from  study.email_account ;
delete from  study.link_study_arkmodule ;
delete from  study.link_study_studycomp ;
delete from  study.link_study_studysite ;
delete from  study.link_study_substudy ;
delete from  study.link_subject_contact ;
delete from  study.link_subject_pedigree ;
delete from  study.link_subject_study ;
delete from  study.link_subject_studycomp ;
delete from  study.link_subject_twin ;
delete from  study.padding_character ;    -- this can go or better yet refactor so that we don't ahve 3 differen padchar tables
delete from  study.payload ;
delete from  study.phone ;
delete from  study.relationship ;
delete from  study.study ;
delete from  study.study_comp ;
delete from  study.study_consent_question ;
delete from  study.study_site ;
delete from  study.subject_custom_field_data ;
delete from  study.subject_file ;
delete from  study.subject_study_consent ;
delete from  study.subjectuid_sequence ;
delete from  study.upload ;
delete from  study.upload_error ;
delete from  study.otherid ;
delete from  study.person ;
delete from  study.person_lastname_history ;

/*
one off only...dropping these tables which appear to be junk 
drop table study.temp ;
drop view study.study_user_role_permission_view ;
drop view study.role_policy ;-- delete from  study.role_policy ;  -- THIS IS A VIEW...CAN IT BE DELETED
drop table  study.question_answer ;  -- CAN THIS BE DELETED?
drop table   study.registration_status ; -- CAN THIS BE DELETED?
drop table    study.csv_blob ;  -- TODO CHECK IF STILL NEEDED - maybe drop and all entities
drop table    study.link_site_contact ; -- TODO CHECK IF STILL NEEDED - drop and all entities
*/


/*******************
 LOOK UP TABLES DO NOT DELETE
-- TABLES NOT TO DELETE FROM
delete from  study.consent_answer ;
delete from  study.study_comp_status ;
delete from  study.consent_option ;
delete from  study.consent_section ; -- TODO CHECK IF STILL NEEDED - drop and all entities ??? i couldnt find it's entities
delete from  study.consent_type ;
delete from  study.correspondence_direction_type ;
delete from  study.correspondence_mode_type ;
delete from  study.correspondence_outcome_type ;
delete from  study.ark_role ;
delete from  study.ark_module_role ;
delete from  study.ark_permission ;
delete from  study.ark_role_policy_template ;
delete from  study.ark_module_function ;
delete from  study.action_type ;
delete from  study.address_status ;
delete from  study.address_type ;
-- delete from  study.ark_module ;
-- delete from  study.ark_function ;
-- delete from  study.ark_function_type ;
-- delete from  study.state ;
-- delete from  study.country ;
-- delete from  study.gender_type ;
-- delete from  study.file_format ;
-- delete from  study.email_status ;
-- delete from  study.delimiter_type ;
-- delete from  study.domain_type ;
-- delete from  study.upload_status ;
-- delete from  study.upload_type ;
-- delete from  study.vital_status ;
delete from  study.phone_status ;
delete from  study.phone_type ;
delete from  study.email_account_type ;
delete from  study.entity_type ;
-- delete from  study.yes_no ;
-- delete from  study.field_type ;
-- delete from  study.consent_status ;
-- delete from  study.person_contact_method ;
-- delete from  study.marital_status ;
-- delete from  study.twin_type ;
-- delete from  study.unit_type ;
-- delete from  study.title_type ;
-- delete from  study.study_status ;
-- delete from  study.measurement_type ;
-- delete from  study.subject_status ;
-- delete from  study.subjectuid_token ;
-- delete from  study.subjectuid_padchar ;
*********************/