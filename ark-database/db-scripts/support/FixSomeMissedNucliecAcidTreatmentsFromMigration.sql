SELECT 
distinct BIO.TREATMENT, count(*)
/*
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
*/
FROM		
    ZEUS.SUBJECT PAT,
    WAGERLAB.IX_BIOSPECIMEN BIO
WHERE
    PAT.STUDYKEY = 414
        AND PAT.SUBJECTKEY = BIO.PATIENTKEY
        AND BIO.DELETED = 0
        AND BIO.STUDYKEY = 414
group BY bio.treatment

NULL,'1170'
'70% Alcohol Fixed','51'
'Formalin Fixed','88'
'Frozen','47'
'Nucleic Acid','3'
'Unprocessed','41'

'-1','1170','Unknown'
'4','51','70% Alcohol Fixed'
'1','47','Frozen'
'2','88','Formalin Fixed'
'8','41','Unprocessed'
'11','3','Nucleic Acid'

select  treatment_type_id, count(*), b.name
from lims.biospecimen bio, lims.treatment_type b
where study_id = 414
and bio.treatment_type_id = b.id
group by treatment_type_id


select * from lims.biospecimen where old_id in 
(
select biospecimenkey from  WAGERLAB.IX_BIOSPECIMEN BIO
where treatment = 'Nucleic Acid' and studykey = 414 and deleted = 0 )

update lims.biospecimen 
set treatment_type_id = (select id from lims.treatment_type where name = 'Nucleic Acid')
where old_id in 
(
select biospecimenkey from  WAGERLAB.IX_BIOSPECIMEN BIO
where treatment = 'Nucleic Acid' and studykey = 414 and deleted = 0 )