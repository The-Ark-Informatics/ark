select * from link_subject_study l, person p
where study_id > 72
and l.id > 371111
and l.person_id = p.id
order by l.id desc ;

-- delete from link_subject_study where study_id = 73;
-- delete from subjectuid_sequence where study_name_id = 'pad thai'