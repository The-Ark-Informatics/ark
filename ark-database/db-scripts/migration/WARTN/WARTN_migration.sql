
-- Remove any existing data
DELETE FROM lims.inv_cell WHERE box_id IN 
    (SELECT ID FROM lims.inv_box WHERE rack_id IN 
        (SELECT ID FROM lims.inv_rack WHERE freezer_id IN 
            (SELECT ID FROM lims.inv_freezer WHERE site_id IN
                (SELECT ID FROM lims.inv_site WHERE name = 'SJOG'))));
DELETE FROM lims.inv_box WHERE rack_id IN 
        (SELECT ID FROM lims.inv_rack WHERE freezer_id IN 
            (SELECT ID FROM lims.inv_freezer WHERE site_id IN
                (SELECT ID FROM lims.inv_site WHERE name = 'SJOG')));
DELETE FROM lims.inv_rack WHERE freezer_id IN 
            (SELECT ID FROM lims.inv_freezer WHERE site_id IN
                (SELECT ID FROM lims.inv_site WHERE name = 'SJOG'));
DELETE FROM lims.inv_freezer WHERE site_id IN
                (SELECT ID FROM lims.inv_site WHERE name = 'SJOG');
DELETE FROM lims.study_inv_site WHERE inv_site_id IN (SELECT ID FROM lims.inv_site WHERE name = 'SJOG');
DELETE FROM lims.inv_site WHERE name = 'SJOG';
DELETE FROM `lims`.`bio_transaction` WHERE biospecimen_id IN (SELECT ID FROM `lims`.`biospecimen` WHERE study_id in (SELECT ID FROM study.study WHERE parent_id = 274));
-- NOTE: Cascades biospecimen_custom_field_data
DELETE FROM `lims`.`biospecimen` WHERE study_id in (SELECT ID FROM study.study WHERE parent_id = 274);
-- NOTE: Cascades biocollection_custom_field_data
DELETE FROM `lims`.`biocollection` WHERE study_id in (SELECT ID FROM study.study WHERE parent_id = 274);
DELETE FROM study.custom_field WHERE study_id in (SELECT ID FROM study.study WHERE parent_id = 274);
DELETE FROM study.link_subject_study WHERE person_id IN (SELECT id FROM study.person WHERE OTHER_ID IN (SELECT subjectkey FROM zeus.SUBJECT WHERE studykey=274));
DELETE FROM study.phone WHERE person_id IN (SELECT id FROM study.person WHERE OTHER_ID IN (SELECT subjectkey FROM zeus.SUBJECT WHERE studykey=274));
DELETE FROM study.address WHERE person_id IN (SELECT id FROM study.person WHERE OTHER_ID IN (SELECT subjectkey FROM zeus.SUBJECT WHERE studykey=274));
DELETE FROM study.person WHERE OTHER_ID IN (SELECT subjectkey FROM zeus.SUBJECT WHERE studykey=274);
DELETE FROM study.study WHERE parent_id = 274;
DELETE FROM study.study WHERE ldap_group_name = 'WARTN';

-- Insert Study
INSERT INTO study.study(
`ID`,
`NAME`,
`DESCRIPTION`,
`AUTO_GENERATE_SUBJECTUID`,
`STUDY_STATUS_ID`,
`SUBJECTUID_PREFIX`,
`CONTACT_PERSON`,
`CHIEF_INVESTIGATOR`,
`LDAP_GROUP_NAME`,
`AUTO_CONSENT`,
`SUBJECTUID_TOKEN_ID`,
`SUBJECTUID_PADCHAR_ID`,
`PARENT_ID`,
`AUTO_GENERATE_BIOSPECIMENUID`,
`AUTO_GENERATE_BIOCOLLECTIONUID`)
SELECT (CASE UPPER(ss.substudy_name) WHEN 'WARTN' THEN 274 ELSE ss.substudykey END) as ID, 
ss.substudy_name AS NAME, 
ss.description AS DESCRIPTION,
1 as AUTO_GENERATE_SUBJECTUID,
1 as STUDY_STATUS_ID,
s.SUBJECTID_PREFIX as SUBJECTUID_PREFIX, 
s.OWNER AS `CONTACT_PERSON`,
s.OWNER AS `CHIEF_INVESTIGATOR`,
s.LDAP_GROUP AS LDAP_GROUP_NAME,
s.AUTO_CONSENT as AUTO_CONSENT,
1 as SUBJECTUID_TOKEN_ID,
8 as SUBJECTUID_PADCHAR_ID,
s.studykey as PARENT_ID,
1 as AUTO_GENERATE_BIOSPECIMENUID,
1 as AUTO_GENERATE_BIOCOLLECTIONUID
FROM zeus.STUDY s, zeus.ZE_SUBSTUDY ss
WHERE s.studykey= ss.studykey
AND s.studyname='WARTN'
ORDER BY ID;

-- Add missed titles
INSERT INTO study.title_type (ID, NAME)
SELECT DISTINCT (SELECT MAX(ID)+1 FROM study.title_type), concat( upper(substring(title,1,1)),lower(substring(title,2)) ) FROM zeus.SUBJECT
WHERE TITLE NOT IN (SELECT (NAME) FROM study.title_type);

-- Insert person details
INSERT INTO study.person (OTHER_ID, FIRST_NAME, MIDDLE_NAME, LAST_NAME, DATE_OF_BIRTH, VITAL_STATUS_ID, GENDER_TYPE_ID, TITLE_TYPE_ID, DATE_OF_DEATH, CAUSE_OF_DEATH)
SELECT
  SUBJECTKEY as OTHER_ID,
  FIRSTNAME as FIRST_NAME,
  MIDDLENAME as MIDDLE_NAME,
  SURNAME as LAST_NAME,
  DOB as DATE_OF_BIRTH,
  (SELECT id FROM study.vital_status WHERE name = (IF(DATE_OF_DEATH IS NOT NULL, 'Deceased', 'Alive'))) as VITAL_STATUS_ID,
  (SELECT id FROM study.gender_type WHERE UPPER(study.gender_type.NAME) = UPPER(CASE UPPER(SEX) WHEN 'M' THEN 'Male' WHEN 'F' THEN 'Female' ELSE 'Unknown' END)) as GENDER_TYPE_ID,
  (SELECT id FROM study.title_type WHERE UPPER(study.title_type.NAME) = UPPER(IF(TITLE IS NULL, 'Unknown', TITLE))) as TITLE_TYPE_ID,
  DATE_OF_DEATH as DATE_OF_DEATH,
  CAUSE_OF_DEATH as CAUSE_OF_DEATH
FROM zeus.SUBJECT
WHERE studykey=274;

-- Home phone
INSERT INTO study.phone (area_code, phone_number, person_id, phone_type_id, phone_status_id)
SELECT 
    '08' as area_code,
     REPLACE(TRIM(LEADING '08 ' FROM sub.home_phone), " ", "") as phone_number,
    `person`.`id`,
    (SELECT id FROM study.phone_type WHERE UPPER(name) = 'HOME') as phone_type,
    1 as `phone_status_id`
