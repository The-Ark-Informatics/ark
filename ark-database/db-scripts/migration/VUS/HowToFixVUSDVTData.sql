select * from lims.biocollection where study_id = 24;


select * from wagerlab.ix_admissions 
where admissionid like 'DVT%';


select * from wagerlab.ix_biospecimen 
where encounter like 'DVT%';

select 'UPDATE wagerlab.ix_biospecimen set encounter=VUS' as update_string,
		SUBSTRING_INDEX(encounter,'T',-1),
		'where encounter = ' as more_text,
		encounter
FROM  wagerlab.ix_biospecimen 
where encounter like 'DVT%'
		and studykey=24;

