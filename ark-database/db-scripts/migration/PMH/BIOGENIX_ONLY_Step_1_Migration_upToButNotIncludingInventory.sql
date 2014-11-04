-- T1D PARAMETERS
-- latest
SET @STUDY_GROUP_NAME = 'PMH';
SET @STUDYKEY = 590;
SET @STUDYNAME= 'PMH';
SET @AUTOGEN_SUBJECT = 1;
SET @AUTOGEN_BIOSPECIMEN = 1;
SET @AUTOGEN_BIOCOLLECTION = 1;
-- before setting each of these params check that this can work...ie; that there is not some weird multiple prefix for a given study.

-- SET @SUBJECT_PADCHAR = 8; -- no of chars to pad out
-- apparently subject prefix comes from wager
-- SET @SUBJECT_PREFIX = ’T1D’;

SET @BIOCOLLECTIONUID_PREFIX = 'PMH';
-- SET @BIOCOLLECTIONUID_TOKEN_ID = 1;
SET @BIOCOLLECTIONUID_TOKEN_DASH = '';
SET @BIOCOLLECTIONUID_PADCHAR_ID = 5;
	
SET @BIOSPECIMENUID_PREFIX = 'PMH';
-- SET @BIOSPECIMENUID_TOKEN_ID = 1;
SET @BIOSPECIMENUID_PADCHAR_ID = 6;




update pmhdiaendo.ix_biospecimen
set samplesubtype = 'DNA',
sampletype = 'Nucleic Acid'
where sampletype='DNA';

update pmhdiaendo.ix_biospecimen
set samplesubtype = 'Blood',
sampletype = 'Whole Blood'
where sampletype='Whole Blood';

update pmhdiaendo.ix_biospecimen
set samplesubtype = 'Blood',
sampletype = 'Whole Blood (EDTA)'
where sampletype='Blood (EDTA)';

update pmhdiaendo.ix_biospecimen
set samplesubtype = 'Blood',
sampletype = 'Serum'
where sampletype='Serum';


-- fix two bad records

UPDATE `pmhdiaendo`.`ix_admissions` SET `ADMISSIONID`='12345' 
WHERE `ADMISSIONKEY`='10567';

UPDATE `pmhdiaendo`.`ix_biospecimen` SET 
`ENCOUNTER`='12345' 
WHERE `ADMISSIONKEY`='10567';

-- this guy's admission was deleted ... i undeleted given he theoretically has samples pointing at this admission/encounter
UPDATE `pmhdiaendo`.`ix_admissions` SET `DELETED`='0' WHERE `ADMISSIONKEY`='12716.000000000000000000000000000000';


-- Insert Study
INSERT INTO study.study(
`ID`,
`NAME`,
`AUTO_GENERATE_SUBJECTUID`,
`STUDY_STATUS_ID`,
`SUBJECTUID_PREFIX`,
`CONTACT_PERSON`,
`CHIEF_INVESTIGATOR`,
`LDAP_GROUP_NAME`,
`AUTO_CONSENT`,
`SUBJECTUID_TOKEN_ID`,
`SUBJECTUID_PADCHAR_ID`,
-- `PARENT_ID`,
`AUTO_GENERATE_BIOSPECIMENUID`,
`AUTO_GENERATE_BIOCOLLECTIONUID`)
SELECT @NEWSTUDYKEY as ID, 
s.study_name AS NAME, 
@AUTOGEN_SUBJECT as AUTO_GENERATE_SUBJECTUID,
1 as STUDY_STATUS_ID,
s.STUDYCODE as SUBJECTUID_PREFIX, 
s.STUDY_OWNER AS `CONTACT_PERSON`,
s.STUDY_OWNER AS `CHIEF_INVESTIGATOR`,
'' AS LDAP_GROUP_NAME,
1 as AUTO_CONSENT,
1 as SUBJECTUID_TOKEN_ID,
@SUBJECT_PADCHAR as SUBJECTUID_PADCHAR_ID,
-- s.studykey as PARENT_ID,
@AUTOGEN_BIOSPECIMEN as AUTO_GENERATE_BIOSPECIMENUID,
@AUTOGEN_BIOCOLLECTION as AUTO_GENERATE_BIOCOLLECTIONUID
FROM pmhdiaendo.IX_STUDY s
WHERE s.study_name=@STUDYNAME
ORDER BY ID;

UPDATE study.study set parent_id = @STUDYKEY where id = @STUDYKEY;

select * from study.study where id = @STUDYKEY;



/***********************  !!!!!!!!!!!!!!!!!!!!
-- IF it should NOT HAVE A SUBJECT_UID PREFIX FIX IT NOW... BECAUSE IT IS GETTING IT FROM ZEUS!!!!!!!!
-- update study.study set subjectuid_prefix = null where id = @STUDYKEY;
-- update study.study set subjectuid_token_id = null where id = @STUDYKEY;
-- and update the sequence if necessary
************************************************/

select * from study.study;

/*-- Add missed titles

select * from study.study where id = @STUDYKEY


INSERT INTO study.title_type (ID, NAME)
SELECT DISTINCT (SELECT MAX(ID)+1 FROM study.title_type), concat( upper(substring(title,1,1)),lower(substring(title,2)) ) FROM zeus.SUBJECT
WHERE TITLE NOT IN (SELECT (NAME) FROM study.title_type);
*/