FROM
zeus.STUDY s, zeus.SUBJECT sub, study.person
WHERE s.studykey = sub.studykey
AND sub.`SUBJECTKEY` = `person`.`OTHER_ID` 
AND `person`.`OTHER_ID` IS NOT NULL
AND sub.home_phone IS NOT NULL
AND s.studyname='WARTN';

-- Work phone
INSERT INTO study.phone (area_code, phone_number, person_id, phone_type_id, phone_status_id)
SELECT 
    '08' as area_code, 
    REPLACE(sub.work_phone, " ", "") as phone_number,
    `person`.`id`,
    (SELECT id FROM study.phone_type WHERE UPPER(name) = 'WORK') as phone_type,
    1 as `phone_status_id`
FROM
zeus.STUDY s, zeus.SUBJECT sub, study.person
WHERE s.studykey = sub.studykey
AND sub.`SUBJECTKEY` = `person`.`OTHER_ID` 
AND `person`.`OTHER_ID` IS NOT NULL
AND sub.work_phone IS NOT NULL
AND s.studyname='WARTN';

-- Mobile phone
INSERT INTO study.phone (area_code, phone_number, person_id, phone_type_id, phone_status_id)
SELECT 
    null as area_code, 
    REPLACE(sub.mobile_phone, " ", "") as phone_number,
    `person`.`id`,
    (SELECT id FROM study.phone_type WHERE UPPER(name) = 'MOBILE') as phone_type,
    1 as `phone_status_id`
FROM
zeus.STUDY s, zeus.SUBJECT sub, study.person
WHERE s.studykey = sub.studykey
AND sub.`SUBJECTKEY` = `person`.`OTHER_ID` 
AND `person`.`OTHER_ID` IS NOT NULL
AND sub.mobile_phone IS NOT NULL
AND s.studyname='WARTN';

-- Address
INSERT INTO study.address
(`ADDRESS_LINE_1`,`STREET_ADDRESS`,`CITY`,`STATE_ID`,`POST_CODE`,`COUNTRY_ID`,`ADDRESS_STATUS_ID`,`ADDRESS_TYPE_ID`,`OTHER_STATE`,`PERSON_ID`,`PREFERRED_MAILING_ADDRESS`)
SELECT 
sub.ADDR_STREET AS `ADDRESS_LINE_1`,
null  AS `STREET_ADDRESS`,
sub.ADDR_SUBURB AS `CITY`,
(SELECT id FROM study.state WHERE short_name = sub.ADDR_STATE OR name = sub.ADDR_STATE)`STATE_ID`,
sub.ADDR_POSTCODE `POST_CODE`,
(SELECT id FROM study.country WHERE NAME = sub.ADDR_COUNTRY) `COUNTRY_ID`,
1 AS `ADDRESS_STATUS_ID`,
1 AS `ADDRESS_TYPE_ID`,
null AS `OTHER_STATE`,
`person`.`id`  as `PERSON_ID`,
1 AS `PREFERRED_MAILING_ADDRESS`
FROM
zeus.STUDY s, zeus.SUBJECT sub, study.person
WHERE s.studykey = sub.studykey
AND sub.`SUBJECTKEY` = `person`.`OTHER_ID` 
AND `person`.`OTHER_ID` IS NOT NULL
AND s.studyname='WARTN';

-- Insert subject/consent details into parent study
INSERT INTO study.link_subject_study (person_id, study_id, subject_status_id, subject_uid, consent_status_id)
SELECT 
    `person`.`id`,
    274 as `study_id`,
    1 as `subject_status_id`,
    sub.`SUBJECTID` as `subject_uid`,
    1 as `consent_status_id`
FROM
zeus.STUDY s, zeus.SUBJECT sub, study.person
WHERE s.studykey = sub.studykey
AND sub.`SUBJECTKEY` = `person`.`OTHER_ID` 
AND `person`.`OTHER_ID` IS NOT NULL
AND s.studyname='WARTN';

-- Insert child-study subjects
-- Based on consent to particular sub-study, if not consented to any sub-study (or consent in fact wrong, subjects will be missed)
INSERT INTO study.link_subject_study (person_id, study_id, subject_status_id, subject_uid, consent_status_id, consent_date, comments)
SELECT 
    `person`.`id` as `person_id`,
    ss.substudykey as `study_id`,
    1 as `subject_status_id`,
    sub.`SUBJECTID` as `subject_uid`,
    IF(csub.status = 1, csub.status, null) as `consent_status_id`,
    csub.consent_date,
    csub.comments
FROM
zeus.STUDY s, zeus.ZE_SUBSTUDY ss, zeus.SUBJECT sub, study.person, zeus.CONSENT_STUDY_SECTION css, zeus.CONSENT_SUBJECT_SECTION csub
WHERE s.studykey = sub.studykey
AND s.studykey = ss.studykey
AND sub.SUBJECTKEY = study.person.OTHER_ID
AND study.person.OTHER_ID IS NOT NULL
AND s.studyname='WARTN'
AND css.consentsectionkey=300 AND css.substudykey = ss.substudykey
AND csub.consstudysectkey = css.consstudysectkey
AND csub.subjectkey = sub.subjectkey;

-- Some subjects/sub-studies may have been missed. This adds any missed based on admission sub-study (should only have the one "dodgy" subject)
-- run select as a check first, but otherwise not needed to execute INSERT
INSERT INTO study.link_subject_study (person_id, study_id, subject_status_id, subject_uid, comments)
SELECT 
    p.`id` as `person_id`,
    IF(collectiongroupkey IS NULL, 274, IF(collectiongroupkey = 404, 274, collectiongroupkey)) as `study_id`,
    1 as `subject_status_id`,
    zeus.SUBJECT.`SUBJECTID` as `subject_uid`,
    'Added to study, as collection exists but consent may have been incorrect' as comments
FROM wagerlab.IX_ADMISSIONS adm, study.person p, zeus.SUBJECT
WHERE adm.studykey=274
AND adm.DELETED = 0
AND p.other_id = adm.patientkey
AND adm.patientkey = zeus.SUBJECT.subjectkey
AND (adm.admissionid, adm.patientkey, adm.collectiongroupkey) NOT IN
(
SELECT admissionid, patientkey, collectiongroupkey
FROM
    wagerlab.`IX_ADMISSIONS` `adm`,
    zeus.SUBJECT s,
    zeus.ZE_SUBSTUDY ss,
    `study`.`link_subject_study` `lss`
WHERE
    `adm`.`patientkey` = s.SUBJECTKEY
AND s.subjectid = `lss`.`subject_uid`
AND `lss`.study_id = `adm`.collectiongroupkey
AND `adm`.studykey = s.studykey
AND `adm`.collectiongroupkey = ss.substudykey
AND ss.studykey = 274
AND `adm`.`DELETED` = 0
);

