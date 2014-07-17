
-- SET @STUDYKEY = 17;17waffs    24vus
SET @NEWSTUDYKEY = 17;
SET @COPYFROMSTUDYKEY = 18;
SET @SEARCHNAME1 = 'Biospecimen Detailed Report';
SET @SEARCHNAME2 = 'Locations Info Only';
SET @SEARCHNAME3 = 'Biospecimen Custom Fields';
select * from study.study;
/*
insert into reporting.search(
`NAME`,
`TOP_LEVEL_GROUPING_ID`,
`STUDY_ID`,
`STATUS`,
`STARTTIME`,
`FINISHTIME`,
`INCLUDE_GENO`)
	SELECT 
	`NAME`,
	`TOP_LEVEL_GROUPING_ID`,
	@NEWSTUDYKEY,
	`STATUS`,
	`STARTTIME`,
	`FINISHTIME`,
	`INCLUDE_GENO` 
	FROM `reporting`.`search`
	WHERE 	study_id = @COPYFROMSTUDYKEY
	AND 	name in (@SEARCHNAME1, @SEARCHNAME2, @SEARCHNAME3);

select * from reporting.search where study_id in (@COPYFROMSTUDYKEY, @NEWSTUDYKEY );

select @COPYFROMSTUDYKEY, @NEWSTUDYKEY, s.* from study.study s where  id in (@COPYFROMSTUDYKEY, @NEWSTUDYKEY );


select * from lims.biospecimen where biospecimen_uid = '0208SCZ00154EB2';


select EXTRACTED_TIME,
  TIME_FORMAT(EXTRACTED_TIME, '%H:%i:%s') AS 'Processed Time'
 from wagerlab.ix_biospecimen 
where biospecimen_uid = '0208SCZ00154EB2';

select EXTRACTED_TIME,
  TIME_FORMAT(EXTRACTED_TIME, '%H:%i:%s') AS 'Processed Time'
 from wagerlab.ix_biospecimen 
where biospecimenid = '0208SCZ00154EB2';
*/
SELECT 
    PAT.SUBJECTID as 'SUBJECTUID',
    BIO.BIOSPECIMENID AS 'BIOSPECIMENUID',
	DATE_FORMAT(BIO.SAMPLEDATE, '%d/%m/%Y') AS 'Sample Date',
	TIME_FORMAT(BIO.SAMPLE_TIME, '%H:%i:%s') AS 'Sample Time',
	DATE_FORMAT(BIO.DATEEXTRACTED,'%d/%m/%Y')  AS 'Processed Date',
	TIME_FORMAT(BIO.EXTRACTED_TIME, '%H:%i:%s') AS 'Processed Time',
	--  BIO.COLLABORATOR AS 'COLLABORATOR',
	-- if( ((ifnull(BIO.QTY_REMOVED, 0) + ifnull(BIO.QTY_COLLECTED, 0)) = 0 ), '', (ifnull(BIO.QTY_REMOVED, 0) + ifnull(BIO.QTY_COLLECTED, 0)) ) AS 'Quantity',
	(ifnull(BIO.QTY_REMOVED, 0) + ifnull(BIO.QTY_COLLECTED, 0))  AS 'Quantity',
	--  BIO.QTY_REMOVED  AS 'QuantityRem',
	-- BIO.QTY_COLLECTED  AS 'QuantityCol',
    BIO.DNACONC AS 'Concentration',
    BIO.PURITY AS 'Purity',
    BIO.GRADE AS 'Grade',
    BIO.COMMENTS AS 'Comments',
    UPPER(ifnull(BIO.UNITS, 'unit')) AS 'UNIT',  -- then case as upper in both reports
    IFNULL(BIO.TREATMENT, 'Unknown') AS 'Treatment Type',
    BIO.QUALITY AS 'Quality',
    BIO.ANTICOAG AS 'Anticoagulant',
    BIO.STATUS AS 'Status',
    BIO.PROTOCOL AS 'Protocol',
	--  BIO.QTY_COLLECTED AS 'QTY_COLLECTED',
    BIO.ENCOUNTER AS 'BiocollectionUid',
    BIO.STORED_IN AS 'Stored In'
	--  BIO.EXTRACTED_TIME  AS 'EXTRACTED_TIME_real',
	--   BIO.GESTAT AS 'GESTAT',   never used at all
--  !this is bad we dont extract this...in there interim could manually run something to compare...be very thorough!!!!!!!    BIO.PARENTID AS 'PARENTID'
FROM		
    ZEUS.SUBJECT PAT,
    WAGERLAB.IX_BIOSPECIMEN BIO
WHERE
    PAT.STUDYKEY = @NEWSTUDYKEY
        AND PAT.SUBJECTKEY = BIO.PATIENTKEY
        AND BIO.DELETED = 0
        AND BIO.STUDYKEY = @NEWSTUDYKEY
ORDER BY (PAT.SUBJECTID + 0) , BIO.BIOSPECIMENID;

/* doctored to match ark formating friom this lims report
SELECT PAT.SUBJECTID, BIO.ANTICOAG AS "ANTICOAG", BIO.BIOSPECIMENID AS "BIOSPECIMENID", BIO.COLLABORATOR AS "COLLABORATOR", BIO.COMMENTS AS "COMMENTS", TO_CHAR(BIO.DATEEXTRACTED, 'DD/MM/YYYY') AS "DATEEXTRACTED", BIO.DNACONC AS "DNACONC", BIO.ENCOUNTER AS "ENCOUNTER", TO_CHAR(BIO.EXTRACTED_TIME, 'HH:mm:ss') AS "EXTRACTED_TIME", BIO.GRADE AS "GRADE", BIO.PROTOCOL AS "PROTOCOL", BIO.PURITY AS "PURITY", BIO.QTY_COLLECTED AS "QTY_COLLECTED", BIO.QTY_REMOVED AS "QTY_REMOVED", BIO.QUALITY AS "QUALITY", BIO.QUANTITY AS "QUANTITY", TO_CHAR(BIO.SAMPLEDATE, 'DD/MM/YYYY') AS "SAMPLEDATE", TO_CHAR(BIO.SAMPLE_TIME, 'HH:mm:ss') AS "SAMPLE_TIME", BIO.STATUS AS "STATUS", BIO.STORED_IN AS "STORED_IN", BIO.TREATMENT AS "TREATMENT", BIO.UNITS AS "UNITS", BIO.GESTAT AS "GESTAT", BIO.PARENTID AS "PARENTID"
FROM ZEUS.SUBJECT PAT , WAGERLAB.IX_BIOSPECIMEN BIO 
WHERE PAT.STUDYKEY = 17
AND PAT.SUBJECTKEY = BIO.PATIENTKEY
AND BIO.DELETED = 0
AND BIO.STUDYKEY = 17
ORDER BY PAT.SUBJECTID, BIO.BIOSPECIMENID
*/

select * from lims.biospecimen where biospecimen_uid like '0106SCZ07344LB1';


-- study fee is $4000 p.a.

select count(*) from lims.biospecimen 
where study_id like @NEWSTUDYKEY;  -- 11830 specimens  = $5915 p.a

select count(*) from study.link_subject_study 
where study_id like @NEWSTUDYKEY;  -- 1828 subject = $914 p.a.

-- no pheno in actual prod as yet but that is at 1c per answer  - at last check and without all of Mel's new data it was around $3360pa



select  @NEWSTUDYKEY;