-- Insert person details
INSERT INTO study.person (OTHER_ID, FIRST_NAME, MIDDLE_NAME, LAST_NAME, DATE_OF_BIRTH, VITAL_STATUS_ID, GENDER_TYPE_ID, TITLE_TYPE_ID, DATE_OF_DEATH, CAUSE_OF_DEATH)
SELECT
  PATIENTKEY as OTHER_ID,
  FIRSTNAME as FIRST_NAME,
  MIDDLENAME as MIDDLE_NAME,
  SURNAME as LAST_NAME,
  DOB as DATE_OF_BIRTH,
  (SELECT id FROM study.vital_status WHERE name = (IF(DODEATH IS NOT NULL, 'Deceased', 'Alive'))) as VITAL_STATUS_ID,
  (SELECT id FROM study.gender_type WHERE UPPER(study.gender_type.NAME) = UPPER(CASE UPPER(SEX) WHEN 'M' THEN 'Male' WHEN 'F' THEN 'Female' ELSE 'Unknown' END)) as GENDER_TYPE_ID,
  (SELECT id FROM study.title_type WHERE UPPER(study.title_type.NAME) = UPPER(IF(TITLE IS NULL, 'Unknown', TITLE))) as TITLE_TYPE_ID,
  DODEATH as DATE_OF_DEATH,
  CAUSEOD as CAUSE_OF_DEATH
FROM  `pmhdiaendo`.`ix_patient`;
-- WHERE studykey=@STUDYKEY;


select l.study_id from pmhdiaendo.ix_patient p, study.person per, study.link_subject_study l
where p.patientkey = per.other_id
and l.person_id = per.id


/* NO PHONES STORED
-- Home phone
INSERT INTO study.phone (area_code, phone_number, person_id, phone_type_id, phone_status_id)
SELECT 
    NULL as area_code,
	sub.home_phone as phone_number,
    `person`.`id`,
    (SELECT id FROM study.phone_type WHERE UPPER(name) = 'HOME') as phone_type,
    1 as `phone_status_id`
FROM `pmhdiaendo`.`IX_STUDY` s, pmhdiaendo.ix_patient sub, study.person
WHERE s.studykey = sub.studykey
AND sub.`SUBJECTKEY` = `person`.`OTHER_ID` 
AND `person`.`OTHER_ID` IS NOT NULL
AND sub.home_phone IS NOT NULL
AND s.studyname=@STUDYNAME;

-- Work phone
INSERT INTO study.phone (area_code, phone_number, person_id, phone_type_id, phone_status_id)
SELECT 
    NULL as area_code, 
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
AND s.studyname=@STUDYNAME;

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
AND s.studyname=@STUDYNAME;
*/
;
-- Address
-- trav may get referencial issues
INSERT INTO study.address
(`ADDRESS_LINE_1`,`STREET_ADDRESS`,`CITY`,`STATE_ID`,`POST_CODE`,`COUNTRY_ID`,`ADDRESS_STATUS_ID`,`ADDRESS_TYPE_ID`,`OTHER_STATE`,`PERSON_ID`,`PREFERRED_MAILING_ADDRESS`)
SELECT 
sub.ADDR_LINE1 AS `ADDRESS_LINE_1`,
null  AS `STREET_ADDRESS`,
sub.ADDR_SUBURB AS `CITY`,
(SELECT id FROM study.state WHERE short_name = sub.ADDR_STATE OR name = sub.ADDR_STATE)`STATE_ID`,
sub.ADDR_POSTCODE `POST_CODE`,
(SELECT id FROM study.country WHERE NAME = sub.ADDR_COUNTRY) `COUNTRY_ID`,
1 AS `ADDRESS_STATUS_ID`,
1 AS `ADDRESS_TYPE_ID`,
null AS `OTHER_STATE`,
`p`.`id`  as `PERSON_ID`,
1 AS `PREFERRED_MAILING_ADDRESS`
FROM `pmhdiaendo`.ix_patient sub, study.person p
WHERE  sub.`PATIENTKEY` = `p`.`OTHER_ID` 
AND `p`.`OTHER_ID` IS NOT NULL
AND (sub.ADDR_LINE1 is not null 
	OR
	sub.ADDR_SUBURB is not null	-- aka city
	OR
	sub.ADDR_POSTCODE is not null
	) 
-- and p.study_id = @STUDYKEY 
and p.id not in (select person_id from study.link_subject_study);

-- Insert subject/consent details into parent study
-- trav assuming wager doesnt have a status or consent status itself
INSERT INTO study.link_subject_study (person_id, study_id, subject_status_id, subject_uid)
SELECT 
    `person`.`id`,
    @STUDYKEY as `study_id`,
    1,
    sub.`PATIENTID` as `subject_uid`-- ,
	-- IFNULL((select min(id) from study.consent_status where UPPER(name) = UPPER(constat.description)),1) as `consent_status_id`
	-- UPPER(constat.description) as throwawayupper-- ,
	-- s-elect min(id) from study.consent_status where UPPER(name) = UPPER(constat.description) as throwawayselect
FROM
study.STUDY s, pmhdiaendo.ix_patient sub, study.person -- , zeus.consent_status constat, zeus.consent_study constudy 
WHERE s.id = sub.studykey
AND sub.`PATIENTKEY` = `person`.`OTHER_ID` 
AND `person`.`OTHER_ID` IS NOT NULL
AND s.name=@STUDYNAME
and person.id not in (select person_id from study.link_subject_study);


select * from study.link_subject_study where study_id  = @STUDYKEY;

select subject_status_id, count(*) from study.link_subject_study where study_id =@STUDYKEY
group by subject_status_id;