-- Insert admissions/bioCollection
-- Based on particular sub-study, if sub-study (collectiongroupkey) is incorrect, collections will be missed
INSERT INTO `lims`.`biocollection`
(
`BIOCOLLECTION_UID`,
`NAME`,
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
    `adm`.`admissionid` as `BIOCOLLECTION_UID`,
    `adm`.`admissionid` as `NAME`,
    `lss`.`id` as `LINK_SUBJECT_STUDY_ID`,
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
    wagerlab.`IX_ADMISSIONS` `adm`,
    zeus.SUBJECT s,
    zeus.ZE_SUBSTUDY ss,
    `study`.`link_subject_study` `lss`
WHERE
    `adm`.`patientkey` = s.SUBJECTKEY
AND s.subjectid = `lss`.`subject_uid`
AND `lss`.study_id = `adm`.collectiongroupkey
AND `adm`.studykey = s.studykey
AND `adm`.collectiongroupkey = ss.substudykey
AND ss.studykey = 274
AND `adm`.`DELETED` = 0;

-- Insert biospecimen sampletypes that may not exist
INSERT INTO lims.bio_sampletype (name, sampletype, samplesubtype)
SELECT DISTINCT CONCAT(sampletype, ' / ', samplesubtype), sampletype, samplesubtype FROM wagerlab.IX_BIOSPECIMEN
WHERE (sampletype, samplesubtype) NOT IN (SELECT sampletype, samplesubtype FROM lims.bio_sampletype);

-- TODO: HANDLE FOR STORED_IN / GRADE / 

/*
-- Any Treatment types not already matched
SELECT tt.* 
FROM lims.treatment_type tt 
WHERE UPPER(tt.name) NOT IN (SELECT DISTINCT UPPER(b.TREATMENT) FROM wagerlab.IX_BIOSPECIMEN b WHERE studykey=274);
*/

-- Any Units not already matched
/* NOTE: COULD RUN THIS ON ORACLE COPY FIRST
UPDATE wagerlab.IX_BIOSPECIMEN b
INNER JOIN 
(
SELECT bt.biospecimenkey, IFNULL(MIN(bt.unit), 'unit') as units
FROM wagerlab.IX_BIO_TRANSACTIONS bt, wagerlab.IX_BIOSPECIMEN b
WHERE b.studykey=274
AND bt.biospecimenkey = b.biospecimenkey
GROUP BY bt.biospecimenkey
) bt ON b.biospecimenkey = bt.biospecimenkey
SET b.units = bt.units;
*/

INSERT INTO `lims`.`biospecimen_protocol` (NAME) 
SELECT DISTINCT protocol FROM wagerlab.IX_BIOSPECIMEN WHERE protocol IS NOT NULL
AND protocol NOT IN (SELECT NAME FROM lims.biospecimen_protocol);

INSERT INTO `lims`.`biospecimen_grade` (NAME) 
SELECT DISTINCT GRADE FROM wagerlab.IX_BIOSPECIMEN WHERE GRADE IS NOT NULL
AND grade NOT IN (SELECT NAME FROM lims.biospecimen_grade);

INSERT INTO `lims`.`biospecimen_storage` (NAME) 
SELECT DISTINCT STORED_IN FROM wagerlab.IX_BIOSPECIMEN WHERE STORED_IN IS NOT NULL
AND STORED_IN NOT IN (SELECT NAME FROM lims.biospecimen_storage);

INSERT INTO lims.unit (NAME)
SELECT DISTINCT unit
FROM wagerlab.IX_BIO_TRANSACTIONS
WHERE studykey = 274
AND DELETED =0
AND unit NOT IN (SELECT NAME FROM lims.unit);

-- Insert biospecimens
-- Require all biocollections (encounter) to match accordingly
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
`SAMPLESUBTYPE`,
`DEPTH`,
`BIOSPECIMEN_SPECIES_ID`,
`QTY_COLLECTED`,
`QTY_REMOVED`,
`COMMENTS`,
`QUANTITY`,
`TREATMENT_TYPE_ID`,
`BARCODED`,
`UNIT_ID`,
`PURITY`,
`BIOSPECIMEN_PROTOCOL_ID`,
`BIOSPECIMEN_GRADE_ID`,
`BIOSPECIMEN_STORAGE_ID`,
`CONCENTRATION`
)
SELECT 
    `b`.`BIOSPECIMENID` as `biospecimen_uid`,
    `lss`.`STUDY_ID`,
    `lss`.`ID` as `link_subject_study_id`,
    (SELECT min(id) FROM lims.bio_sampletype WHERE (sampletype, samplesubtype) = (b.sampletype, b.samplesubtype)) AS `sampletype_id`,
    (SELECT min(id) FROM lims.biocollection bc WHERE (bc.link_subject_study_id, bc.study_id, bc.name) = (lss.id, lss.study_id, b.encounter)) AS `biocollection_id`,
    `b`.`PARENTID`,
    `b`.`BIOSPECIMENKEY` as `old_id`,
    `b`.`PARENTKEY` as `oldparent_id`,
    `b`.`DELETED`,
    `b`.`TIMESTAMP`,
    `b`.`BIOSPECIMENID` as `otherid`,
    `b`.`DATEEXTRACTED` as processed_date,
    `b`.`EXTRACTED_TIME` as processed_time,
    `b`.`SAMPLEDATE` as sample_date,
    `b`.`SAMPLE_TIME` as sample_time,
    `b`.`SAMPLETYPE` as sampletype,
    `b`.`SAMPLESUBTYPE` as samplesubtype,
    `b`.`DEPTH`,
    1 as `BIOSPECIMEN_SPECIES_ID`,
    `b`.`QTY_COLLECTED`,
    `b`.`QTY_REMOVED`,
    `b`.`COMMENTS`,
    (`b`.`QTY_COLLECTED` + (IF(`b`.`QTY_REMOVED` IS NULL, 0, `b`.`QTY_REMOVED`))) as `quantity`,
    IFNULL((SELECT id FROM lims.treatment_type tt WHERE UPPER(tt.name) = UPPER(b.TREATMENT)),1) as `treatment_type_id`,
    1 as `barcoded`,
    IFNULL((SELECT id FROM lims.unit WHERE name = b.UNITS), 0) as `UNIT_ID`,
    `b`.`PURITY`,
    (SELECT max(id) FROM lims.biospecimen_protocol WHERE name = b.PROTOCOL) as `BIOSPECIMEN_PROTOCOL_ID`,
    (SELECT max(id) FROM lims.biospecimen_grade WHERE name = b.GRADE) as `BIOSPECIMEN_GRADE_ID`,
    (SELECT max(id) FROM lims.biospecimen_storage WHERE name = b.STORED_IN) as `BIOSPECIMEN_STORAGE_ID`,
    b.DNACONC as `CONCENTRATION`
FROM
    wagerlab.`IX_BIOSPECIMEN` `b`,
    zeus.SUBJECT s,
    zeus.ZE_SUBSTUDY ss,
    `study`.`link_subject_study` `lss`
