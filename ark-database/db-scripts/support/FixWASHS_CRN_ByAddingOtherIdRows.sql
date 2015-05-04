select studykey, count(*), s.name
from zeus.subject, study s
where CRN is not null
and s.id = studykey;
select * from link_subject_study where study_id = 11;


select * from study.study

select * from otherid;# ID, PersonID, OtherID, OtherID_Source
-- '1', '83502', '1234', '1234'


-- ok selection looks good.  names match as paranoid check
select s.SUBJECTKEY, s.subjectid, s.FIRSTNAME, p.FIRST_NAME, CRN, studykey
from zeus.subject s, study.person p
where CRN is not null
and s.studykey=11
and p.id in (select person_id from link_subject_study where study_id = 11)
and s.SUBJECTKEY = p.OTHER_ID;   -- this number was off before doing the old_id thing over again...which makes me think other_id is not foolprood

-- now let's insert
insert into study.otherid(PersonID, OtherID, OtherID_Source)
select p.id, CRN, 'CRN'
from  zeus.subject s, study.person p
where CRN is not null
and s.studykey=11
and p.id in (select person_id from link_subject_study where study_id = 11)
and s.SUBJECTKEY = p.OTHER_ID;   -- this is 1491 rather than 1490 ?

select * from otherid where otherID_Source = 'CRN'



