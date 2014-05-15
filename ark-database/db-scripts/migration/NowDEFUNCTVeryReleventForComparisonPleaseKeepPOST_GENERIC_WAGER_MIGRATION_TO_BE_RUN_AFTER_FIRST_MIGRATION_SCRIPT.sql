-- Script to be run post generic migration forom wager script

-- There is a difference in wager and ark units 
-- we are holding some biospcimens in ml type...move to mL type like the rest

update biospecimen
set units = 17 -- current ark ie mL
where units = 101; -- wager ml


-- update all transactions to have the units of their parents
-- script for this exists in some past update script in svn

/*
add units to transaction - update existing data to ensure unit matches that of the parent;
logic needs to make sure every transaction gets the unit of the biospecimen if it has one (app logic doesnt permit no unit but...), else use that of the parent, else use that of the grandparent, etc?
*/
update lims.bio_transaction t
left join lims.biospecimen b on
    t.biospecimen_id = b.id
	and b.study_id = @study_id
set
    t.unit_id = b.unit_id ;


