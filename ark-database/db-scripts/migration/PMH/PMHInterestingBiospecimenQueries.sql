SELECT * FROM pmhdiaendo.ix_biospecimen
where patientkey  in (select patientkey from pmhdiaendo.ix_patient)
and deleted = 0;

SELECT * FROM pmhdiaendo.ix_biospecimen
where encounter is null;

select * from 
pmhdiaendo.ix_admissions 
where admissionkey  = 10567 

UPDATE `pmhdiaendo`.`ix_admissions` SET `ADMISSIONID`='12345' 
WHERE `ADMISSIONKEY`='10567';

UPDATE `pmhdiaendo`.`ix_biospecimen` SET 
`ENCOUNTER`='12345' 
WHERE `ADMISSIONKEY`='10567';


SELECT * FROM pmhdiaendo.ix_biospecimen
where biospecimenid like '11OBE02434%'

SELECT * FROM pmhdiaendo.ix_biospecimen
where patientkey = 108435

SELECT * FROM pmhdiaendo.ix_admissions
where patientkey = 108435