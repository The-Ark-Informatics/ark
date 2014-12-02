
-- email investigation
select * from study.person where id in 
	(select person_id 
	from study.link_subject_study 
	where subject_uid = 'MH-00055118D')

select * from lims.biospecimen where link_subject_study_id in 
	(select id 
	from study.link_subject_study 
	where subject_uid = 'MH-00055118D')
        

select * from study.person where id in
(
select person_id from study.link_subject_study where subject_uid = 'MH-00055118D'
);

/*
I didn't know how to do this off the top of my head;

google;

update from multiple joined table mysql

led to this;

http://stackoverflow.com/questions/8331687/mysql-update-a-joined-table

their example;

UPDATE tableA a
JOIN tableB b
   ON a.a_id = b.a_id
JOIN tableC c
   ON b.b_id = c.b_id
SET b.val = a.val+c.val
WHERE a.val > 10
    AND c.val > 10;

so - let's try it;
*/
UPDATE study.person p
        JOIN
    study.link_subject_study l ON p.id = l.person_id
        JOIN
    zeus.subject zeus_subject ON l.subject_uid = zeus_subject.subjectid
        AND l.study_id = zeus_subject.studykey 
SET 
    p.preferred_email = zeus_subject.email
WHERE
    p.preferred_email IS NULL AND zeus_subject.email IS NOT NULL;


-- TO CONFIRM THIS I HAVE CONVERTED IT TO A SELECT STATEMENT WITH SAME CHECKS - I WANT TO MAKE SURE I HAVE 465 rows also, and visually check they match
select * from study.person p, study.link_subject_study l, zeus.subject zeus_subject
where p.id = l.person_id
and  l.subject_uid = zeus_subject.subjectid
AND l.study_id = zeus_subject.studykey 
AND    p.preferred_email IS NULL AND zeus_subject.email IS NOT NULL;




select * from study.link_subject_study where subject_uid = 'MH-00055118D'


select * from zeus.subject where subjectid = 'MH-00055118D';


SELECT 
    *
FROM
    study.link_subject_study ark_subject_table,
    zeus.subject wager_subject_table
WHERE
    ark_subject_table.comments IS NULL
        AND wager_subject_table.COMMENTS IS NOT NULL
        AND ark_subject_table.SUBJECT_UID = wager_subject_table.SUBJECTID
        AND ark_subject_table.study_id = wager_subject_table.studykey;
    

   

SELECT 
    *
FROM
    study.link_subject_study ark_subject_table,
    zeus.subject wager_subject_table
WHERE
    ark_subject_table.comments <> wager_subject_table.COMMENTS
        AND ark_subject_table.SUBJECT_UID = wager_subject_table.SUBJECTID
        AND ark_subject_table.study_id = wager_subject_table.studykey;
    
    

SELECT 
    *
FROM
    link_subject_study
WHERE
    subject_uid = '1';
    
    
UPDATE study.link_subject_study ark_subject_table
        INNER JOIN
    zeus.subject wager_subject_table ON ark_subject_table.comments IS NULL
        AND wager_subject_table.COMMENTS IS NOT NULL
        AND ark_subject_table.SUBJECT_UID = wager_subject_table.SUBJECTID
        AND ark_subject_table.study_id = wager_subject_table.studykey 
SET 
    ark_subject_table.comments = wager_subject_table.comments;
    
    

/* TEST ONLY
UPDATE study.link_subject_study 
SET 
    comments = NULL
WHERE
    subject_uid = 'MH-00055118D'
        AND study_id = 11;*/
    
UPDATE study.link_subject_study ark_subject_table
        INNER JOIN
    zeus.subject wager_subject_table ON ark_subject_table.comments IS NULL
        AND wager_subject_table.COMMENTS IS NOT NULL 
SET 
    ark_subject_table.comments = wager_subject_table.comments
WHERE
    ark_subject_table.SUBJECT_UID = wager_subject_table.SUBJECTID
        AND ark_subject_table.study_id = wager_subject_table.studykey;
        
        
SELECT 
    *
FROM
    study.link_subject_study
WHERE
    subject_uid = 'MH-00055118D'
        AND study_id = 11;
