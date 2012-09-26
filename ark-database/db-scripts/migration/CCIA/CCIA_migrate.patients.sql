USE `ccia-live-28July`;

-- Insert Study
INSERT INTO study.study(`NAME`,
`DESCRIPTION`,
`DATE_OF_APPLICATION`,
`ESTIMATED_YEAR_OF_COMPLETION`,
`CHIEF_INVESTIGATOR`,
`CO_INVESTIGATOR`,
`AUTO_GENERATE_SUBJECTUID`,
`SUBJECTUID_START`,
`STUDY_STATUS_ID`,
`SUBJECTUID_PREFIX`,
`CONTACT_PERSON`,
`CONTACT_PERSON_PHONE`,
`LDAP_GROUP_NAME`,
`AUTO_CONSENT`,
`SUB_STUDY_BIOSPECIMEN_PREFIX`,
`STUDY_LOGO`,
`FILENAME`,
`SUBJECTUID_TOKEN_ID`,
`SUBJECTUID_PADCHAR_ID`,
`SUBJECT_KEY_PREFIX`,
`SUBJECT_KEY_START`,
`PARENT_ID`)
VALUES ('CCIA', 'Children''s Cancer Institute Australia for Medical Research', NULL, NULL, 'John Sullivan', NULL, '0', NULL, '1', NULL, 'John Sullivan', '02 9385 2546', NULL, '0', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);

-- Split-str function
CREATE FUNCTION `SPLIT_STR`(`x` VARCHAR(255), `delim` VARCHAR(12), `pos` INT
)
	RETURNS varchar(255)
	LANGUAGE SQL
	NOT DETERMINISTIC
	CONTAINS SQL
	SQL SECURITY DEFINER
	COMMENT ''
RETURN REPLACE(SUBSTRING(SUBSTRING_INDEX(x, delim, pos),
       LENGTH(SUBSTRING_INDEX(x, delim, pos -1)) + 1),
       delim, '');

-- Insert person details
INSERT INTO study.person (OTHER_ID, FIRST_NAME, MIDDLE_NAME, LAST_NAME, DATE_OF_BIRTH, VITAL_STATUS_ID, GENDER_TYPE_ID, DATE_OF_DEATH, CAUSE_OF_DEATH)
SELECT
  PATIENTKEY as OTHER_ID,
  split_str(FIRSTNAME, ' ', 1) as FIRST_NAME,
  split_str(FIRSTNAME, ' ', 2) as MIDDLE_NAME,
  SURNAME as LAST_NAME,
  DOB as DATE_OF_BIRTH,
  (SELECT id FROM study.vital_status WHERE name = (CASE UPPER(STATUS) WHEN 'DEAD' THEN 'Deceased' WHEN 'ALIVE' THEN 'Alive' END)) as VITAL_STATUS_ID,
  (SELECT id FROM study.gender_type WHERE UPPER(NAME) = UPPER(SEX)) as GENDER_TYPE_ID,
  DODEATH as DATE_OF_DEATH,
  CAUSEOD as CAUSE_OF_DEATH
FROM ix_patient;

-- Insert subject/consent details
INSERT INTO study.link_subject_study (person_id, study_id, subject_status_id, subject_uid, consent_status_id, consent_to_active_contact_id, consent_type_id, comments)
SELECT 
    `person`.`id`,
    `s`.`id` as `study_id`,
1 as `subject_status_id`,
    `p`.`PATIENTID` as `subject_uid`,
    1 as `consent_status_id`,
    co.id as `consent_to_active_contact_id`,
    2 as `consent_type_id`,
    `c`.`comments`
FROM
    `ix_patient` `p`,
    `ix_consent` `c`,
    `study`.`person`,
    `study`.`study` `s`,
    `study`.`consent_option` co
WHERE    `p`.`PATIENTKEY` = `c`.`PATIENTKEY` 
AND `c`.`DELETED` = 0
AND `p`.`PATIENTKEY` = `person`.`OTHER_ID` 
AND `person`.`OTHER_ID` IS NOT NULL
AND co.NAME = c.CONTACT_OK
AND s.NAME = 'CCIA';