/*  SHOULDNT need this
-- Some subjects/sub-studies may have been missed. This adds any missed based on admission sub-study (should only have the one "dodgy" subject)
-- run select as a check first, but otherwise not needed to execute INSERT
INSERT INTO study.link_subject_study (person_id, study_id, subject_status_id, subject_uid, comments)
SELECT 
    p.`id` as `person_id`,
    IF(collectiongroupkey IS NULL, @STUDYKEY, IF(collectiongroupkey = 404, @STUDYKEY, collectiongroupkey)) as `study_id`,
    1 as `subject_status_id`,
    zeus.SUBJECT.`SUBJECTID` as `subject_uid`,
    'Added to study, as collection exists but consent may have been incorrect' as comments
FROM wagerlab.IX_ADMISSIONS adm, study.person p, zeus.SUBJECT
WHERE adm.studykey=@STUDYKEY
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
AND ss.studykey = @STUDYKEY
AND `adm`.`DELETED` = 0
);
*/

select count(*) from lims.biocollection where study_id = @STUDYKEY;

del ete from lims.biocollection where study_id = @STUDYKEY;

-- FYI Raves one less than number of people...investigate if that is ok - it was due to delete ok..
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
    ifnull(`adm`.`admissionid`,'HadNoAdmissionId') as `BIOCOLLECTION_UID`,
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
    pmhdiaendo.`IX_ADMISSIONS` `adm`,
    pmhdiaendo.IX_PATIENT s,
--    zeus.ZE_SUBSTUDY ss,
    `study`.`link_subject_study` `lss`
WHERE
    `adm`.`patientkey` = s.PATIENTKEY
AND s.patientid = `lss`.`subject_uid`
and lss.STUDY_ID = @STUDYKEY
-- AND `lss`.study_id = s.studykey -- `adm`.collectiongroupkey
-- AND `adm`.studykey = s.studykey
-- AND `adm`.collectiongroupkey = ss.substudykey
-- AND ss.studykey = @STUDYKEY
AND `adm`.`DELETED` = 0; -- (107732, 107729, 107730, 107731);

select * from wagerlab.ix_admissions where admissionkey = 107734;

select * from lims.biocollection where study_id = 11 and id > 78500;

select count(*) from lims.biocollection where study_id = @STUDYKEY;



-- Insert biospecimen sampletypes that may not exist
-- TRAV PROD  When you run this in prod...check what it is about to install and if we want them
/* INSERT INTO lims.bio_sampletype (name, sampletype, samplesubtype)
SELECT DISTINCT CONCAT(sampletype, ' / ', samplesubtype), sampletype, samplesubtype FROM wagerlab.IX_BIOSPECIMEN
WHERE (sampletype, samplesubtype) NOT IN (SELECT sampletype, samplesubtype FROM lims.bio_sampletype);
*/
-- TODO: HANDLE FOR STORED_IN / GRADE / - we have none stored in the ark...does that matter?  are we losing data?

/*
-- Any Treatment types not already matched
SELECT tt.* 
FROM lims.treatment_type tt 
WHERE UPPER(tt.name) NOT IN (SELECT DISTINCT UPPER(b.TREATMENT) FROM wagerlab.IX_BIOSPECIMEN b WHERE studykey=@STUDYKEY);
*/

-- Any Units not already matched
/* NOTE: COULD RUN THIS ON ORACLE COPY FIRST
UPDATE wagerlab.IX_BIOSPECIMEN b
INNER JOIN 
(
SELECT bt.biospecimenkey, IFNULL(MIN(bt.unit), 'unit') as units
FROM wagerlab.IX_BIO_TRANSACTIONS bt, wagerlab.IX_BIOSPECIMEN b
WHERE b.studykey=@STUDYKEY
AND bt.biospecimenkey = b.biospecimenkey
GROUP BY bt.biospecimenkey
) bt ON b.biospecimenkey = bt.biospecimenkey
SET b.units = bt.units;
*/

/*
-- Trav : is this not done already?
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
WHERE studykey = @STUDYKEY
AND DELETED =0
AND unit NOT IN (SELECT NAME FROM lims.unit);
*/

SELECT * 
FROM lims.unit a, lims.unit b 
WHERE a.name = b.name 
and a.id <> b.id;

-- change nulls to something meaningful
update pmhdiaendo.ix_biospecimen set samplesubtype = 'wasNull' where samplesubtype is null and sampletype is not null;

update lims.bio_sampletype set samplesubtype = 'wasNull' where samplesubtype is null and sampletype is not null
 limit 1000;


update pmhdiaendo.ix_biospecimen set treatment = 'Tissue Cultured' where treatment = 'Tissue Culture' limit 1000000;


-- then run insert
--  ...
-- then clean up the mess you made
-- this will be run AFTER INSERT update lims.bio_sampletype set subsampletype = null where subsampletype = 'wasNull' and sampletype is not null;


-- update biogenix to match ark sampletypes


-- THESE MAY NOW NEED A TWEAK ON ACTUAL PROD RUN!!!!!!!!!
update pmhdiaendo.ix_biospecimen
set samplesubtype = 'DNA',
sampletype = 'Nucleic Acid'
where sampletype='DNA';

update pmhdiaendo.ix_biospecimen
set samplesubtype = 'Whole Blood',
sampletype = 'Blood'
where sampletype='Whole Blood' -- and samplesubtype='Blood';

update pmhdiaendo.ix_biospecimen
set samplesubtype ='Whole Blood (EDTA)',
sampletype =  'Blood'
where sampletype like'%Blood (EDTA)';

update pmhdiaendo.ix_biospecimen
set samplesubtype = 'Serum',
sampletype = 'Blood'
where sampletype='Serum'
or samplesubtype='Serum';

select * from lims.biocollection where link_subject_study_id = '111387';