WHERE
    `b`.`patientkey` = s.SUBJECTKEY
AND s.subjectid = `lss`.`subject_uid`
AND `lss`.study_id = `b`.substudykey
AND `b`.substudykey = ss.substudykey
AND ss.studykey = 274
AND `b`.`DELETED` = 0;

-- Update parent/child mapping
UPDATE lims.biospecimen b
INNER JOIN
(
SELECT b.id, b.oldparent_id, b.old_id, 
(SELECT id FROM lims.biospecimen p WHERE p.old_id = b.oldparent_id) as parent_id, 
(SELECT biospecimen_uid FROM lims.biospecimen p WHERE p.old_id = b.oldparent_id) as parentid
FROM lims.biospecimen b
WHERE oldparent_id IS NOT NULL
AND oldparent_id > -1
ORDER BY oldparent_id
) p ON b.id = p.id
SET b.parent_id = p.parent_id, b.parentid = p.parentid;

-- Insert bio_transactions
INSERT INTO `lims`.`bio_transaction`
(`BIOSPECIMEN_ID`,
`TRANSACTION_DATE`,
`QUANTITY`,
`RECORDER`,
`REASON`)
SELECT b.id, bt.transactiondate, bt.quantity, bt.recorder, bt.reason
FROM wagerlab.IX_BIO_TRANSACTIONS bt, lims.biospecimen b
WHERE bt.biospecimenkey = b.old_id
AND b.study_id IN (SELECT id FROM study.study WHERE parent_id = 274)
AND bt.DELETED = 0;

-- Update status if initial quantity (where possible)
UPDATE lims.bio_transaction SET status_id = 1 WHERE reason like 'Initia%' AND status_id IS NULL;

-- SITES
INSERT INTO `lims`.`inv_site`
(
`DELETED`,
`TIMESTAMP`,
`CONTACT`,
`ADDRESS`,
`NAME`,
`PHONE`)
SELECT `DELETED`,
`TIMESTAMP`,
`CONTACT`,
`ADDRESS`,
`NAME`,
`PHONE` 
FROM wagerlab.IX_INV_SITE
WHERE ldap_group = 'SJOG';

INSERT INTO lims.study_inv_site (study_id, inv_site_id)
SELECT id, (SELECT id FROM lims.inv_site WHERE name = 'SJOG')
FROM study.study
WHERE parent_id = 274;

-- FREEZERS
INSERT INTO `lims`.`inv_freezer`
(
`DELETED`,
`TIMESTAMP`,
`LOCATION`,
`STATUS`,
`SITE_ID`,
`CAPACITY`,
`LASTSERVICENOTE`,
`NAME`,
`AVAILABLE`,
`DECOMMISSIONDATE`,
`COMMISSIONDATE`,
`LASTSERVICEDATE`,
`DESCRIPTION`)
SELECT t.DELETED, t.TIMESTAMP, t.LOCATION, t.STATUS, lims_site.ID, t.CAPACITY, t.LASTSERVICENOTE, t.NAME, 
            t.AVAILABLE, t.DECOMMISSIONDATE, t.COMMISSIONDATE, t.LASTSERVICEDATE,  t.DESCRIPTION
FROM wagerlab.IX_INV_TANK t, wagerlab.IX_INV_SITE s, lims.inv_site lims_site
WHERE t.SITEKEY = s.SITEKEY
AND s.NAME = lims_site.NAME
AND s.NAME = 'SJOG'
AND t.TANKKEY IN (222, 223, 224, 225);

-- RACKS
INSERT INTO `lims`.`inv_rack`
(
`FREEZER_ID`,
`DELETED`,
`TIMESTAMP`,
`NAME`,
`AVAILABLE`,
`DESCRIPTION`,
`CAPACITY`,
`OLD_ID`)
SELECT f.ID, b.DELETED, b.TIMESTAMP, b.NAME, b.AVAILABLE, b.DESCRIPTION, b.CAPACITY, b.boxkey
FROM wagerlab.IX_INV_BOX b, wagerlab.IX_INV_TANK t, lims.inv_freezer f
WHERE t.TANKKEY = b.TANKKEY
AND t.NAME = f.NAME
AND t.TANKKEY IN (222, 223, 224, 225);

-- BOXES
INSERT INTO `lims`.`inv_box`
(`DELETED`,
`TIMESTAMP`,
`NAME`,
`NOOFCOL`,
`CAPACITY`,
`RACK_ID`,
`AVAILABLE`,
`NOOFROW`,
`COLNOTYPE_ID`,
`ROWNOTYPE_ID`,
`OLD_ID`)
SELECT 
    `t`.`DELETED`,
    `t`.`TIMESTAMP`,
    `t`.`NAME`,
    `t`.`NOOFCOL`,
    `t`.`CAPACITY`,
    (SELECT id FROM `lims`.`inv_rack` WHERE OLD_ID = b.BOXKEY) as `RACK_ID`,
    `t`.`AVAILABLE`,
    `t`.`NOOFROW`,
    (SELECT 
        `crt`.`ID`
    FROM
        `lims`.`inv_col_row_type` `crt`
    WHERE
        `NAME` = `t`.`COLNOTYPE`) as `COLNOTYPE_ID`,
    (SELECT 
        `crt`.`ID`
    FROM
        `lims`.`inv_col_row_type` `crt`
    WHERE
        `NAME` = `t`.`ROWNOTYPE`) as `ROWNOTYPE_ID`,
`t`.`TRAYKEY` as OLD_ID
FROM
    wagerlab.`IX_INV_TRAY` t, 
    wagerlab.`IX_INV_BOX` b,
    wagerlab.IX_INV_TANK tank, 
    lims.inv_freezer f
WHERE `t`.`BOXKEY` = `b`.`BOXKEY`
AND tank.TANKKEY = b.TANKKEY
AND tank.NAME = f.NAME
AND tank.TANKKEY IN (222, 223, 224, 225);

-- Insert a fake biospecimen for cell merging
INSERT INTO `lims`.`biospecimen`
(`ID`,
`BIOSPECIMEN_UID`,
`STUDY_ID`,
`LINK_SUBJECT_STUDY_ID`,
`SAMPLETYPE_ID`,
`BIOCOLLECTION_ID`,
`OLD_ID`,
`TREATMENT_TYPE_ID`,
`DELETED`)
SELECT '-1' AS ID, 
'FAKE_BIOSPECIMEN' AS BIOSPECIMEN_UID,
(SELECT id FROM study.study WHERE name = 'WARTN') AS STUDY_ID, 
(SELECT min(id) FROM study.link_subject_study WHERE study_id =(SELECT id FROM study.study WHERE name = 'WARTN')) AS LINK_SUBJECT_STUDY, 
'0' AS SAMPLETYPE_ID, 
(SELECT min(id) FROM lims.biocollection WHERE study_id IN (SELECT id FROM study.study WHERE parent_id =274)) AS BIOCOLLECTION_ID, 
'-1' AS OLD_ID, 
'1' AS TREATMENT_TYPE_ID, 
'-1' AS DELETED
FROM dual;

