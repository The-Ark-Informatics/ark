SELECT 
    l.subject_uid,
    l.study_id,
    p.gender_type_id,
    pmh_subject.sex
FROM
    study.person p,
    study.link_subject_study l,
    pmhdiaendo.ix_patient pmh_subject
WHERE
    p.id = l.person_id
	AND l.subject_uid = pmh_subject.patientid
	AND l.study_id = 590
	AND p.gender_type_id = 0
	AND pmh_subject.sex IS NOT NULL;
-- this suggests we have 1702 rows which were incorreect and need to be changes    



UPDATE study.person p
        JOIN
    study.link_subject_study l ON p.id = l.person_id
        JOIN
    pmhdiaendo.ix_patient pmh_subject ON l.subject_uid = pmh_subject.patientid
        AND l.study_id = 590 
SET 
    p.GENDER_TYPE_ID = (SELECT 
            id
        FROM
            study.gender_type
        WHERE
            name = pmh_subject.sex)
WHERE
    p.gender_type_id = 0
        AND pmh_subject.sex IS NOT NULL;
    
-- – and l.SUBJECT_UID = 'WTN-00003054'
--  and exists (select id from lims.biospecimen_quality where name like CONCAT(zeus_subject.marital_status,'%'));

    
select * from study.link_subject_study where study_id = 590;

select * from study.study where name = 'PMH';

select distinct sex from pmhdiaendo.ix_patient;

select sex, count(*) from pmhdiaendo.ix_patient group by SEX;

/*
NULL
'Male'
'Female'
'Unknown'
*/

SELECT  * FROM study.gender_type
/*
'0', 'Unknown', NULL
'1', 'Male', NULL
'2', 'Female', NULL
*/


select * from study.person

select * from pmhdiaendo.ix_patient