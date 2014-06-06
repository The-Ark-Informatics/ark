
-- stops repeat locations for a given biospecimen
alter table lims.inv_cell 
add unique index (biospecimen_id);