-- CELLS
INSERT INTO `lims`.`inv_cell`
(`BOX_ID`,
`DELETED`,
`TIMESTAMP`,
`ROWNO`,
`COLNO`,
`BIOSPECIMEN_ID`,
`BIOSPECIMENKEY`,
`STATUS`)
SELECT 
    b.ID as BOX_ID,
    `c`.`DELETED`,
    `c`.`TIMESTAMP`,
    `c`.`COLNO`,
    `c`.`ROWNO`,
bio.id as biospecimen_id,
`c`.`BIOSPECIMENKEY`,
    (CASE
        WHEN `c`.`BIOSPECIMENKEY` > -1 THEN 2
        ELSE 1
    END) as `STATUS`
FROM
    wagerlab.IX_INV_CELL `c`,
    wagerlab.IX_INV_TRAY `t`,
	lims.inv_box b,
	lims.biospecimen bio
WHERE
    `c`.`TRAYKEY` = `t`.`TRAYKEY`
AND b.OLD_ID = `t`.`TRAYKEY`
AND bio.OLD_ID = c.biospecimenkey
AND bio.study_id IN (SELECT id FROM study.study WHERE parent_id = 274);

-- Set -1 biospecimen_id to nulls
UPDATE `lims`.`inv_cell` SET biospecimen_id = null WHERE biospecimen_id = -1;
-- remove fake biospecimen
DELETE FROM lims.biospecimen WHERE id = -1;

-- Allow large byte value for GROUP_CONCAT
SET SESSION group_concat_max_len = 30000;

-- Insert HOSPITAL and REF_DOCTOR as custom biocollection fields
INSERT INTO `study`.`custom_field`
(`NAME`,
`DESCRIPTION`,
`FIELD_TYPE_ID`,
`STUDY_ID`,
`ARK_FUNCTION_ID`,
`UNIT_TYPE_ID`,
`MIN_VALUE`,
`MAX_VALUE`,
`ENCODED_VALUES`,
`MISSING_VALUE`,
`HAS_DATA`,
`CUSTOM_FIELD_LABEL`)
SELECT 'SURGEON' AS NAME, 'NOTE: Encoded values mapped from sortorder in old data' as DESCRIPTION, 
(SELECT ID FROM study.field_type WHERE name = 'CHARACTER') AS FIELD_TYPE,
274 AS STUDY_ID, (SELECT ID FROM study.ark_function WHERE name = 'LIMS_COLLECTION') AS ARK_FUNCTION_ID, NULL AS UNITS_TYPE_ID, NULL MIN_VALUE, NULL MAX_VALUE,
(SELECT (CONCAT(GROUP_CONCAT(CONCAT(FLOOR(SORTORDER),'=',TRIM(VALUE)) ORDER BY SORTORDER SEPARATOR ';'),';'))) AS ENCODED_VALUES,
NULL AS MISSING_VALUE,
0 AS HAS_DATA,
'Surgeon' AS CUSTOM_FIELD_LABEL
FROM wagerlab.IX_LISTOFVALUES 
WHERE TYPE ='REF_DOCTOR' AND DELETED =0 AND STUDYKEY IN (0, 274) AND VALUE IS NOT NULL;

INSERT INTO `study`.`custom_field`
(`NAME`,
`DESCRIPTION`,
`FIELD_TYPE_ID`,
`STUDY_ID`,
`ARK_FUNCTION_ID`,
`UNIT_TYPE_ID`,
`MIN_VALUE`,
`MAX_VALUE`,
`ENCODED_VALUES`,
`MISSING_VALUE`,
`HAS_DATA`,
`CUSTOM_FIELD_LABEL`)
SELECT 'HOSPITAL' AS NAME, 'NOTE: Encoded values mapped from sortorder in old data' as DESCRIPTION, 
(SELECT ID FROM study.field_type WHERE name = 'CHARACTER') AS FIELD_TYPE,
274 AS STUDY_ID, (SELECT ID FROM study.ark_function WHERE name = 'LIMS_COLLECTION') AS ARK_FUNCTION_ID, NULL AS UNITS_TYPE_ID, NULL MIN_VALUE, NULL MAX_VALUE,
(SELECT (CONCAT(GROUP_CONCAT(CONCAT(FLOOR(SORTORDER),'=',TRIM(VALUE)) ORDER BY SORTORDER SEPARATOR ';'),';'))) AS ENCODED_VALUES,
NULL AS MISSING_VALUE,
0 AS HAS_DATA,
'Hospital' AS CUSTOM_FIELD_LABEL
FROM wagerlab.IX_LISTOFVALUES 
WHERE TYPE ='HOSPITAL' AND DELETED =0 AND STUDYKEY IN (0, 274) AND VALUE IS NOT NULL;

-- Insert Custom field display (BioCollection)
INSERT INTO `study`.`custom_field_display`
(`CUSTOM_FIELD_ID`,
`CUSTOM_FIELD_GROUP_ID`,
`SEQUENCE`,
`REQUIRED`,
`REQUIRED_MESSAGE`)
SELECT ID, NULL, ID, 0 AS REQUIRED, NULL
FROM study.custom_field
WHERE study_id = 274
AND ark_function_id = (SELECT ID FROM study.ark_function WHERE name = 'LIMS_COLLECTION')
AND name IN ('HOSPITAL', 'SURGEON');

-- Insert BioCollection Custom fields
INSERT INTO `study`.`custom_field`
(`NAME`,
`DESCRIPTION`,
`FIELD_TYPE_ID`,
`STUDY_ID`,
`ARK_FUNCTION_ID`,
`UNIT_TYPE_ID`,
`MIN_VALUE`,
`MAX_VALUE`,
`ENCODED_VALUES`,
`MISSING_VALUE`,
`HAS_DATA`,
`CUSTOM_FIELD_LABEL`)
SELECT bf.COLUMNNAME AS NAME, IF(bf.LOVTYPE IS NOT NULL, 'NOTE: Encoded values mapped from sortorder in old data', null) as DESCRIPTION, 
(SELECT ID FROM study.field_type WHERE name = IF(bft.TYPENAME = 'string', 'CHARACTER', IF(bft.TYPENAME = 'number', 'NUMBER', 'DATE'))) AS FIELD_TYPE,
274 AS STUDY_ID, (SELECT ID FROM study.ark_function WHERE name = 'LIMS_COLLECTION') AS ARK_FUNCTION_ID, NULL AS UNITS_TYPE_ID, NULL MIN_VALUE, NULL MAX_VALUE,
(
SELECT CONCAT(GROUP_CONCAT(CONCAT(FLOOR(SORTORDER),'=',TRIM(VALUE)) ORDER BY SORTORDER SEPARATOR ';'),';') AS ENCODED_VALUES
FROM wagerlab.IX_LISTOFVALUES 
WHERE TYPE = bf.LOVTYPE
GROUP BY TYPE
) AS ENCODED_VALUES,
NULL AS MISSING_VALUE,
0 AS HAS_DATA,
bf.FIELDNAME AS CUSTOM_FIELD_LABEL
FROM wagerlab.IX_BIODATA_FIELD bf, wagerlab.IX_BIODATA_TYPES bft, wagerlab.IX_BIODATA_FIELD_GROUP bfg, wagerlab.IX_BIODATA_GROUP bg
WHERE bfg.GROUPKEY = bg.GROUPKEY
AND bfg.FIELDKEY = bf.FIELDKEY
AND bf.DOMAIN = bg.DOMAIN
AND bf.TYPEKEY = bft.TYPEKEY
AND bg.GROUP_NAME like 'WARTN%'
AND bg.DOMAIN = 'ADMISSIONS'
ORDER BY bfg.POSITION;

