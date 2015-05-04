select studykey, count(*), s.name
from zeus.subject, study s
where ASRBNO is not null
and s.id = studykey;
/*
'17.000000000000000000000000000000','454','WAFSS'
'18.000000000000000000000000000000','203','IRD'
'22.000000000000000000000000000000','828','Vitamin A'
'457.000000000000000000000000000000','5','ParkC'
1490 in total
*/

select studykey, count(*), s.name
from zeus.subject, study s
where ASRBNO is not null
and s.id = studykey;

select * from study.study

select * from otherid;# ID, PersonID, OtherID, OtherID_Source
-- '1', '83502', '1234', '1234'


-- ok selection looks good.  names match as paranoid check
select s.SUBJECTKEY, s.subjectid, s.FIRSTNAME, p.FIRST_NAME, asrbno, studykey
from zeus.subject s, study.person p
where ASRBNO is not null
and s.SUBJECTKEY = p.OTHER_ID;-- this is 1491 rather than 1490 ?

-- now let's insert
insert into study.otherid(PersonID, OtherID, OtherID_Source)
select p.id, asrbno, 'ASRB Number'
from  zeus.subject s, study.person p
where ASRBNO is not null
and s.SUBJECTKEY = p.OTHER_ID;   -- this is 1491 rather than 1490 ?

select * from otherid -- , person p 
where otherID_Source = 'ASRB Number'
and  personID not in (select person_id from link_Subject_study where study_id in (17, 18, 22, 457));

had to delete one outlier due to repeated otherID caused by PMH migration repeating id's

-- and personid = p.id;

select * from link_subject_study where person_id  = 101303;

select * from lims.biospecimen where biospecimen_uid = '12IRD03122H'

select *  from wagerlab.ix_biospecimen where biospecimenid = '12IRD03122H';


