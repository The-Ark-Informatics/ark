
select BIOSPECIMEN_STATUS_ID, count(*) 
from lims.biospecimen
-- where study_id = 18
group by BIOSPECIMEN_STATUS_ID;

select status, count(*) 
from wagerlab.ix_biospecimen
-- where studykey = 18
group by STATUS;

select * from lims.biospecimen_status; -- 1=new 2 Archived

update lims.biospecimen
set BIOSPECIMEN_STATUS_ID = 1
where old_id in (select biospecimenkey 
		from wagerlab.ix_biospecimen 
		where status = 'New');
-- and study_id = 18;


update lims.biospecimen
set BIOSPECIMEN_STATUS_ID = 2
where old_id in (select biospecimenkey 
		from wagerlab.ix_biospecimen 
		where status = 'Archived');