-- Insert biospecimens
-- Require all biocollections (encounter) to match accordingly
-- Trav : let's run this slowly with a small sample or something???
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
`CONCENTRATION`,
`BIOSPECIMEN_ANTICOAGULANT_ID`,
`BIOSPECIMEN_QUALITY_ID`,
`BIOSPECIMEN_STATUS_ID`
)
SELECT 
    `b`.`BIOSPECIMENID` as `biospecimen_uid`,
    `lss`.`STUDY_ID`,
    `lss`.`ID` as `link_subject_study_id`,
    -- sampletype,
    -- samplesubtype,
    (SELECT min(id) FROM lims.bio_sampletype WHERE (sampletype, samplesubtype) = (b.sampletype, b.samplesubtype)) AS `sampletype_id`,
    ifnull(
			(SELECT min(id) FROM lims.biocollection bc WHERE (bc.link_subject_study_id, bc.study_id, bc.name) = 
																	(lss.id, lss.study_id, b.encounter)
																	and deleted=0)
                                                                    ,
			(SELECT min(id) FROM lims.biocollection bc WHERE (bc.link_subject_study_id, bc.study_id) = 
																	(lss.id, lss.study_id)
																	and deleted=0)                                                           
                                                                    )AS `biocollection_id`,
    -- b.ENCOUNTER,                                                               
    `b`.`PARENTID`,
    `b`.`BIOSPECIMENKEY` as `old_id`,
    `b`.`PARENTKEY` as `oldparent_id`,
    `b`.`DELETED`,
    `b`.`TIMESTAMP`,
    `b`.`BIOSPECIMENID` as `otherid`,
    `b`.`DATEEXTRACTED` as processed_date, 
	DATE_FORMAT(`b`.`EXTRACTED_TIME`, '%H:%i:%s') as processed_time,  --  `b`.`EXTRACTED_TIME` as processed_time,
    `b`.`SAMPLEDATE` as sample_date,
	DATE_FORMAT(`b`.`SAMPLE_TIME`, '%H:%i:%s') as sample_time, --   `b`.`SAMPLE_TIME` as sample_time,
    `b`.`SAMPLETYPE` as sampletype,
    `b`.`SAMPLESUBTYPE` as samplesubtype,
    `b`.`DEPTH`,
    1 as `BIOSPECIMEN_SPECIES_ID`,
    `b`.`QTY_COLLECTED`,
    `b`.`QTY_REMOVED`,
    `b`.`COMMENTS`,
    (`b`.`QTY_COLLECTED` + (IF(`b`.`QTY_REMOVED` IS NULL, 0, `b`.`QTY_REMOVED`))) as `quantity`,
    -1 as `treatment_type_id`,
    1 as `barcoded`,
    (SELECT min(id) FROM lims.unit WHERE name = 'mL') as `UNIT_ID`,
    `b`.`PURITY`,
    (SELECT max(id) FROM lims.biospecimen_protocol WHERE name = b.PROTOCOL) as `BIOSPECIMEN_PROTOCOL_ID`,
    (SELECT max(id) FROM lims.biospecimen_grade WHERE name = b.GRADE) as `BIOSPECIMEN_GRADE_ID`,
    (SELECT max(id) FROM lims.biospecimen_storage WHERE name = b.STORED_IN) as `BIOSPECIMEN_STORAGE_ID`,
    b.DNACONC as `CONCENTRATION`,
	(SELECT max(id) FROM lims.BIOSPECIMEN_ANTICOAGULANT WHERE name = b.ANTICOAG) as `BIOSPECIMEN_ANTICOAGULANT_ID`,
	1 as `BIOSPECIMEN_QUALITY_ID`,
	1 as `BIOSPECIMEN_STATUS_ID`
FROM
    pmhdiaendo.`IX_BIOSPECIMEN` `b`,
    pmhdiaendo.ix_patient s,
    `study`.`link_subject_study` `lss`
WHERE
    `b`.`patientkey` = s.PATIENTKEY
AND s.patientid = `lss`.`subject_uid`
AND `lss`.study_id = @STUDYKEY -- `b`.studykey
AND s.studykey = @STUDYKEY
AND `b`.`DELETED` = 0;






select sampletype, SAMPLESUBTYPE, count(*)
from pmhdiaendo.ix_biospecimen
where deleted = 0
group by sampletype, SAMPLESUBTYPE;


-- note biogenix pmh let's put original studyid in comments or something

select count(*) from lims.biospecimen where study_id = @STUDYKEY;

select count(*) from wagerlab.ix_biospecimen where studykey = @STUDYKEY
 and deleted  =0 and patientkey in (select subjectkey from zeus.subject where studykey = @STUDYKEY);

-- then clean up the mess you made from the nulls
-- inverse there two -- update wagerlab.ix_biospecimen set samplesubtype = 'wasNull' where samplesubtype is null and sampletype is not null; not much need to inverse this really
			-- update lims.bio_sampletype set samplesubtype = 'wasNull' where samplesubtype is null and sampletype is not null;
update lims.bio_sampletype set samplesubtype = null where samplesubtype = 'wasNull' and sampletype is not null limit 1000000;



-- Update parent/child mapping
UPDATE lims.biospecimen b
        INNER JOIN
    (SELECT 
        b.id,
            b.oldparent_id,
            b.old_id,
            (SELECT 
                    id
                FROM
                    lims.biospecimen p
                WHERE
                    p.old_id = b.oldparent_id and p.study_id = @studykey) as parent_id,
            (SELECT 
                    biospecimen_uid
                FROM
                    lims.biospecimen p
                WHERE
                    p.old_id = b.oldparent_id and p.study_id = @studykey) as parentid
    FROM
        lims.biospecimen b
    WHERE
        oldparent_id IS NOT NULL
            AND oldparent_id > - 1
		and b.study_id = @studykey
    ORDER BY oldparent_id) p ON b.id = p.id 
