select * from link_subject_study where study_id in (83);

	#pheno.collection
select sequence_next_hi_value from study.subjectuid_sequence where sequence_name = 'STUDY_NAME_ID'

SELECT table_schema "Data Base Name",
sum( data_length + index_length ) / 1024 /
1024 "Data Base Size in MB",
sum( data_free )/ 1024 / 1024 "Free Space in MB"
FROM information_schema.TABLES
GROUP BY table_schema ; 


SELECT table_schema "Data Base Name",
sum( data_length + index_length ) / 1024 /
1024 / 1024 "Data Base Size in GB",
sum( data_free )/ 1024 / 1024 / 1024 "Free Space in GB"
FROM information_schema.TABLES
GROUP BY table_schema ; 


SELECT table_schema "Data Base Name", sum( data_length + index_length ) / 1024 / 1024 "Data Base Size in MB"
FROM information_schema.TABLES GROUP BY table_schema 