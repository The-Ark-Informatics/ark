-- more fixes to WAMHS data.
UPDATE `wagerlab`.`ix_biospecimen` 
SET `ENCOUNTER`='MH01465' WHERE `BIOSPECIMENKEY`='1698093.000000000000000000000000000000';

-- /start    decremented ALL of these people

UPDATE `wagerlab`.`ix_biospecimen` SET `ENCOUNTER`='MH01465' WHERE `BIOSPECIMENKEY`='1698253.000000000000000000000000000000';
UPDATE `wagerlab`.`ix_biospecimen` SET `ENCOUNTER`='MH01465' WHERE `BIOSPECIMENKEY`='1698261.000000000000000000000000000000';
UPDATE `wagerlab`.`ix_biospecimen` SET `ENCOUNTER`='MH01465' WHERE `BIOSPECIMENKEY`='1698262.000000000000000000000000000000';
UPDATE `wagerlab`.`ix_biospecimen` SET `ENCOUNTER`='MH01465' WHERE `BIOSPECIMENKEY`='1698503.000000000000000000000000000000';
UPDATE `wagerlab`.`ix_biospecimen` SET `ENCOUNTER`='MH01465' WHERE `BIOSPECIMENKEY`='1698505.000000000000000000000000000000';
UPDATE `wagerlab`.`ix_biospecimen` SET `ENCOUNTER`='MH01465' WHERE `BIOSPECIMENKEY`='1698506.000000000000000000000000000000';
UPDATE `wagerlab`.`ix_biospecimen` SET `ENCOUNTER`='MH01465' WHERE `BIOSPECIMENKEY`='1698743.000000000000000000000000000000';
UPDATE `wagerlab`.`ix_biospecimen` SET `ENCOUNTER`='MH01465' WHERE `BIOSPECIMENKEY`='1698925.000000000000000000000000000000';

UPDATE `wagerlab`.`ix_biospecimen` SET `ENCOUNTER`='MH01820' WHERE `BIOSPECIMENKEY`='1698168.000000000000000000000000000000';
UPDATE `wagerlab`.`ix_biospecimen` SET `ENCOUNTER`='MH01820' WHERE `BIOSPECIMENKEY`='1698286.000000000000000000000000000000';
UPDATE `wagerlab`.`ix_biospecimen` SET `ENCOUNTER`='MH01820' WHERE `BIOSPECIMENKEY`='1699155.000000000000000000000000000000';

UPDATE `wagerlab`.`ix_biospecimen` SET `ENCOUNTER`='MH01900' WHERE `BIOSPECIMENKEY`='1699200.000000000000000000000000000000';
UPDATE `wagerlab`.`ix_biospecimen` SET `ENCOUNTER`='MH01900' WHERE `BIOSPECIMENKEY`='1699452.000000000000000000000000000000';
UPDATE `wagerlab`.`ix_biospecimen` SET `ENCOUNTER`='MH01900' WHERE `BIOSPECIMENKEY`='1699451.000000000000000000000000000000';
UPDATE `wagerlab`.`ix_biospecimen` SET `ENCOUNTER`='MH01900' WHERE `BIOSPECIMENKEY`='1699658.000000000000000000000000000000';

UPDATE `wagerlab`.`ix_biospecimen` SET `ENCOUNTER`='MH03045' WHERE `BIOSPECIMENKEY`='1722749.000000000000000000000000000000';
UPDATE `wagerlab`.`ix_biospecimen` SET `ENCOUNTER`='MH03045' WHERE `BIOSPECIMENKEY`='1722750.000000000000000000000000000000';
UPDATE `wagerlab`.`ix_biospecimen` SET `ENCOUNTER`='MH03045' WHERE `BIOSPECIMENKEY`='1722751.000000000000000000000000000000';

update wagerlab.ix_biospecimen 
set ENCOUNTER = 'MH03050' 
where ENCOUNTER = 'MH03051' LIMIT 100;



-- /end of all decrement by one guys