SET 
    b.parent_id = p.parent_id,
    b.parentid = p.parentid;

-- Insert bio_transactions
-- Trav : these may now need units themselves (just take from biospecimen) - we will just run a fix up script after to make sure the units are the correct references.
INSERT INTO `lims`.`bio_transaction`
(`BIOSPECIMEN_ID`,
`TRANSACTION_DATE`,
`QUANTITY`,
`RECORDER`,
`REASON`)
SELECT   b.id, bt.transactiondate, bt.quantity, bt.recorder, bt.reason  
FROM pmhdiaendo.IX_BIO_TRANSACTIONS bt, lims.biospecimen b
WHERE bt.biospecimenkey = b.old_id
AND b.study_id = @STUDYKEY
AND bt.DELETED = 0;

select * from lims.biospecimen where study_id  = @STUDYKEY;

select * from lims.bio_transaction 
where biospecimen_id in (select id from lims.biospecimen where study_id  = @STUDYKEY);

select * from pmhdiaendo.ix_biospecimen where biospecimenkey in (
select biospecimenkey FROM pmhdiaendo.IX_BIO_TRANSACTIONS bt 
where deleted = 0 and biospecimenkey not in 
									(select old_id from lims.biospecimen where study_id = 590)
);

select biospecimenkey FROM pmhdiaendo.IX_BIO_TRANSACTIONS bt where deleted = 0 and biospecimenkey not in (select old_id from lims.biospecimen where study_id = 590);
/* 
ie; those that dont have a valid biospecimen
produces the 5 transactions difference between all transactions on pmh and all that are coming in
'0.000000000000000000000000000000','25-JUN-08 06.51.51.801703 AM +08:00','Frozen -80 Celsius','22.000000000000000000000000000000',NULL,'mL',NULL,NULL,'2008-06-25 00:00:00','0.5','sdennis','14196.000000000000000000000000000000',NULL,'Available',NULL,'None','10328.000000000000000000000000000000','sdennis',NULL,NULL,NULL
'0.000000000000000000000000000000','25-MAY-07 09.04.50.539051 AM +08:00','Frozen -80 Celsius','22.000000000000000000000000000000',NULL,'mL',NULL,NULL,'2007-05-25 00:00:00','0.5','laura','9716.000000000000000000000000000000',NULL,'Available',NULL,'None','7290.000000000000000000000000000000','laura',NULL,NULL,'192'
'0.000000000000000000000000000000','31-AUG-07 08.56.34.598766 AM +08:00','Frozen -80 Celsius','24.000000000000000000000000000000',NULL,'mL',NULL,NULL,'2007-08-31 00:00:00','0.5','sdennis','11035.000000000000000000000000000000',NULL,'Available',NULL,'None','8227.000000000000000000000000000000','sdennis',NULL,NULL,NULL
'0.000000000000000000000000000000','31-AUG-07 08.57.12.389827 AM +08:00','Frozen -80 Celsius','24.000000000000000000000000000000',NULL,'mL',NULL,NULL,'2007-08-31 00:00:00','0.5','sdennis','11097.000000000000000000000000000000',NULL,'Available',NULL,'None','8228.000000000000000000000000000000','sdennis',NULL,NULL,NULL
'0.000000000000000000000000000000','06/APR/10 01:53:58.177998 PM +08:00','Frozen -80 Celsius','22.000000000000000000000000000000',NULL,'mL',NULL,NULL,'2010-04-06 00:00:00','0.45','wmair','18011.000000000000000000000000000000',NULL,'Available',NULL,'None','14036.000000000000000000000000000000','wmair',NULL,NULL,NULL

select * from pmhdiaendo.ix_biospecimen where biospecimenkey in (
select biospecimenkey FROM pmhdiaendo.IX_BIO_TRANSACTIONS bt 
where deleted = 0 and biospecimenkey not in 
									(select old_id from lims.biospecimen where study_id = 590)
);

and so it turns out these guys are all deleted biospecimens...so no drama that these transactions don't come across

'-1.000000000000000000000000000000','25-JUN-08 06.51.27.558373 AM +08:00','1',NULL,'0.5 mL tube','1970-01-01 09:50:00','Delay < 1 hr','-1.000000000000000000000000000000','1.000000000000000000000000000000','08T1D03080J','-1.000000000000000000000000000000','2008-06-23 00:00:00','22.000000000000000000000000000000','1970-01-01 06:50:00',NULL,NULL,'Nucleic Acid',NULL,'Human','106278.000000000000000000000000000000','0.25','2008-06-25 00:00:00',NULL,NULL,'14196.000000000000000000000000000000',NULL,'DNA',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL
'-1.000000000000000000000000000000','31-AUG-07 07.57.53.732372 AM +08:00','1',NULL,'0.5 mL tube','1970-01-01 10:25:00','Delay < 1 hr','-1.000000000000000000000000000000','1.000000000000000000000000000000','07T1D02828J','-1.000000000000000000000000000000','2007-08-21 00:00:00','24.000000000000000000000000000000','1970-01-01 11:55:00',NULL,NULL,'Nucleic Acid',NULL,'Human','103618.000000000000000000000000000000','0','2007-08-23 00:00:00',NULL,NULL,'11097.000000000000000000000000000000',NULL,'DNA',NULL,NULL,NULL,'134',NULL,NULL,NULL,NULL
'-1.000000000000000000000000000000','25-MAY-07 09.03.21.193832 AM +08:00','2',NULL,'1 mL tube','1970-01-01 10:50:00','Delay < 1 hr','-1.000000000000000000000000000000','1.000000000000000000000000000000','07T1D02508H','-1.000000000000000000000000000000','2006-09-28 00:00:00','22.000000000000000000000000000000',NULL,NULL,NULL,'Nucleic Acid',NULL,'Human','100159.000000000000000000000000000000','0.25','2006-10-16 00:00:00',NULL,NULL,'9716.000000000000000000000000000000',NULL,'DNA',NULL,NULL,NULL,'192',NULL,NULL,NULL,NULL
'-1.000000000000000000000000000000','30-AUG-07 11.51.19.435416 AM +08:00','1',NULL,'0.5 mL tube','1970-01-01 10:25:00','Delay < 1 hr','-1.000000000000000000000000000000','1.000000000000000000000000000000','07T1D02828H','-1.000000000000000000000000000000','2007-08-21 00:00:00','24.000000000000000000000000000000','1970-01-01 11:55:00',NULL,NULL,'Nucleic Acid',NULL,'Human','103618.000000000000000000000000000000','0','2007-08-23 00:00:00',NULL,NULL,'11035.000000000000000000000000000000',NULL,'DNA',NULL,NULL,NULL,'134',NULL,NULL,NULL,NULL
'-1.000000000000000000000000000000','30/01/2009 02:56:03.003579 PM AUSTRALIA/PERTH','1',NULL,'2 mL tube',NULL,'Delay < 1 hr','-1.000000000000000000000000000000','1.000000000000000000000000000000','07T1D01379H','-1.000000000000000000000000000000','2010-04-06 00:00:00','22.000000000000000000000000000000',NULL,NULL,NULL,'Nucleic Acid',NULL,NULL,'101241.000000000000000000000000000000','0.45','2010-04-06 00:00:00',NULL,NULL,'18011.000000000000000000000000000000','Sampleid: 368 Barcodeid: 1141B Freezer:  Box:  Position: \r\nOriginal volume: 	missing\r\nH sample added 30/01/2009\r\n\r\nDNA Concentration (ng/µL): 123\r\nOD 260/280:  1.93','DNA',NULL,NULL,'4097.000000000000000000000000000000','126','1.830000000000000000000000000000',NULL,NULL,NULL

*/


