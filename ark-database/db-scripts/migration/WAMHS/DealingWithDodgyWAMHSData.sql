-- WAMHS PARAMETERS
-- latest
SET @STUDY_GROUP_NAME = 'WAMHS';
SET @STUDYKEY = 11;
SET @STUDYNAME= 'WAMHS';
SET @AUTOGEN_SUBJECT = 1;
SET @AUTOGEN_BIOSPECIMEN = 1;
SET @AUTOGEN_BIOCOLLECTION = 1;
-- before setting each of these params check that this can work...ie; that there is not some weird multiple prefix for a given study.

-- SET @SUBJECT_PADCHAR = 8; -- no of chars to pad out
-- apparently subject prefix comes from wager
-- SET @SUBJECT_PREFIX = 'RAV';

SET @BIOCOLLECTIONUID_PREFIX = 'MHC';
-- SET @BIOCOLLECTIONUID_TOKEN_ID = 1;
SET @BIOCOLLECTIONUID_TOKEN_DASH = '';
SET @BIOCOLLECTIONUID_PADCHAR_ID = 5;
	
SET @BIOSPECIMENUID_PREFIX = 'MHS';
-- SET @BIOSPECIMENUID_TOKEN_ID = 1;
SET @BIOSPECIMENUID_PADCHAR_ID = 6;

select *
from wagerlab.ix_biospecimen where biospecimenid = '07MH01466R1';

select * from wagerlab.ix_biospecimen where encounter = 'MH01466'

select * from lims.biocollection where name = 'MH01465';

select * from wagerlab.ix_admissions where admissionid = 'MH01466';

select * from zeus.subject where subjectkey = 55151

select * from wagerlab.ix_admissions where patientkey = 55119

SELECT 
	s.subjectid,
    `b`.`BIOSPECIMENID` as `biospecimen_uid`,
    `lss`.`STUDY_ID`,
    b.encounter,
    `lss`.`ID` as `link_subject_study_id`,
    (SELECT min(id) FROM lims.bio_sampletype WHERE (sampletype, samplesubtype) = (b.sampletype, b.samplesubtype)) AS `sampletype_id`,
    (SELECT min(id) FROM lims.biocollection bc WHERE (bc.link_subject_study_id, bc.study_id, bc.name) = 
																	(lss.id, lss.study_id, b.encounter)) AS `biocollection_id`,
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
    IFNULL((SELECT min(id) FROM lims.treatment_type tt WHERE UPPER(tt.name) = UPPER(b.TREATMENT)),-1) as `treatment_type_id`,
    1 as `barcoded`,
    IFNULL((SELECT min(id) FROM lims.unit WHERE name = b.UNITS), 0) as `UNIT_ID`,
    `b`.`PURITY`,
    (SELECT max(id) FROM lims.biospecimen_protocol WHERE name = b.PROTOCOL) as `BIOSPECIMEN_PROTOCOL_ID`,
    (SELECT max(id) FROM lims.biospecimen_grade WHERE name = b.GRADE) as `BIOSPECIMEN_GRADE_ID`,
    (SELECT max(id) FROM lims.biospecimen_storage WHERE name = b.STORED_IN) as `BIOSPECIMEN_STORAGE_ID`,
    b.DNACONC as `CONCENTRATION`,
	(SELECT max(id) FROM lims.BIOSPECIMEN_ANTICOAGULANT WHERE name = b.ANTICOAG) as `BIOSPECIMEN_ANTICOAGULANT_ID`,
	(SELECT max(id) FROM lims.BIOSPECIMEN_QUALITY WHERE name = b.QUALITY) as `BIOSPECIMEN_QUALITY_ID`,
	(SELECT max(id) FROM lims.BIOSPECIMEN_STATUS WHERE name = b.STATUS) as `BIOSPECIMEN_STATUS_ID`
FROM
    wagerlab.`IX_BIOSPECIMEN` `b`,
    zeus.SUBJECT s,
   --  zeus.ZE_SUBSTUDY ss,  This was only needed for WARTN substudy ...keep as reference in case we need again
    `study`.`link_subject_study` `lss`
WHERE
    `b`.`patientkey` = s.SUBJECTKEY
AND s.subjectid = `lss`.`subject_uid`
AND `lss`.study_id = `b`.studykey
and b.biospecimenkey <> 1728287 -- one we know not to exist - todo lookup
-- AND `b`.substudykey = ss.substudykey  This was only needed for WARTN substudy ...keep as reference in case we need again
AND s.studykey = @STUDYKEY
AND `b`.`DELETED` = 0;

select * from wagerlab.ix_biospecimen 
where admissionkey is null and studykey = @STUDYKEY ;


select * from wagerlab.ix_biospecimen 
where encounter is null and studykey = @STUDYKEY ;


select * from wagerlab.ix_biospecimen 
where admissionkey is null and studykey = @STUDYKEY ;

select * from zeus.subject where subjectkey = 55303 ;




select * from wagerlab.ix_biospecimen 
where encounter is null and studykey = @STUDYKEY and deleted = 0 ;


