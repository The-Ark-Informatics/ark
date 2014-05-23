

select * from lims.unit a, lims.unit b where a.name = b.name and a.id <> b.id;


select * from lims.biospecimen where unit_id in (7, 41, 101);
/*
'496731', '8', '572', '87664', '83', '53696', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '3', '00:00:00', '2013-07-08 00:00:00', '2013-07-08 00:00:00', NULL, NULL, '00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, '5.000000000000000000000000000000', '7', '10', '0', NULL, NULL, NULL, '0', '1', NULL, NULL
'511570', 'TN000053570', '436', '94008', '13', '59068', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '12:05:00', '2014-04-23 00:00:00', '2014-04-22 00:00:00', NULL, NULL, '11:45:00', NULL, NULL, NULL, NULL, NULL, NULL, '0.500000000000000000000000000000', '41', '1', '0', NULL, NULL, NULL, '0', NULL, NULL, NULL
*/
update lims.biospecimen
set unit_id = 17 
where unit_id in (7,41,101); -- check the unit ids in your specific env first!!!!

select * from lims.bio_transaction where unit_id in (7, 41, 101);
/*
'267393', '496731', '2013-07-08 15:02:01', '5.000000000000000000000000000000', 'lspalding', 'Initial Quantity', '1', NULL, '7'
'280632', '511570', '2014-04-23 15:47:33', '0.500000000000000000000000000000', 'tegan.mcnab@uwa.edu.au', 'Initial Quantity', '1', NULL, '41'
*/
update lims.bio_transaction
set unit_id = 17 
where unit_id in (7,41,101);  -- check the unit ids in your specific env first!!!!

select * from lims.biospecimen;

delete from lims.unit where id in (7,41,101);   -- check the unit ids in your specific env first!!!!

delete from lims.unit where id = 101;
delete from lims.unit where id = 41;
delete from lims.unit where id = 7;

select * from lims.biospecimen where unit_id = 7;
																																																																																																																																							
select * from lims.bio_transaction where unit_id = 41;																																																																																																																																																																																																																																																																																						

select * from lims.biospecimen where unit_id = 17;
																									
select * from lims.bio_transaction where unit_id = 17;

select * from lims.unit a, lims.unit b where a.name = b.name and a.id <> b.id;

show tables in lims;
																																																																												
ALTER TABLE `lims`.`unit` 
ADD UNIQUE INDEX `uq_unit_name` (`NAME` ASC) ;