/*  above select for inset is

SELECT count(*) -- b.id, bt.transactiondate, bt.quantity, bt.recorder, bt.reason
FROM wagerlab.IX_BIO_TRANSACTIONS bt, lims.biospecimen b
WHERE bt.biospecimenkey = b.old_id
AND b.study_id IN (SELECT id FROM study.study WHERE parent_id = @STUDYKEY)
AND bt.DELETED = 0;

*/

/*
select * from lims.bio_transaction where biospecimen_id in(
select id from lims.biospecimen where study_id = @STUDYKEY)*/

-- Update status if initial quantity (where possible)
UPDATE lims.bio_transaction 
SET 
    status_id = 1
WHERE
    reason like 'Initia%'
        AND status_id IS NULL;






/**********

!!!!!!!!!!!!!! NOW SET UP THE USERS, both original and DNA BANK
!!!!!
!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

************************/


SET SESSION group_concat_max_len = 30000;
/*







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
AND bg.GROUP_NAME like (@STUDYNAME || '%')
AND bg.DOMAIN = 'BIOSPECIMEN'
AND bd.FIELDKEY = bf.FIELDKEY
AND bd.DOMAINKEY = bio.BIOSPECIMENKEY
AND bio.DELETED = 0
AND cf.study_id = @STUDYKEY
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
AND bg.GROUP_NAME like (@STUDYNAME || '%')
AND bg.DOMAIN = 'BIOSPECIMEN'
AND bd.FIELDKEY = bf.FIELDKEY
AND bd.DOMAINKEY = bio.BIOSPECIMENKEY
AND bio.DELETED = 0
AND cf.study_id = @STUDYKEY
AND ark_function_id = (SELECT ID FROM study.ark_function WHERE name = 'BIOSPECIMEN')
AND cf.id = cfd.custom_field_id
AND cf.NAME = bf.COLUMNNAME
AND DATE_VALUE IS NOT NULL
AND bs.OLD_ID = bio.BIOSPECIMENKEY;
*/

-- BioCollection pattern
-- Trav : TODO CREATE PARAMS
DELETE FROM `lims`.`biocollectionuid_template` 
WHERE
    STUDY_ID = @STUDYKEY;
INSERT INTO `lims`.`biocollectionuid_template`
(`STUDY_ID`,
`BIOCOLLECTIONUID_PREFIX`,
`BIOCOLLECTIONUID_TOKEN_ID`,
`BIOCOLLECTIONUID_PADCHAR_ID`)
VALUES (@STUDYKEY, @BIOCOLLECTIONUID_PREFIX, @BIOCOLLECTIONUID_TOKEN_ID, @BIOCOLLECTIONUID_PADCHAR_ID);

SELECT @STUDYKEY, @BIOCOLLECTIONUID_PREFIX, @BIOCOLLECTIONUID_TOKEN_ID, @BIOCOLLECTIONUID_PADCHAR_ID;



-- Set base sequence count
-- Trav : TODO CREATE PARAMS
/**************************
!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
ONLY WORKS WHERE EXISTING STRUCTURE PPERMITS and auto
***********************************/
DELETE FROM `lims`.`biocollectionuid_sequence` 
WHERE
    `STUDY_NAME_ID` = @STUDYNAME;
