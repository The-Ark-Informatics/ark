select * from link_subject_study
where subject_uid not like 'WES-0%'
and study_id = 576;

UPDATE `study`.`link_subject_study` SET `SUBJECT_UID`='WES-056' WHERE `ID`='119678' and`STUDY_ID`='576' and`SUBJECT_UID`='SAG-056';
UPDATE `study`.`link_subject_study` SET `SUBJECT_UID`='WES-058' WHERE `ID`='123791' and`STUDY_ID`='576' and`SUBJECT_UID`='WES-58';
UPDATE `study`.`link_subject_study` SET `SUBJECT_UID`='WES-064' WHERE `ID`='123926' and`STUDY_ID`='576' and`SUBJECT_UID`='WES-64';


select * from wagerlab.ix_biospecimen where biospecimenid = '11RAV00015H'


select * from wagerlab.ix_biospecimen 
where biospecimenid like '01VTA00017H%'

select * from lims.bio_transaction

