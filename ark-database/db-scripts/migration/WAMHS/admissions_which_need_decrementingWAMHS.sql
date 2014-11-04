select * from study.link_subject_study where id = 109611

select * from study.person where id = 84338

select * from lims.biocollection where link_subject_study_id = 109358;

select * from lims.biocollection where biocollection_uid = 'MH03055';

select * from wagerlab.ix_admissions where ADMISSIONID = 'MH03095';

select * from wagerlab.ix_biospecimen where biospecimenid like '07MH03060R5%'; -- patient = 55281 owns this

select * from wagerlab.ix_biospecimen where encounter  = 'MH030' and deleted =0;

select * from wagerlab.ix_biospecimen where encounter  = 'MH03055' and deleted =0 and studykey = @STUDYKEY;

select * from zeus.subject where subjectkey in (
select patientkey from wagerlab.ix_biospecimen where encounter  = 'MH03050' and deleted =0 and studykey = @STUDYKEY) ;

select * from wagerlab.ix_admissions where admissionid  in ('MH03050'); -- owner = 56751

select * from lims.biocollection where biocollection_uid in ('MH03050') ; -- lss109630 aka person 84360 aka zeus (otherid of 56751)

select * from study.person where id in (select person_id from study.link_subject_study where id= 109654);

-- u-pdate wagerlab.ix_biospecimen  set encounter = MH03100 where patientkey = 56734;
select * from zeus.subject where subjectkey = 56734;

select * from wagerlab.ix_admissions where patientkey in (55050);

select * from zeus.subject where subjectkey  like '55202%';  

select * from wagerlab.ix_admissions where admissionid  in ('MH03065'); -- patient = 58096

select * from wagerlab.ix_biospecimen where encounter  in ('MH03065');
