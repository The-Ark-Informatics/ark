

select QUALITY, count(*) 
from wagerlab.ix_biospecimen 
where studykey  = 18
group by quality;


select BIOSPECIMEN_QUALITY_ID, count(*) 
from lims.biospecimen 
where study_id = 18;


select * from lims.biospecimen_quality;

--  where old_id is null

update lims.biospecimen
set BIOSPECIMEN_QUALITY_ID = 2
where old_id in (select biospecimenkey 
		from wagerlab.ix_biospecimen 
		where QUALITY = 'Frozen short term (<6mths)');
-- and study_id = 18;


update lims.biospecimen
set BIOSPECIMEN_QUALITY_ID = 1
where old_id in (select biospecimenkey 
		from wagerlab.ix_biospecimen 
		where QUALITY = 'Fresh');  -- note this number is higher than study = 18 due to the previous ones we missed
