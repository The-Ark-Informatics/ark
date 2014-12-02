


SELECT 
    *
FROM
    zeus.subject;

SELECT DISTINCT
    marital_status
FROM
    zeus.subject;
/*
NULL
'D'
'M'
'S'
'W'
*/
update zeus.subject 
set marital_status = 'Widow(er)'
where marital_status = 'W';
update zeus.subject 
set marital_status = 'Divorced'
where marital_status = 'D';
update zeus.subject 
set marital_status = 'Single'
where marital_status = 'S';
update zeus.subject 
set marital_status = 'Married'
where marital_status = 'M';

SELECT 
    *
FROM
    study.person;

SELECT DISTINCT
    marital_status_id
FROM
    study.person;
/*
NULL
'D'
'M'
'S'
'W'
*/

-- added this 

INSERT INTO `study`.`marital_status` (`ID`, `NAME`) VALUES ('5', 'Widow(er)');






SELECT 
    *
FROM
    study.marital_status;
/*
'1','Married',NULL
'2','Single',NULL
'3','Divorced',NULL
'4','Unknown',NULL
*/
/*
SELECT *
FROM-- study.marital_status ms,
    study.person p,
    zeus.subject zs
where
	-- ms.name = p.ID
	p.*/

/*this tells me so far there are 1312 that are wrong to fix    ---  we might even be well served in this instance to check that they are correct too */    
select l.subject_uid, l.study_id, p.marital_status_id, zeus_subject.MARITAL_STATUS from study.person p, study.link_subject_study l, zeus.subject zeus_subject
where p.id = l.person_id
and  l.subject_uid = zeus_subject.subjectid
AND l.study_id = zeus_subject.studykey 
-- and zeus_subject.marital_status like CONCAT('W','%')
AND p.marital_status_id IS NULL AND zeus_subject.marital_status IS NOT NULL
    and exists (select id from lims.biospecimen_quality where name like CONCAT(zeus_subject.marital_status,'%'));
    
UPDATE study.person p
        JOIN
    study.link_subject_study l ON p.id = l.person_id
        JOIN
    zeus.subject zeus_subject ON l.subject_uid = zeus_subject.subjectid
        AND l.study_id = zeus_subject.studykey 
SET 
    p.marital_status_id = (select id from study.marital_status where name like CONCAT(zeus_subject.marital_status,'%'))
WHERE
    p.marital_status_id IS NULL 
    AND zeus_subject.marital_status IS NOT NULL;
    
 --   and l.SUBJECT_UID = 'WTN-00003054'
 --   and exists (select id from lims.biospecimen_quality where name like CONCAT(zeus_subject.marital_status,'%'));

