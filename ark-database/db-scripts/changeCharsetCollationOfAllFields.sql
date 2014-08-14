SELECT * 
FROM information_schema.`COLUMNS` C
WHERE table_schema in ('study', 'admin', 'audit', 'lims', 'pheno', 'reporting')
order by character_set_name, collation_name;

/* cant do this, but here is the idea
update information_schema.`COLUMNS` C
set collation_name = 'utf8_general_ci',
character_set_name = 'utf8'
WHERE table_schema in ('study', 'admin', 'audit', 'lims', 'pheno', 'reporting');
*/

select distinct 
-- collation_name
character_set_name 
from  information_schema.`COLUMNS` C
WHERE table_schema in ('study', 'admin', 'audit', 'lims', 'pheno', 'reporting');


-- so to generate tables was generate all of these;
SELECT CONCAT('ALTER TABLE `', t.`TABLE_SCHEMA`, '`.`', t.`TABLE_NAME`,
 '` CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;') as stmt 
FROM `information_schema`.`Columns` t
WHERE 1
AND t.`TABLE_SCHEMA`  in ('study', 'admin', 'audit', 'lims', 'pheno', 'reporting') ORDER BY 1;

select * FROM `information_schema`.`Columns` t;

-- so let's generate all of these;
SELECT CONCAT('ALTER TABLE `', t.`TABLE_SCHEMA`, '`.`', t.`TABLE_NAME`,   '` MODIFY `', t.`COLUMN_NAME`, '` ', t.`COLUMN_TYPE`,  ' CHARACTER SET utf8 COLLATE utf8_general_ci;') as stmt 
FROM `information_schema`.`COLUMNS` t
WHERE 1
and t.`data_type` in ('varchar', 'text', 'mediumtext', 'longtext')
and t.character_set_name='latin1'
AND t.`TABLE_SCHEMA`  in ('study', 'admin', 'audit', 'lims', 'pheno', 'reporting') ORDER BY 1;

