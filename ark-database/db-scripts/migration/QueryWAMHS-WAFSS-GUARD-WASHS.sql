select studykey, count(*) from zeus.subject where studykey in 
	(select studykey 
	from zeus.study 
	where STUDYNAME in ('WAMHS', 'WAFSS', 'GUARD', 'WASHS')) 
    group by studykey;

/*# studykey, count(*)
'5.000000000000000000000000000000', '6536'
'11.000000000000000000000000000000', '1647'
'17.000000000000000000000000000000', '1828'
'29.000000000000000000000000000000', '774'*/

select count(*) from zeus.subject where studykey in 
	(select studykey 
	from zeus.study 
	where STUDYNAME in ('WAMHS', 'WAFSS', 'GUARD', 'WASHS'));

select count(*) from zeus.subject where studykey in 
	(select studykey 
	from zeus.study 
	where STUDYNAME in ('WAMHS', 'WAFSS', 'GUARD', 'WASHS'));

select * from zeus.study 
where STUDYNAME in ('WAMHS', 'WAFSS', 'GUARD', 'WASHS');

select * -- count(*) 
from zeus.subject where studykey = 11;


select * from wagerlab.ix_biospecimen where patientkey in (176006, 176008,176009,176011);

select * from zeus.consent_study where subjectkey in (176006, 176008,176009,176011);

select * from wagerlab.consent_study where subjectkey in (176006, 176008,176009,176011);

select * from zeus.subject where firstname = 'Emma' and surname = 'Smith';

