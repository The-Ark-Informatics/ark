INSERT INTO study.`study` (`ID`,`NAME`,`DESCRIPTION`,`DATE_OF_APPLICATION`,`ESTIMATED_YEAR_OF_COMPLETION`,`CHIEF_INVESTIGATOR`,`CO_INVESTIGATOR`,`AUTO_GENERATE_SUBJECTUID`,`SUBJECTUID_START`,`STUDY_STATUS_ID`,`SUBJECTUID_PREFIX`,`CONTACT_PERSON`,`CONTACT_PERSON_PHONE`,`LDAP_GROUP_NAME`,`AUTO_CONSENT`,`SUB_STUDY_BIOSPECIMEN_PREFIX`,`STUDY_LOGO`,`FILENAME`,`SUBJECTUID_TOKEN_ID`,`SUBJECTUID_PADCHAR_ID`,`SUBJECT_KEY_PREFIX`,`SUBJECT_KEY_START`,`PARENT_ID`,`AUTO_GENERATE_BIOSPECIMENUID`,`AUTO_GENERATE_BIOCOLLECTIONUID`) 
VALUES (415,'Australian Ovarian Cancer Study',NULL,NULL,NULL,'Dr Nikolajs Zeps',NULL,1,NULL,1,'WTN','Dr Nikolajs Zeps',NULL,'WARTN',1,NULL,NULL,NULL,1,8,NULL,NULL,274,1,1);

update study.study set parent_id = 415 where id =415;
update	lims.Barcode_Label	SET study_id = 415 where study_id =414;
update	lims.BioCollection	SET study_id = 415 where study_id =414;
update	lims.BioCollectionUid_Template	SET study_id = 415 where study_id =414;
update	lims.Biospecimen	SET study_id = 415 where study_id =414;
update	lims.BiospecimenUid_Template	SET study_id = 415 where study_id =414;
update	lims.Study_Inv_Site	SET study_id = 415 where study_id =414;
update	reporting.Search	SET study_id = 415 where study_id =414;
update	study.Ark_User_Role	SET study_id = 415 where study_id =414;
update	study.Consent	SET study_id = 415 where study_id =414;
update	study.Correspondences	SET study_id = 415 where study_id =414;
update	study.Custom_Field	SET study_id = 415 where study_id =414;
update	study.Custom_Field_Group	SET study_id = 415 where study_id =414;
update	study.Link_Study_ArkModule	SET study_id = 415 where study_id =414;
update	study.Link_Study_Studycomp	SET study_id = 415 where study_id =414;
update	study.Link_Study_Studysite	SET study_id = 415 where study_id =414;
update	study.Link_Study_Substudy	SET study_id = 415 where study_id =414;
update	study.Link_Subject_Contact	SET study_id = 415 where study_id =414;
update	study.Link_Subject_Study	SET study_id = 415 where study_id =414;
update	study.Link_Subject_Studycomp	SET study_id = 415 where study_id =414;
update	study.Study_Comp	SET study_id = 415 where study_id =414;
update	study.Upload	SET study_id = 415 where study_id =414;
DELETE FROM `study`.`sTUDY` WHERE `ID`='414';