-- and here is the result for you to run - though you could also do the statement above and generate your own
ALTER TABLE `study`.`consent_section` MODIFY `DESCRIPTION` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`consent_section` MODIFY `NAME` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`consent_status` MODIFY `DESCRIPTION` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`consent_status` MODIFY `NAME` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`consent_type` MODIFY `DESCRIPTION` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`consent_type` MODIFY `NAME` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`consent` MODIFY `COMMENTS` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`consent` MODIFY `CONSENTED_BY` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`correspondences` MODIFY `ATTACHMENT_FILENAME` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`correspondences` MODIFY `COMMENTS` varchar(4096) CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`correspondences` MODIFY `DETAILS` varchar(4096) CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`correspondences` MODIFY `REASON` varchar(4096) CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`correspondences` MODIFY `TIME` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`correspondence_direction_type` MODIFY `DESCRIPTION` varchar(4096) CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`correspondence_direction_type` MODIFY `NAME` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`correspondence_mode_type` MODIFY `DESCRIPTION` varchar(4096) CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`correspondence_mode_type` MODIFY `NAME` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`correspondence_outcome_type` MODIFY `DESCRIPTION` varchar(496) CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`correspondence_outcome_type` MODIFY `NAME` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`country` MODIFY `ALPHA_3_CODE` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`country` MODIFY `COUNTRY_CODE` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`country` MODIFY `NAME` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`country` MODIFY `NUMERIC_CODE` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`country` MODIFY `OFFICIAL_NAME` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`custom_field_display` MODIFY `REQUIRED_MESSAGE` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`custom_field_group` MODIFY `DESCRIPTION` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`custom_field_group` MODIFY `NAME` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`custom_field` MODIFY `CUSTOM_FIELD_LABEL` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`custom_field` MODIFY `DESCRIPTION` text CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`custom_field` MODIFY `ENCODED_VALUES` text CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`custom_field` MODIFY `MAX_VALUE` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`custom_field` MODIFY `MIN_VALUE` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`custom_field` MODIFY `MISSING_VALUE` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`custom_field` MODIFY `NAME` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`delimiter_type` MODIFY `DELIMITER_CHARACTER` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`delimiter_type` MODIFY `DESCRIPTION` text CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`delimiter_type` MODIFY `NAME` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`domain_type` MODIFY `DESCRIPTION` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`domain_type` MODIFY `NAME` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`email_account_type` MODIFY `DESCRIPTION` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`email_account_type` MODIFY `NAME` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`email_account` MODIFY `NAME` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`email_status` MODIFY `DESCRIPTION` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`email_status` MODIFY `NAME` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`entity_type` MODIFY `NAME` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`field_type` MODIFY `NAME` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`file_format` MODIFY `DESCRIPTION` text CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`file_format` MODIFY `NAME` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`gender_type` MODIFY `DESCRIPTION` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`gender_type` MODIFY `NAME` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`link_subject_study` MODIFY `COMMENTS` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`link_subject_study` MODIFY `HEARD_ABOUT_STUDY` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`link_subject_study` MODIFY `SUBJECT_UID` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`marital_status` MODIFY `DESCRIPTION` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`marital_status` MODIFY `NAME` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`measurement_type` MODIFY `DESCRIPTION` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`measurement_type` MODIFY `VALUE` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`otherid` MODIFY `OtherID_Source` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`otherid` MODIFY `OtherID` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`padding_character` MODIFY `NAME` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`person_contact_method` MODIFY `NAME` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`person_lastname_history` MODIFY `LASTNAME` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`person` MODIFY `CAUSE_OF_DEATH` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`person` MODIFY `FIRST_NAME` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`person` MODIFY `LAST_NAME` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`person` MODIFY `MIDDLE_NAME` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`person` MODIFY `OTHER_EMAIL` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`person` MODIFY `PREFERRED_EMAIL` varchar(150) CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`person` MODIFY `PREFERRED_NAME` varchar(150) CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`phone_status` MODIFY `DESCRIPTION` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`phone_status` MODIFY `NAME` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`phone_type` MODIFY `DESCRIPTION` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`phone_type` MODIFY `NAME` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`phone` MODIFY `AREA_CODE` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`phone` MODIFY `COMMENT` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`phone` MODIFY `PHONE_NUMBER` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`phone` MODIFY `SOURCE` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`question_answer` MODIFY `ANSWER` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`registration_status` MODIFY `DESCRIPTION` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`registration_status` MODIFY `REGISTRATION_STATUS` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`relationship` MODIFY `DESCRIPTION` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`relationship` MODIFY `NAME` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`state` MODIFY `CODE` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`state` MODIFY `NAME` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`state` MODIFY `SHORT_NAME` varchar(56) CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`state` MODIFY `TYPE` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`study_comp_status` MODIFY `DESCRIPTION` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`study_comp_status` MODIFY `NAME` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`study_comp` MODIFY `DESCRIPTION` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`study_comp` MODIFY `KEYWORD` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`study_comp` MODIFY `NAME` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`study_consent_question` MODIFY `DISCRETE_VALUES` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`study_consent_question` MODIFY `QUESTION` varchar(700) CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`study_site` MODIFY `DESCRIPTION` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`study_site` MODIFY `NAME` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`study_status` MODIFY `DESCRIPTION` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`study_status` MODIFY `NAME` varchar(25) CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`study` MODIFY `CHIEF_INVESTIGATOR` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`study` MODIFY `CONTACT_PERSON_PHONE` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`study` MODIFY `CONTACT_PERSON` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`study` MODIFY `CO_INVESTIGATOR` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`study` MODIFY `DESCRIPTION` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`study` MODIFY `FILENAME` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`study` MODIFY `LDAP_GROUP_NAME` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`study` MODIFY `NAME` varchar(150) CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`study` MODIFY `SUBJECTUID_PREFIX` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`study` MODIFY `SUBJECT_KEY_PREFIX` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`study` MODIFY `SUBJECT_KEY_START` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`study` MODIFY `SUB_STUDY_BIOSPECIMEN_PREFIX` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`subjectuid_padchar` MODIFY `NAME` varchar(25) CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`subjectuid_sequence` MODIFY `STUDY_NAME_ID` varchar(150) CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`subjectuid_token` MODIFY `NAME` varchar(25) CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`subject_custom_field_data` MODIFY `ERROR_DATA_VALUE` text CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`subject_custom_field_data` MODIFY `TEXT_DATA_VALUE` text CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`subject_file` MODIFY `CHECKSUM` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`subject_file` MODIFY `COMMENTS` text CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`subject_file` MODIFY `FILENAME` text CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`subject_file` MODIFY `USER_ID` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`subject_status` MODIFY `DESCRIPTION` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`subject_status` MODIFY `NAME` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`subject_study_consent` MODIFY `STATUS` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`title_type` MODIFY `DESCRIPTION` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`title_type` MODIFY `NAME` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`twin_type` MODIFY `DESCRIPTION` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`twin_type` MODIFY `NAME` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`unit_type` MODIFY `DESCRIPTION` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`unit_type` MODIFY `NAME` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`upload_error` MODIFY `ERROR_MSG` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`upload_error` MODIFY `ORIGINAL_ROW_DATA` text CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`upload_status` MODIFY `LONG_MESSAGE` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`upload_status` MODIFY `NAME` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`upload_status` MODIFY `SHORT_MESSAGE` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`upload_type` MODIFY `DESCRIPTION` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`upload_type` MODIFY `NAME` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`upload` MODIFY `CHECKSUM` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`upload` MODIFY `FILENAME` text CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`upload` MODIFY `USER_ID` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`user_config` MODIFY `VALUE` text CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`vital_status` MODIFY `DESCRIPTION` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`vital_status` MODIFY `NAME` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE `study`.`yes_no` MODIFY `NAME` varchar(3) CHARACTER SET utf8 COLLATE utf8_general_ci;