-- Insert Custom field display (BioCollection)
INSERT INTO `study`.`custom_field_display`
(`CUSTOM_FIELD_ID`,
`CUSTOM_FIELD_GROUP_ID`,
`SEQUENCE`,
`REQUIRED`,
`REQUIRED_MESSAGE`)
SELECT ID, NULL, ID, 0 AS REQUIRED, NULL
FROM study.custom_field
WHERE study_id = 274
AND ark_function_id = (SELECT ID FROM study.ark_function WHERE name = 'LIMS_COLLECTION');

-- HOSPITAL and SURGEON data from IX_ADMISSIONS
INSERT INTO `lims`.`biocollection_custom_field_data`
(`BIO_COLLECTION_ID`,
`CUSTOM_FIELD_DISPLAY_ID`,
`TEXT_DATA_VALUE`,
`DATE_DATA_VALUE`,
`NUMBER_DATA_VALUE`,
`ERROR_DATA_VALUE`)
SELECT bc.id AS BIO_COLLECTION_ID, cfd.id AS CUSTOM_FIELD_DISPLAY_ID, SUBSTRING_INDEX(TRIM(TRAILING  SUBSTRING(cf.encoded_values, INSTR(cf.ENCODED_VALUES, concat('=', bc.HOSPITAL, ';'))) FROM cf.ENCODED_VALUES), ';', -1) AS TEXT_DATA_VALUE, NULL AS `DATE_DATA_VALUE`, NULL AS`NUMBER_DATA_VALUE`, NULL AS `ERROR_DATA_VALUE`
FROM study.custom_field cf, study.custom_field_display cfd, lims.biocollection bc
WHERE cf.id = cfd.custom_field_id
AND cf.NAME = 'HOSPITAL'
AND cf.study_id IN (SELECT id FROM study.study WHERE parent_id = 274)
AND bc.study_id IN (SELECT id FROM study.study WHERE parent_id = 274)
AND bc.HOSPITAL IS NOT NULL;

INSERT INTO `lims`.`biocollection_custom_field_data`
(`BIO_COLLECTION_ID`,
`CUSTOM_FIELD_DISPLAY_ID`,
`TEXT_DATA_VALUE`,
`DATE_DATA_VALUE`,
`NUMBER_DATA_VALUE`,
`ERROR_DATA_VALUE`)
SELECT bc.id AS BIO_COLLECTION_ID, cfd.id AS CUSTOM_FIELD_DISPLAY_ID, SUBSTRING_INDEX(TRIM(TRAILING  SUBSTRING(cf.encoded_values, INSTR(cf.ENCODED_VALUES, concat('=', bc.REF_DOCTOR, ';'))) FROM cf.ENCODED_VALUES), ';', -1) AS TEXT_DATA_VALUE, NULL AS `DATE_DATA_VALUE`, NULL AS`NUMBER_DATA_VALUE`, NULL AS `ERROR_DATA_VALUE`
FROM study.custom_field cf, study.custom_field_display cfd, lims.biocollection bc
WHERE cf.id = cfd.custom_field_id
AND cf.NAME = 'SURGEON'
AND cf.study_id IN (SELECT id FROM study.study WHERE parent_id = 274)
AND bc.study_id IN (SELECT id FROM study.study WHERE parent_id = 274)
AND bc.REF_DOCTOR IS NOT NULL;

-- Normal text/character data
INSERT INTO `lims`.`biocollection_custom_field_data`
(`BIO_COLLECTION_ID`,
`CUSTOM_FIELD_DISPLAY_ID`,
`TEXT_DATA_VALUE`,
`DATE_DATA_VALUE`,
`NUMBER_DATA_VALUE`,
`ERROR_DATA_VALUE`)
SELECT bc.id AS BIO_COLLECTION_ID, cfd.id AS CUSTOM_FIELD_DISPLAY_ID, bd.STRING_VALUE AS TEXT_DATA_VALUE, NULL AS `DATE_DATA_VALUE`, NULL AS`NUMBER_DATA_VALUE`, NULL AS `ERROR_DATA_VALUE`
FROM wagerlab.IX_BIODATA bd, wagerlab.IX_BIODATA_FIELD bf, wagerlab.IX_BIODATA_TYPES bft, wagerlab.IX_BIODATA_FIELD_GROUP bfg, wagerlab.IX_BIODATA_GROUP bg, wagerlab.IX_ADMISSIONS adm,study.custom_field cf, study.custom_field_display cfd,
lims.biocollection bc
WHERE bfg.GROUPKEY = bg.GROUPKEY
AND bfg.FIELDKEY = bf.FIELDKEY
AND bf.DOMAIN = bg.DOMAIN
AND bf.TYPEKEY = bft.TYPEKEY
AND bg.GROUP_NAME like 'WARTN%'
AND bg.DOMAIN = 'ADMISSIONS'
AND bd.FIELDKEY = bf.FIELDKEY
AND bd.DOMAINKEY = adm.ADMISSIONKEY
AND adm.DELETED = 0
AND cf.study_id = 274
AND ark_function_id = (SELECT ID FROM study.ark_function WHERE name = 'LIMS_COLLECTION')
AND cf.id = cfd.custom_field_id
AND cf.NAME = bf.COLUMNNAME
AND bc.NAME = adm.ADMISSIONID
AND bc.STUDY_ID = adm.COLLECTIONGROUPKEY
AND bf.LOVTYPE IS NULL
AND STRING_VALUE IS NOT NULL;