INSERT INTO `lims`.`biocollectionuid_sequence`
(`STUDY_NAME_ID`,
`UID_SEQUENCE`,
`INSERT_LOCK`)
VALUES
(
@STUDYNAME,
(SELECT MAX(TRIM(@BIOCOLLECTIONUID_PREFIX FROM name)) + 1 -- NEED TO CHECK THIS!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
FROM lims.biocollection
WHERE study_id IN (SELECT id FROM study.study WHERE parent_id = @STUDYKEY)
AND name like concat(@BIOCOLLECTIONUID_PREFIX, '%')),  -- 'TN%'),
0
);
/******************************************************
* ELSE DO THIS MANUALLY LIKE BELOW**************************

DELETE FROM `lims`.`biocollectionuid_sequence` 
WHERE
    `STUDY_NAME_ID` = @STUDYNAME;
INSERT INTO `lims`.`biocollectionuid_sequence`
(`STUDY_NAME_ID`,
`UID_SEQUENCE`,
`INSERT_LOCK`)
VALUES (@STUDYNAME, 5000, 0);

 --- U PDATE `lims`.`biocollectionuid_sequence` SET `UID_SEQUENCE`='5000' WHERE `STUDY_NAME_ID`='Vitamin A';

**********************************************************************************************************************/

select * from  `lims`.`biocollectionuid_template` ;

select * from  `lims`.`biospecimenuid_template` ;


select * from  `lims`.`biocollectionuid_sequence` ;

select * from  `lims`.`biospecimenuid_sequence` ;


-- Biospecimen pattern
-- Trav : TODO CREATE PARAMS
DELETE FROM `lims`.`biospecimenuid_template` 
WHERE
    `STUDY_ID` = @STUDYKEY;
INSERT INTO `lims`.`biospecimenuid_template`
(`STUDY_ID`,
`BIOSPECIMENUID_PREFIX`,
`BIOSPECIMENUID_TOKEN_ID`,
`BIOSPECIMENUID_PADCHAR_ID`)
VALUES (@STUDYKEY, @BIOSPECIMENUID_PREFIX, @BIOSPECIMENUID_TOKEN_ID, @BIOSPECIMENUID_PADCHAR_ID); -- NEED TO CHECK THIS!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

select @STUDYKEY, @BIOSPECIMENUID_PREFIX, @BIOSPECIMENUID_TOKEN_ID, @BIOSPECIMENUID_PADCHAR_ID;


-- Set base sequence count
/**************************
!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
ONLY WORKS WHERE EXISTING STRUCTURE PPERMITS - and autogen
***********************************/
DELETE FROM `lims`.`biospecimenuid_sequence` 
WHERE
    `STUDY_NAME_ID` = @STUDYNAME;
INSERT INTO `lims`.`biospecimenuid_sequence`
(`STUDY_NAME_ID`,
`UID_SEQUENCE`,
`INSERT_LOCK`)
VALUES
(
@STUDYNAME,
(SELECT MAX(ID) FROM lims.biospecimen WHERE study_id IN (SELECT ID FROM study.study WHERE parent_id = @STUDYKEY)),
0
);
/******************************************************
* ELSE DO THIS MANUALLY LIKE BELOW**************************

DELETE FROM `lims`.`biospecimenuid_sequence` 
WHERE
    `STUDY_NAME_ID` = @STUDYNAME;
INSERT INTO `lims`.`biospecimenuid_sequence`
(`STUDY_NAME_ID`,
`UID_SEQUENCE`,
`INSERT_LOCK`)
VALUES (@STUDYNAME, 50000, 0);

IN SERT INTO `lims`.`biospecimenuid_sequence`
(`STUDY_NAME_ID`,
`UID_SEQUENCE`,
`INSERT_LOCK`)
VALUES ('Vitamin A', 5000, 0)

U P DATE `lims`.`biospecimenuid_sequence` SET `UID_SEQUENCE`='50000' WHERE `STUDY_NAME_ID`='Vitamin A';

Example To manual add a template

select * from  `lims`.`biocollectionuid_template` ;

select * from  `lims`.`biospecimenuid_template` ;

insert into `lims`.`biocollectionuid_template`(study_id, BIOCOLLECTIONUID_PREFIX, BIOCOLLECTIONUID_TOKEN_ID, BIOCOLLECTIONUID_PADCHAR_ID)
										values (22, 'VTA', NULL, 5);

insert into `lims`.`biospecimenuid_template`(study_id, BIOSPECIMENUID_PREFIX, BIOSPECIMENUID_TOKEN_ID, BIOSPECIMENUID_PADCHAR_ID)
										values (22, 'VTA', NULL, 5);

 INSERT INTO `study`.`subjectuid_sequence` (`STUDY_NAME_ID`, `UID_SEQUENCE`, `INSERT_LOCK`) VALUES (@STUDYNAME, '5000', '0');
 INSERT INTO `study`.`subjectuid_sequence` (`STUDY_NAME_ID`, `UID_SEQUENCE`, `INSERT_LOCK`) VALUES (@STUDYNAME, '5000', '0');

select * from  `study`.`subjectuid_sequence` where study_id = 18;


**********************************************************************************************************************/
select * from lims.biospecimenuid_sequence; -- TODO rewrite another time and run manually.


-- Assign modules to all studies
-- Trav : In each study analyze which modules are needed - or select them all and add the appropriate for max coverage.
DELETE FROM `study`.`link_study_arkmodule` 
WHERE
    study_id IN (SELECT 
        ID
    FROM
        study.study
    
    WHERE
        parent_id = @STUDYKEY);
INSERT INTO `study`.`link_study_arkmodule`
(`STUDY_ID`,
`ARK_MODULE_ID`)
SELECT s.id as study_id, a.id as ark_module_id
FROM study.study s, study.ark_module a
WHERE s.parent_id = @STUDYKEY
AND a.name IN ('Study', 'Subject', 'Datasets', 'LIMS', 'Genotypic',  'Reporting', 'Work Tracking')
ORDER BY s.id, a.id;


