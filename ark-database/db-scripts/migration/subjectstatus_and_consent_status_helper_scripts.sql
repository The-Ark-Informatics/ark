select status, count(*) 
from zeus.consent_study 
where subjectkey not in 
	(select subjectkey from zeus.subject 
	where studykey in (18,17, 22, 194, 374, 414))      -- check wago and wartn 436)
group by status;

-- previous migrated studies
'1.000000000000000000000000000000', '9502'

-- all
'0.000000000000000000000000000000', '1246'
'1.000000000000000000000000000000', '96939'
'2.000000000000000000000000000000', '18'
'3.000000000000000000000000000000', '131'
'4.000000000000000000000000000000', '59'


SELECT * FROM zeus.consent_status;	
'0.000000000000000000000000000000', 'Not Consented'
'1.000000000000000000000000000000', 'Consented'
'2.000000000000000000000000000000', 'Refused'
'3.000000000000000000000000000000', 'Withdrawn'
'4.000000000000000000000000000000', 'Ineligible'

select * from study.consent_status;
'1', 'Consented', 'Subject Consented'
'2', 'Not Consented', 'Subject Not Consented'
'3', 'Ineligible', 'Ineligible'
'4', 'Refused', 'Refused'
'5', 'Withdrawn', 'Withdrawn'
'6', 'Pending', 'Pending'

select consent_status_id, count(*) from study.link_subject_study  
group by consent_status_id;



select * from  study.link_subject_study lss
where lss.consent_status_id = (select id from subject_status ss
						where ss.name = (select name from zeus.consent_status cs 
										where cs.status in (select zcstud.status
														from zeus.consent_study zcstud 
														where subjectkey = (select subjectkey 
																			from zeus.subject where subjectid = lss.subject_uid and  subject_uid = '1941'))))
and  subject_uid = '1941';
