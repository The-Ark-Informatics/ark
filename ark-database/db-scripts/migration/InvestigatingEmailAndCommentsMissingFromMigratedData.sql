select * from study.link_subject_Study lss, zeus.ix_patient
where 
;


select * from study.person where id in
(
select person_id from study.link_subject_study where subject_uid = 'MH-00055118D'
);




select * from study.link_subject_study where subject_uid = 'MH-00055118D'


select * from zeus.subject where subjectid = 'MH-00055118D';


select * from  
		study.link_subject_study ark_subject_table, 
		zeus.subject wager_subject_table
where ark_subject_table.comments is null 
	and wager_subject_table.COMMENTS is not null	
	and ark_subject_table.SUBJECT_UID = wager_subject_table.SUBJECTID
    and ark_subject_table.study_id = wager_subject_table.studykey;
    
   
   

select * from  
		study.link_subject_study ark_subject_table, 
		zeus.subject wager_subject_table
where ark_subject_table.comments <> wager_subject_table.COMMENTS 
	and ark_subject_table.SUBJECT_UID = wager_subject_table.SUBJECTID
    and ark_subject_table.study_id = wager_subject_table.studykey;
    
    

select * from link_subject_study where subject_uid = '1';    
    
    
update 
	study.link_subject_study ark_subject_table 
inner join zeus.subject wager_subject_table 
on
	ark_subject_table.comments is null 
	and wager_subject_table.COMMENTS is not null	
	and ark_subject_table.SUBJECT_UID = wager_subject_table.SUBJECTID
    and ark_subject_table.study_id = wager_subject_table.studykey
set
    ark_subject_table.comments = wager_subject_table.comments;
    
    


update study.link_subject_study set comments = null 
where subject_uid = 'MH-00055118D' and study_id = 11 ;
    
update 
	study.link_subject_study ark_subject_table 
inner join zeus.subject wager_subject_table 
on
	ark_subject_table.comments is null 
	and wager_subject_table.COMMENTS is not null	
set
    ark_subject_table.comments = wager_subject_table.comments
where
	ark_subject_table.SUBJECT_UID = wager_subject_table.SUBJECTID
    and ark_subject_table.study_id = wager_subject_table.studykey;
        
        
        select * from study.link_subject_study where subject_uid = 'MH-00055118D' and study_id = 11 ;
