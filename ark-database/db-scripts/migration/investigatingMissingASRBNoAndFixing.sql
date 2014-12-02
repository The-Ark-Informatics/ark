SELECT 
    count(*)
FROM
    zeus.subject
WHERE
    studykey <> 17 AND asrbno IS NOT NULL; -- wafss


SELECT 
    *
FROM
    zeus.study
WHERE
    studykey IN (SELECT 
            studykey
        FROM
            zeus.subject
        WHERE
            studykey <> 17 AND asrbno IS NOT NULL)