-- Drop-down data as migrated into encoded values
INSERT INTO `lims`.`biocollection_custom_field_data`
(`BIO_COLLECTION_ID`,
`CUSTOM_FIELD_DISPLAY_ID`,
`TEXT_DATA_VALUE`,
`DATE_DATA_VALUE`,
`NUMBER_DATA_VALUE`,
`ERROR_DATA_VALUE`)
SELECT bc.id AS BIO_COLLECTION_ID, cfd.id AS CUSTOM_FIELD_DISPLAY_ID, SUBSTRING_INDEX(TRIM(TRAILING  SUBSTRING(cf.encoded_values, INSTR(cf.ENCODED_VALUES, concat('=', bd.STRING_VALUE, ';'))) FROM cf.ENCODED_VALUES), ';', -1) AS TEXT_DATA_VALUE, NULL AS `DATE_DATA_VALUE`, NULL AS`NUMBER_DATA_VALUE`, NULL AS `ERROR_DATA_VALUE`
FROM wagerlab.IX_BIODATA bd, wagerlab.IX_BIODATA_FIELD bf, wagerlab.IX_BIODATA_TYPES bft, wagerlab.IX_BIODATA_FIELD_GROUP bfg, wagerlab.IX_BIODATA_GROUP bg, wagerlab.IX_ADMISSIONS adm,study.custom_field cf, study.custom_field_display cfd,
lims.biocollection bc
WHERE bfg.GROUPKEY = bg.GROUPKEY
AND bfg.FIELDKEY = bf.FIELDKEY
AND bf.DOMAIN = bg.DOMAIN
AND bf.TYPEKEY = bft.TYPEKEY
AND bg.GROUP_NAME like 'WARTN%'
AND bg.DOMAIN = 'ADMISSIONS'
AND bd.FIELDKEY = bf.FIELDKEY
AND bd.DOMAINKEY = adm.ADMISSIONKEY
AND adm.DELETED = 0
AND cf.study_id = 274
AND ark_function_id = (SELECT ID FROM study.ark_function WHERE name = 'LIMS_COLLECTION')
AND cf.id = cfd.custom_field_id
AND cf.NAME = bf.COLUMNNAME
AND bc.NAME = adm.ADMISSIONID
AND bc.STUDY_ID = adm.COLLECTIONGROUPKEY
AND bf.LOVTYPE IS NOT NULL
AND STRING_VALUE IS NOT NULL;
 
-- Dates
INSERT INTO `lims`.`biocollection_custom_field_data`
(`BIO_COLLECTION_ID`,
`CUSTOM_FIELD_DISPLAY_ID`,
`TEXT_DATA_VALUE`,
`DATE_DATA_VALUE`,
`NUMBER_DATA_VALUE`,
`ERROR_DATA_VALUE`)
SELECT bc.id AS BIO_COLLECTION_ID, cfd.id AS CUSTOM_FIELD_DISPLAY_ID, NULL AS TEXT_DATA_VALUE, bd.DATE_VALUE AS `DATE_DATA_VALUE`, NULL AS`NUMBER_DATA_VALUE`, NULL AS `ERROR_DATA_VALUE`
FROM wagerlab.IX_BIODATA bd, wagerlab.IX_BIODATA_FIELD bf, wagerlab.IX_BIODATA_TYPES bft, wagerlab.IX_BIODATA_FIELD_GROUP bfg, wagerlab.IX_BIODATA_GROUP bg, wagerlab.IX_ADMISSIONS adm,study.custom_field cf, study.custom_field_display cfd,
lims.biocollection bc
WHERE bfg.GROUPKEY = bg.GROUPKEY
AND bfg.FIELDKEY = bf.FIELDKEY
AND bf.DOMAIN = bg.DOMAIN
AND bf.TYPEKEY = bft.TYPEKEY
AND bg.GROUP_NAME like 'WARTN%'
AND bg.DOMAIN = 'ADMISSIONS'
AND bd.FIELDKEY = bf.FIELDKEY
AND bd.DOMAINKEY = adm.ADMISSIONKEY
AND adm.DELETED = 0
AND cf.study_id = 274
AND ark_function_id = (SELECT ID FROM study.ark_function WHERE name = 'LIMS_COLLECTION')
AND cf.id = cfd.custom_field_id
AND cf.NAME = bf.COLUMNNAME
AND bc.NAME = adm.ADMISSIONID
AND bc.STUDY_ID = adm.COLLECTIONGROUPKEY
AND DATE_VALUE IS NOT NULL;

-- Insert Biospecimen Custom fields
INSERT INTO `study`.`custom_field`
(`NAME`,
`DESCRIPTION`,
`FIELD_TYPE_ID`,
`STUDY_ID`,
`ARK_FUNCTION_ID`,
`UNIT_TYPE_ID`,
`MIN_VALUE`,
`MAX_VALUE`,
`ENCODED_VALUES`,
`MISSING_VALUE`,
`HAS_DATA`,
`CUSTOM_FIELD_LABEL`)
SELECT bf.COLUMNNAME AS NAME, IF(bf.LOVTYPE IS NOT NULL, 'NOTE: Encoded values mapped from sortorder in old data', null) as DESCRIPTION, 
(SELECT ID FROM study.field_type WHERE name = IF(bft.TYPENAME = 'string', 'CHARACTER', IF(bft.TYPENAME = 'number', 'NUMBER', 'DATE'))) AS FIELD_TYPE,
274 AS STUDY_ID, (SELECT ID FROM study.ark_function WHERE name = 'BIOSPECIMEN') AS ARK_FUNCTION_ID, NULL AS UNIT_TYPE_ID, NULL MIN_VALUE, NULL MAX_VALUE,
(
SELECT CONCAT(GROUP_CONCAT(CONCAT(FLOOR(SORTORDER),'=',TRIM(VALUE)) ORDER BY SORTORDER SEPARATOR ';'),';') AS ENCODED_VALUES
FROM wagerlab.IX_LISTOFVALUES 
WHERE TYPE = bf.LOVTYPE
GROUP BY TYPE
) AS ENCODED_VALUES,
NULL AS MISSING_VALUE,
0 AS HAS_DATA,
bf.FIELDNAME AS CUSTOM_FIELD_LABEL
FROM wagerlab.IX_BIODATA_FIELD bf, wagerlab.IX_BIODATA_TYPES bft, wagerlab.IX_BIODATA_FIELD_GROUP bfg, wagerlab.IX_BIODATA_GROUP bg
WHERE bfg.GROUPKEY = bg.GROUPKEY
AND bfg.FIELDKEY = bf.FIELDKEY
AND bf.DOMAIN = bg.DOMAIN
AND bf.TYPEKEY = bft.TYPEKEY
AND bg.GROUP_NAME like 'WARTN%'
AND bg.DOMAIN = 'BIOSPECIMEN'
ORDER BY bfg.POSITION;

-- Insert Custom field display (Biospecimen)
INSERT INTO `study`.`custom_field_display`
(`CUSTOM_FIELD_ID`,
`CUSTOM_FIELD_GROUP_ID`,
`SEQUENCE`,
`REQUIRED`,
`REQUIRED_MESSAGE`)
SELECT ID, NULL, ID, 0 AS REQUIRED, NULL
FROM study.custom_field
WHERE study_id = 274
AND ark_function_id = (SELECT ID FROM study.ark_function WHERE name = 'BIOSPECIMEN');

