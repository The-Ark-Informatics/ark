select * from zeus.study;

-- '21.000000000000000000000000000000','KS2','Kidskin2','2014-08-05 10:28:37','2007-10-22 15:30:33','Matt Legge','WAGERLAB','Kidskin2',NULL,'0','1','KIDSKIN2',NULL,'0.000000000000000000000000000000','0.000000000000000000000000000000'

select * from zeus.subject where studykey = 21
and subjectkey in (select subjectkey from zeus.consent_study where status = 1);

select * from zeus.consent_section;

select * from zeus.consent_study where studykey = 21;

select * from 

select * from study_inv_site;


select * from study s 
where s.id not in (1,2,562, 565, 567, 570,571,572,573,574,575,578,579,580,583,584,585,588,589,591) 
and s.PARENT_ID<>274;

select * from inv_site i;

