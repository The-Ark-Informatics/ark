#SELECT max(id) FROM `study`.`link_subject_study` order by id desc;

#select * from study.upload;
#it is currently 12335 --> 12952 --->37927
select *
from link_subject_study 
where 
#study_id = 1--and 
subject_uid = 'NTSA00048';
