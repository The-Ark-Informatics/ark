select * from study.link_subject_study where 

select * from study.person where 
other_id = 100104

select * from pmhdiaendo.ix_patient where 
patientkey = 100104

select * from pmhdiaendo.ix_admissions where patientkey = 100104 
-- ok so this guy doesn't exist...this specimen does;
	-- '0.000000000000000000000000000000','28-NOV-06 08.44.35.259912 PM +08:00',NULL,'1','100104.000000000000000000000000000000','PMH',NULL,'2943.000000000000000000000000000000',NULL,NULL,'2006-11-28 00:00:00','61.000000000000000000000000000000',NULL,'E1234567',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL
-- ====================

-- ====================
-- next
-- ====================
select * from study.link_subject_study where 

select * from study.person where 
other_id = 100101

select * from pmhdiaendo.ix_patient where 
patientkey = 100101

select * from pmhdiaendo.ix_admissions where patientkey = 100101 
-- ok so this guy doesn't exist...this specimen does;
	-- '0.000000000000000000000000000000','28-NOV-06 08.16.01.094892 PM +08:00',NULL,'1','100101.000000000000000000000000000000','PMH',NULL,'2942.000000000000000000000000000000',NULL,NULL,'2006-11-28 00:00:00','50.000000000000000000000000000000',NULL,'E1234567',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL

select * from pmhdiaendo.ix_patient