-- Characters
INSERT INTO `lims`.`biospecimen_custom_field_data`
(`BIOSPECIMEN_ID`,
`CUSTOM_FIELD_DISPLAY_ID`,
`TEXT_DATA_VALUE`,
`DATE_DATA_VALUE`,
`NUMBER_DATA_VALUE`,
`ERROR_DATA_VALUE`)
SELECT bs.id AS BIOSPECIMEN_ID, cfd.id AS CUSTOM_FIELD_DISPLAY_ID, SUBSTRING_INDEX(TRIM(TRAILING  SUBSTRING(cf.encoded_values, INSTR(cf.ENCODED_VALUES, concat('=', bd.STRING_VALUE, ';'))) FROM cf.ENCODED_VALUES), ';', -1) AS TEXT_DATA_VALUE, NULL AS `DATE_DATA_VALUE`, NULL AS`NUMBER_DATA_VALUE`, NULL AS `ERROR_DATA_VALUE`
FROM wagerlab.IX_BIODATA bd, wagerlab.IX_BIODATA_FIELD bf, wagerlab.IX_BIODATA_TYPES bft, wagerlab.IX_BIODATA_FIELD_GROUP bfg, wagerlab.IX_BIODATA_GROUP bg, wagerlab.IX_BIOSPECIMEN bio,study.custom_field cf, study.custom_field_display cfd,
lims.biospecimen bs
WHERE bfg.GROUPKEY = bg.GROUPKEY
AND bfg.FIELDKEY = bf.FIELDKEY
AND bf.DOMAIN = bg.DOMAIN
AND bf.TYPEKEY = bft.TYPEKEY
AND bg.GROUP_NAME like 'WARTN%'
AND bg.DOMAIN = 'BIOSPECIMEN'
AND bd.FIELDKEY = bf.FIELDKEY
AND bd.DOMAINKEY = bio.BIOSPECIMENKEY
AND bio.DELETED = 0
AND cf.study_id = 274
AND ark_function_id = (SELECT ID FROM study.ark_function WHERE name = 'BIOSPECIMEN')
AND cf.id = cfd.custom_field_id
AND cf.NAME = bf.COLUMNNAME
AND STRING_VALUE IS NOT NULL
AND bf.LOVTYPE IS NOT NULL
AND bs.OLD_ID = bio.BIOSPECIMENKEY;
 
-- Dates
INSERT INTO `lims`.`biospecimen_custom_field_data`
(`BIOSPECIMEN_ID`,
`CUSTOM_FIELD_DISPLAY_ID`,
`TEXT_DATA_VALUE`,
`DATE_DATA_VALUE`,
`NUMBER_DATA_VALUE`,
`ERROR_DATA_VALUE`)
SELECT bs.id AS BIOSPECIMEN_ID, cfd.id AS CUSTOM_FIELD_DISPLAY_ID, NULL AS TEXT_DATA_VALUE, bd.DATE_VALUE AS `DATE_DATA_VALUE`, NULL AS`NUMBER_DATA_VALUE`, NULL AS `ERROR_DATA_VALUE`
FROM wagerlab.IX_BIODATA bd, wagerlab.IX_BIODATA_FIELD bf, wagerlab.IX_BIODATA_TYPES bft, wagerlab.IX_BIODATA_FIELD_GROUP bfg, wagerlab.IX_BIODATA_GROUP bg, wagerlab.IX_BIOSPECIMEN bio,study.custom_field cf, study.custom_field_display cfd,
lims.biospecimen bs
WHERE bfg.GROUPKEY = bg.GROUPKEY
AND bfg.FIELDKEY = bf.FIELDKEY
AND bf.DOMAIN = bg.DOMAIN
AND bf.TYPEKEY = bft.TYPEKEY
AND bg.GROUP_NAME like 'WARTN%'
AND bg.DOMAIN = 'BIOSPECIMEN'
AND bd.FIELDKEY = bf.FIELDKEY
AND bd.DOMAINKEY = bio.BIOSPECIMENKEY
AND bio.DELETED = 0
AND cf.study_id = 274
AND ark_function_id = (SELECT ID FROM study.ark_function WHERE name = 'BIOSPECIMEN')
AND cf.id = cfd.custom_field_id
AND cf.NAME = bf.COLUMNNAME
AND DATE_VALUE IS NOT NULL
AND bs.OLD_ID = bio.BIOSPECIMENKEY;

-- BioCollection pattern
DELETE FROM `lims`.`biocollectionuid_template` WHERE STUDY_ID = 274;
INSERT INTO `lims`.`biocollectionuid_template`
(`STUDY_ID`,
`BIOCOLLECTIONUID_PREFIX`,
`BIOCOLLECTIONUID_TOKEN_ID`,
`BIOCOLLECTIONUID_PADCHAR_ID`)
VALUES (274, 'TN', NULL, 5);

-- Set base sequence count
DELETE FROM `lims`.`biocollectionuid_sequence` WHERE `STUDY_NAME_ID` = 'WARTN';
INSERT INTO `lims`.`biocollectionuid_sequence`
(`STUDY_NAME_ID`,
`UID_SEQUENCE`,
`INSERT_LOCK`)
VALUES
(
'WARTN',
(SELECT MAX(TRIM('TN' FROM name)) + 1
FROM lims.biocollection
WHERE study_id IN (SELECT id FROM study.study WHERE parent_id = 274)
AND name like 'TN%'),
0
);

-- Biospecimen pattern
DELETE FROM `lims`.`biospecimenuid_template` WHERE `STUDY_ID` = 274;
INSERT INTO `lims`.`biospecimenuid_template`
(`STUDY_ID`,
`BIOSPECIMENUID_PREFIX`,
`BIOSPECIMENUID_TOKEN_ID`,
`BIOSPECIMENUID_PADCHAR_ID`)
VALUES (274, 'TN', NULL, 9);

-- Set base sequence count
DELETE FROM `lims`.`biospecimenuid_sequence` WHERE `STUDY_NAME_ID` = 'WARTN';
INSERT INTO `lims`.`biospecimenuid_sequence`
(`STUDY_NAME_ID`,
`UID_SEQUENCE`,
`INSERT_LOCK`)
VALUES
(
'WARTN',
(SELECT MAX(ID) FROM lims.biospecimen WHERE study_id IN (SELECT ID FROM study.study WHERE parent_id = 274)),
0
);

-- Assign modules to all studies
DELETE FROM `study`.`link_study_arkmodule` WHERE study_id IN (SELECT ID FROM study.study WHERE parent_id = 274);
INSERT INTO `study`.`link_study_arkmodule`
(`STUDY_ID`,
`ARK_MODULE_ID`)
SELECT s.id as study_id, a.id as ark_module_id
FROM study.study s, study.ark_module a
WHERE s.parent_id = 274
AND a.name IN ('Study', 'Subject', 'Phenotypic', 'LIMS')
ORDER BY s.id, a.id;

-- SET starting subject increment
UPDATE `study`.`subjectuid_sequence` SET `UID_SEQUENCE`=(SELECT TRIM('0' FROM TRIM(LEADING 'WTN-' FROM (SELECT max(subject_uid) maxid FROM link_subject_study WHERE study_id=274)))) WHERE `STUDY_NAME_ID`='WARTN';