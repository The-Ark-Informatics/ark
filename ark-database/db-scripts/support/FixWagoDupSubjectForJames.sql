select * from zeus.subject where OTHER_ID is not null;




select * from study.link_subject_Study
where subject_uid in ('WTN-00006545', 'WTN-00006553');
/*
'123901','102605','274','1','WTN-00006545','1','1','1','1','2','2014-11-19',NULL,NULL,NULL
'123902','102605','436','1','WTN-00006545',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL
'123917','102613','274','1','WTN-00006553','1','1','1','1','2','2014-11-19',NULL,NULL,NULL
'123918','102613','436','1','WTN-00006553',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL
*/
-- so go from  123902 to  123918



select * from lims.biocollection where link_subject_study_id in(
select id from study.link_subject_Study
where subject_uid in ('WTN-00006545', 'WTN-00006553')
);

-- this was my fix
UPDATE `lims`.`biocollection` 
SET `LINK_SUBJECT_STUDY_ID`='123918' 
WHERE `ID`='87561';


select * from lims.biospecimen where link_subject_study_id in(
select id from study.link_subject_Study
where subject_uid in ('WTN-00006545', 'WTN-00006553')
);

-- this was my fix
UPDATE `lims`.`biospecimen` SET `LINK_SUBJECT_STUDY_ID`='123918' WHERE `ID`='620310';
UPDATE `lims`.`biospecimen` SET `LINK_SUBJECT_STUDY_ID`='123918' WHERE `ID`='620311';
UPDATE `lims`.`biospecimen` SET `LINK_SUBJECT_STUDY_ID`='123918' WHERE `ID`='620312';
UPDATE `lims`.`biospecimen` SET `LINK_SUBJECT_STUDY_ID`='123918' WHERE `ID`='620313';


-- so now everything is moved, delete the old subject
select * from study.link_subject_Study
where subject_uid in ('WTN-00006545', 'WTN-00006553');
/*
'123901','102605','274','1','WTN-00006545','1','1','1','1','2','2014-11-19',NULL,NULL,NULL
'123902','102605','436','1','WTN-00006545',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL
'123917','102613','274','1','WTN-00006553','1','1','1','1','2','2014-11-19',NULL,NULL,NULL
'123918','102613','436','1','WTN-00006553',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL
*/
-- so go from  123902 to  123918
DELETE FROM `study`.`link_subject_Study` WHERE `ID`='123902' and`STUDY_ID`='436' and`SUBJECT_UID`='WTN-00006545';
DELETE FROM `study`.`link_subject_Study` WHERE `ID`='123901' and`STUDY_ID`='274' and`SUBJECT_UID`='WTN-00006545';

