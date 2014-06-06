-- our lack of a null
update lims.biospecimen
set treatment_type_id = -1 
where old_id in (select biospecimenkey 
		from wagerlab.ix_biospecimen 
		where treatment is null);

-- fix out lack of a Tissue Culture...we only have tissue cultured
update lims.biospecimen
set treatment_type_id = 3 
where old_id in (select biospecimenkey 
		from wagerlab.ix_biospecimen 
		where treatment like 'Tissue Culture%');