-- insert initial records for demographic consent tracking
-- NOTE: this query must be performed *after* the query to insert link_subject_study records (i.e. the query above)
INSERT audit.lss_consent_history(link_subject_study_id, consent_to_active_contact_id, consent_to_passive_data_gathering_id, consent_to_use_data_id, consent_status_id, consent_type_id, consent_date, consent_downloaded) SELECT 
id, 
consent_to_active_contact_id, 
consent_to_passive_data_gathering_id, 
consent_to_use_data_id, 
consent_status_id, 
consent_type_id, 
consent_date, 
consent_downloaded 
FROM study.link_subject_study
WHERE study_id = (SELECT id FROM study.study WHERE name = 'CCIA');

-- Insert admissions/bioCollection
INSERT INTO `lims`.`biocollection`
(`NAME`,
`LINK_SUBJECT_STUDY_ID`,
`STUDY_ID`,
`COLLECTIONDATE`,
`DELETED`,
`TIMESTAMP`,
`COMMENTS`,
`HOSPITAL`,
`SURGERYDATE`,
`DIAG_CATEGORY`,
`REF_DOCTOR`,
`PATIENTAGE`,
`DISCHARGEDATE`,
`HOSPITAL_UR`,
`DIAG_DATE`)
SELECT 
    `adm`.`ADMISSIONKEY` as `NAME`,
    `lss`.`id`,
    `lss`.`study_id`,
    `adm`.`admissiondate` as `collectiondate`,
    `adm`.`deleted`,
    `adm`.`timestamp`,
    `adm`.`comments`,
    `adm`.`hospital`,
    `adm`.`surgerydate` as `surgerydate`,
    `adm`.`diag_category`,
    `adm`.`ref_doctor`,
    `adm`.`patientage`,
    `adm`.`dischargedate` as `dischargedate`,
    `adm`.`hospital_ur`,
    `adm`.`diag_date` as `diag_date`
FROM
    `ix_admissions` `adm`,
    `ix_patient` `p`,
    `study`.`link_subject_study` `lss`
WHERE
    `adm`.`patientkey` = `p`.`patientkey` 
AND `adm`.`patientkey` = `p`.`patientkey` 
AND `p`.`patientid` = `lss`.`subject_uid`;

-- CCIA sampletypes
INSERT INTO `lims`.`bio_sampletype`
(
`NAME`,
`SAMPLETYPE`,
`SAMPLESUBTYPE`)
SELECT s.*
FROM
(SELECT DISTINCT
    CASE
        WHEN `sampletype` LIKE '%Marrow%' THEN CONCAT('Tissue', ' / ', `sampletype`)
        WHEN `sampletype` LIKE '%Blood%' THEN CONCAT('Blood', ' / ', `sampletype`)
	   WHEN `sampletype` LIKE '%DNA%' THEN CONCAT('Nucleic Acid', ' / ', `sampletype`)
        ELSE `sampletype`
    END as `name`,
CASE
        WHEN `sampletype` LIKE '%Marrow%' THEN CONCAT('Tissue')
        WHEN `sampletype` LIKE '%Blood%' THEN CONCAT('Blood')
        WHEN `sampletype` LIKE '%DNA%' THEN CONCAT('Nucleic Acid')
        ELSE `sampletype`
    END as `sampletype`,
    `sampletype` as `samplesubtype`
FROM
    (SELECT 
        `b`.`sampletype`
    FROM
        `ix_biospecimen` `b`) b) s
WHERE s.`name` not in (SELECT NAME FROM lims.bio_sampletype);

