select * from wagerlab.ix_inv_cell
where biospecimenkey in (
select biospecimenkey from wagerlab.ix_biospecimen
where studykey = 457
and deleted = 0 );

select * from wagerlab.ix_biospecimen
where studykey = 457
and deleted = 0 ;


select * from zeus.subject
where studykey = 457;


-- noted that three admissions werent coming over from wager...so diffed between wager and what we want to put in wager

select * from wagerlab.ix_admissions
where studykey = 457
and deleted = 0
and admissionkey not in(
SELECT 
    `adm`.admissionkey
FROM
    wagerlab.`IX_ADMISSIONS` `adm`,
    zeus.SUBJECT s,
    zeus.ZE_SUBSTUDY ss,
    `study`.`link_subject_study` `lss`
WHERE
    `adm`.`patientkey` = s.SUBJECTKEY
AND s.subjectid = `lss`.`subject_uid`
AND `lss`.study_id = s.studykey -- `adm`.collectiongroupkey
AND `adm`.studykey = s.studykey
AND `adm`.collectiongroupkey = ss.substudykey
AND ss.studykey = @STUDYKEY
AND `adm`.`DELETED` = 0);

-- seems three biocollections pointing to these three patients/subject don't exist.  Therefore they wont make it.
select * from zeus.subject where subjectkey  in (173465, 173467, 173474);


-- seems three biocollections pointing to these three patients/subject don't exist.  Therefore they wont make it.  
	-- So three specimens point at these three deleted collections...but these specimens are deleted = -1 anyway, so no drama, no lost data.
select * from wagerlab.ix_biospecimen
where admissionkey in(
select ADMISSIONKEY from wagerlab.ix_admissions
where studykey = 457
and deleted = 0
and admissionkey not in(
SELECT 
    `adm`.admissionkey
FROM
    wagerlab.`IX_ADMISSIONS` `adm`,
    zeus.SUBJECT s,
    zeus.ZE_SUBSTUDY ss,
    `study`.`link_subject_study` `lss`
WHERE
    `adm`.`patientkey` = s.SUBJECTKEY
AND s.subjectid = `lss`.`subject_uid`
AND `lss`.study_id = s.studykey -- `adm`.collectiongroupkey
AND `adm`.studykey = s.studykey
AND `adm`.collectiongroupkey = ss.substudykey
AND ss.studykey = @STUDYKEY
AND `adm`.`DELETED` = 0));



SELECT  *
FROM wagerlab.IX_BIO_TRANSACTIONS bt, lims.biospecimen b
WHERE bt.biospecimenkey = b.old_id
AND b.study_id IN (SELECT id FROM study.study WHERE id = @STUDYKEY)
AND bt.DELETED = 0;


-- Have completed step 1.
-- Now do Step 2.