-- put him under this other encounter because it had same date/time etc and the other encounter did not exist
UPDATE `wagerlab`.`ix_biospecimen` SET `ENCOUNTER`='MH03110' WHERE `BIOSPECIMENKEY`='1722901.000000000000000000000000000000';
UPDATE `wagerlab`.`ix_biospecimen` SET `ENCOUNTER`='MH03110' WHERE `BIOSPECIMENKEY`='1722902.000000000000000000000000000000';
UPDATE `wagerlab`.`ix_biospecimen` SET `ENCOUNTER`='MH03110' WHERE `BIOSPECIMENKEY`='1722903.000000000000000000000000000000';
UPDATE `wagerlab`.`ix_biospecimen` SET `ENCOUNTER`='MH03110' WHERE `BIOSPECIMENKEY`='1722923.000000000000000000000000000000';
UPDATE `wagerlab`.`ix_biospecimen` SET `ENCOUNTER`='MH03110' WHERE `BIOSPECIMENKEY`='1722983.000000000000000000000000000000';


select * from wagerlab.ix_biospecimen where biospecimenid = '07MH21322A'; -- 58070 is patient MH03045 is encounter which he doesnt own
									-- in fact all he owns is.MH03105 therefore set biospecimen to that encounter
UPDATE `wagerlab`.`ix_biospecimen` SET `ENCOUNTER`='MH03105' WHERE `BIOSPECIMENKEY`='1722749.000000000000000000000000000000';                                  
UPDATE `wagerlab`.`ix_biospecimen` SET `ENCOUNTER`='MH03105' WHERE `BIOSPECIMENKEY`='1722750.000000000000000000000000000000';
UPDATE `wagerlab`.`ix_biospecimen` SET `ENCOUNTER`='MH03105' WHERE `BIOSPECIMENKEY`='1722751.000000000000000000000000000000';
  
-- this is all he has..so change to it
update wagerlab.ix_biospecimen 
set ENCOUNTER = 'MH03052' 
where ENCOUNTER = 'MH03099' LIMIT 100;
                                  

-- this was pointing to something it didn't own - there fore point to a fake on which it now owns. (it didnt own anything else)
UPDATE `wagerlab`.`ix_biospecimen` SET `ENCOUNTER`='MH04224' WHERE `BIOSPECIMENKEY`='1725324.000000000000000000000000000000';

UPDATE `wagerlab`.`ix_biospecimen` SET `ENCOUNTER`='MH03106' WHERE `BIOSPECIMENKEY`='1725325.000000000000000000000000000000';

-- she only owns 3100 therefore change these encounters she doesnt own to the one she does.
update wagerlab.ix_biospecimen 
set encounter = 'MH03100' 
where patientkey = 56734
and encounter = 'MH03060' limit 100;



-- MH03097 is all that is owned by 55054   (he should not have his biospecimens pointing to MH03050)
update wagerlab.ix_biospecimen 
set encounter = 'MH03097' 
where patientkey = 55054
and encounter = 'MH03050' limit 100;


-- MH03098  is all that is owned by 55066   (he should not have his biospecimens pointing to MH03050)
update wagerlab.ix_biospecimen 
set encounter = 'MH03098' 
where patientkey = 55066
and encounter = 'MH03050' limit 100;



-- MH04224 is all that is owned by 55055  (he should not have his biospecimens pointing to MH03055)
update wagerlab.ix_biospecimen 
set encounter = 'MH04224' 
where patientkey = 55055
and encounter = 'MH03055' limit 100;

-- MH04220 is all that is owned by 55277  (he should not have his biospecimens pointing to 3080)
update wagerlab.ix_biospecimen 
set encounter = 'MH04220' 
where patientkey = 55277
and encounter = 'MH03080' limit 100;

-- MH04225 is all that is owned by 55303
update wagerlab.ix_biospecimen 
set encounter = 'MH04225' 
where patientkey = 55303  limit 100;


-- MH04222 is all that is owned by 56757
update wagerlab.ix_biospecimen 
set encounter = 'MH04222'
where patientkey = 56757  limit 100;

-- MH04221 is all that is owned by 56730
update wagerlab.ix_biospecimen 
set encounter = 'MH04221'
where patientkey = 56730  limit 100;

-- MH04223 is all that is owned by 55052
update wagerlab.ix_biospecimen 
set encounter = 'MH04223'
where patientkey = 55052  limit 100;

-- MH03095 is all that is owned by 55050
update wagerlab.ix_biospecimen 
set encounter = 'MH03095'
where patientkey = 55050  and encounter = 'MH03065' limit 100;

-- MH04218 is all that is owned by 55202
update wagerlab.ix_biospecimen 
set encounter = 'MH04218'
where patientkey = 55202  limit 100;