-- Biocollections that didn't exist!
INSERT INTO `lims`.`biocollection`
(`NAME`,
`LINK_SUBJECT_STUDY_ID`,
`STUDY_ID`
)
SELECT DISTINCT CURRENT_TIMESTAMP(), link_subject_study_id, study_id
FROM
(
SELECT 
    `b`.`biospecimenid` as `biospecimen_uid`,
    `lss`.`study_id`,
    `lss`.`id` as `link_subject_study_id`,
    (SELECT min(id) FROM lims.bio_sampletype WHERE (samplesubtype) = (b.sampletype)) AS `sampletype_id`,
    (SELECT min(id) FROM lims.biocollection bc WHERE (bc.link_subject_study_id, bc.study_id) = (lss.id, lss.study_id)) AS `biocollection_id`,
    `b`.`parentid`,
    `b`.`biospecimenkey` as `old_id`,
    `b`.`parentkey` as `oldparent_id`,
    `b`.`deleted`,
    `b`.`timestamp`,
    `b`.`biospecimenid` `otherid`,
    `b`.`dateextracted` as processed_date,
    `b`.`extracted_time` as processed_time,
    `b`.`sampledate` as sample_date,
    `b`.`sampledate` as sample_time,
    `b`.`sampletype` as sampletype,
    `b`.`depth`,
    1 as `BIOSPECIMEN_SPECIES_ID`,
    `b`.`qty_collected`,
    `b`.`qty_removed`,
    `b`.`comments`,
    `b`.`qty_remain` as `quantity`,
    1 as `treatment_type_id`,
    0 as `barcoded`
FROM
    `ix_biospecimen` `b`,
    `ix_patient` `p`,
	`study`.`link_subject_study` `lss`,
    `study`.`study` `s`
WHERE
    `b`.`patientkey` = `p`.`patientkey` 
AND `p`.`patientid` = `lss`.`subject_uid` 
AND `lss`.`study_id` = s.id
AND s.NAME = 'CCIA'
) b
WHERE biocollection_id IS NULL;

-- Biospecimens
INSERT INTO `lims`.`biospecimen`
(`BIOSPECIMEN_UID`,
`STUDY_ID`,
`LINK_SUBJECT_STUDY_ID`,
`SAMPLETYPE_ID`,
`BIOCOLLECTION_ID`,
`PARENTID`,
`OLD_ID`,
`OLDPARENT_ID`,
`DELETED`,
`TIMESTAMP`,
`OTHERID`,
`PROCESSED_DATE`,
`PROCESSED_TIME`,
`SAMPLE_DATE`,
`SAMPLE_TIME`,
`SAMPLETYPE`,
`DEPTH`,
`BIOSPECIMEN_SPECIES_ID`,
`QTY_COLLECTED`,
`QTY_REMOVED`,
`COMMENTS`,
`QUANTITY`,
`TREATMENT_TYPE_ID`,
`BARCODED`)
SELECT 
    `b`.`biospecimenid` as `biospecimen_uid`,
    `lss`.`study_id`,
    `lss`.`id` as `link_subject_study_id`,
    (SELECT min(id) FROM lims.bio_sampletype WHERE (samplesubtype) = (b.sampletype)) AS `sampletype_id`,
    (SELECT min(id) FROM lims.biocollection bc WHERE (bc.link_subject_study_id, bc.study_id) = (lss.id, lss.study_id)) AS `biocollection_id`,
    `b`.`parentid`,
    `b`.`biospecimenkey` as `old_id`,
    `b`.`parentkey` as `oldparent_id`,
    `b`.`deleted`,
    `b`.`timestamp`,
    `b`.`biospecimenid` `otherid`,
    `b`.`dateextracted` as processed_date,
    `b`.`extracted_time` as processed_time,
    `b`.`sampledate` as sample_date,
    `b`.`sample_time` as sample_time,
    `b`.`sampletype` as sampletype,
    `b`.`depth`,
    1 as `BIOSPECIMEN_SPECIES_ID`,
    `b`.`qty_collected`,
    `b`.`qty_removed`,
    `b`.`comments`,
    `b`.`qty_remain` as `quantity`,
    1 as `treatment_type_id`,
    0 as `barcoded`
FROM
    `ix_biospecimen` `b`,
    `ix_patient` `p`,
	`study`.`link_subject_study` `lss`,
    `study`.`study` `s`
WHERE
    `b`.`patientkey` = `p`.`patientkey` 
AND `p`.`patientid` = `lss`.`subject_uid` 
AND `lss`.`study_id` = s.id
AND s.NAME = 'CCIA';

-- bio_transaction
INSERT INTO `lims`.`bio_transaction`
(`BIOSPECIMEN_ID`,
`TRANSACTION_DATE`,
`QUANTITY`,
`RECORDER`,
`REASON`)
SELECT biospecimen.id, bt.transactiondate, bt.quantity, bt.recorder, bt.reason
FROM ix_bio_transactions bt, lims.biospecimen
WHERE bt.biospecimenkey = biospecimen.old_id
