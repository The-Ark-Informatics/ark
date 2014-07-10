select study.id, study.name, count(*) 
from study.study study, study.link_subject_study lss
where lss.study_id = study.id
group by study.id;

select study.studykey, study.studyname, count(*) 
from zeus.study study, zeus.subject lss
where lss.studykey = study.studykey
group by study.studykey;

studyid      study name				number of subjects
-- coming soon
'18.000000000000000000000000000000', 'IRD', '4451'
-- in ark
'22.000000000000000000000000000000', 'Vitamin A', '2217'
'436', 'WAGO Biobank', '1427'            -- plus 4741 total WARTN but they are not active in all the other studies (or at least they say they aren't)
'415', 'Australian Ovarian Cancer Study', '332'

-- using or will use own instance
select * from zeus.subject where studykey = 24