SELECT * from study.ark_module a
-- SET starting subject increment
-- TRAV Params
/****
!!!!!!!!!!!!!!!!!!!!!!
THIS IS ONLY IF IT HAS PREFIX ETC  else use the one after it 
also this can't be an update...needs to be an insert
!!!!!!!!!!!!!!!!!!!!!!
*****
UPDATE `study`.`subjectuid_sequence` 
SET 
    `UID_SEQUENCE` = (SELECT 
            TRIM('0' FROM TRIM(LEADING concat(@SUBJECT_PREFIX, @BIOCOLLECTIONUID_TOKEN_DASH) FROM ( SELECT         -- 'WTN-' FROM (SELECT 
                                max(subject_uid) maxid
                            FROM
                                study.link_subject_study
                            WHERE
                                study_id = @STUDYKEY)))
        )
WHERE
    `STUDY_NAME_ID` = @STUDYNAME;
/****
!!!!!!!!!!!!!!!!!
ELSE USE THIS ONE IF it is just a number     -- ALSO PLEASE TEST WHAT HAPPENS TO MAX(dddd) when comparing 111111 to 22 !!!!!  this is a text field
also this can't be an update...needs to be an insert

-- BUT INSTEAD JUST RUN SOMETHING LIKE THIS MANUALLY UNTIL WE KNOW THERE DATA?

 DELETE FROM  `study`.`subjectuid_sequence` where study_name_id = @STUDYNAME;

 INSERT INTO `study`.`subjectuid_sequence` (`STUDY_NAME_ID`, `UID_SEQUENCE`, `INSERT_LOCK`) VALUES (@STUDYNAME, '3685', '0');

OR

(`STUDY_NAME_ID`,
`UID_SEQUENCE`,
`INSERT_LOCK`)
VALUES (@STUDYNAME, 50000, 0); -- or there abouts as suits your migration data

!!!!!!!!!!!!!!!!!
*****/
select max(subject_uid) from study.link_Subject_study where study_id = @STUDYKEY;  -- THIS TAKES THE FIELD AT TEXT ONLY!!!!!!!!! 99> 10000000 !!!!! use another means

select * from  `study`.`subjectuid_sequence` where study_name_id = @STUDYNAME;

-- There is a difference in wager and ark units 
-- we are holding some biospcimens in ml type...move to mL type like the rest - well mysql is setup case insensitive so this does nothing for us
update lims.biospecimen
set unit_id = 17 -- current ark ie mL
where unit_id = 101 -- wager ml
and study_id = @STUDYKEY;

-- select * from lims.biospecimen  where study_id = 194

-- update all transactions to have the units of their parents
-- script for this exists in some past update script in svn

/*
add units to transaction - update existing data to ensure unit matches that of the parent;
logic needs to make sure every transaction gets the unit of the biospecimen if it has one (app logic doesnt permit no unit but...), else use that of the parent, else use that of the grandparent, etc?

TODO:  Look at this when re-running

update lims.bio_transaction t 
inner join lims.biospecimen b on
    t.biospecimen_id = b.id
	and b.study_id = @study_id
	and (t.UNIT_id is null or t.unit_id = 0)
set
    t.unit_id = b.unit_id ;


select count(*) from lims.bio_transaction t 
 inner join lims.biospecimen b on  -- or left join...let's research what we are doing with some selects
    t.biospecimen_id = b.id
	and b.study_id = @STUDYKEY
	and (t.UNIT_id is null or t.unit_id = 0);

select count(*) from  lims.bio_transaction t -- where  t.id = 0;

*/

select distinct hospital from wagerlab.ix_admissions;

select distinct hospital from lims.biocollection where study_id  = 194;

select * From lims.link_subject_Study

select * from study.link_subject_study lss, study.person p
where first_name like '%TEST%ARK%'
and lss.person_id = p.id
-- # ID, PERSON_ID, STUDY_ID, SUBJECT_STATUS_ID, SUBJECT_UID, CONSENT_TO_ACTIVE_CONTACT_ID, CONSENT_TO_PASSIVE_DATA_GATHERING_ID, CONSENT_TO_USE_DATA_ID, CONSENT_STATUS_ID, CONSENT_TYPE_ID, CONSENT_DATE, HEARD_ABOUT_STUDY, COMMENTS, CONSENT_DOWNLOADED, ID, FIRST_NAME, MIDDLE_NAME, LAST_NAME, PREFERRED_NAME, GENDER_TYPE_ID, DATE_OF_BIRTH, DATE_OF_DEATH, REGISTRATION_DATE, CAUSE_OF_DEATH, VITAL_STATUS_ID, TITLE_TYPE_ID, MARITAL_STATUS_ID, PERSON_CONTACT_METHOD_ID, PREFERRED_EMAIL, OTHER_EMAIL, DATE_LAST_KNOWN_ALIVE, OTHER_ID, OTHER_EMAIL_STATUS, PREFERRED_EMAIL_STATUS
-- '123798', '102543', '590', '1', '7365', '1', '1', '1', '1', '2', '2014-11-03', NULL, NULL, NULL, '102543', 'TEST_SUBJECT_ARK', NULL, NULL, NULL, '0', NULL, NULL, NULL, NULL, '0', '0', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL

delete from study.link_subject_study where person_id = 102545;
delete from study.person where id = 102545;
delete from lims.biocollection where link_Subject_study_id = 123800;
delete from lims.biospecimen where link_subject_study_id = 123800
