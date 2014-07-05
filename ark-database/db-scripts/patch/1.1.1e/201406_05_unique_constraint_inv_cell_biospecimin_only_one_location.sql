
-- stops repeat locations for a given biospecimen
-- alter table lims.inv_cell 
-- add unique index (biospecimen_id);
-- of course this can't run yet in prod because it is not allowing it because WE DIDNT HAVE THIS...so logically there are some duplications;

ALTER TABLE `lims`.`inv_cell` 
ADD UNIQUE INDEX `uq_inv_cell_biospecimen` (`BIOSPECIMEN_ID` ASC) ;


select * from lims.inv_cell a,
lims.inv_cell b
where b.id <> a.id
and b.biospecimen_id = a.biospecimen_id;
/*produced;
'224729', '2794', NULL, NULL, '4', '6', NULL, '2310', NULL, '250719', '3054', NULL, NULL, '4', '5', NULL, '2310', NULL
'224729', '2794', NULL, NULL, '4', '6', NULL, '2310', NULL, '250729', '3054', NULL, NULL, '4', '6', NULL, '2310', NULL
'224759', '2794', NULL, NULL, '4', '9', NULL, '2296', NULL, '250759', '3054', NULL, NULL, '4', '9', NULL, '2296', NULL
'250719', '3054', NULL, NULL, '4', '5', NULL, '2310', NULL, '224729', '2794', NULL, NULL, '4', '6', NULL, '2310', NULL
'250719', '3054', NULL, NULL, '4', '5', NULL, '2310', NULL, '250729', '3054', NULL, NULL, '4', '6', NULL, '2310', NULL
'250729', '3054', NULL, NULL, '4', '6', NULL, '2310', NULL, '224729', '2794', NULL, NULL, '4', '6', NULL, '2310', NULL
'250729', '3054', NULL, NULL, '4', '6', NULL, '2310', NULL, '250719', '3054', NULL, NULL, '4', '5', NULL, '2310', NULL
'250759', '3054', NULL, NULL, '4', '9', NULL, '2296', NULL, '224759', '2794', NULL, NULL, '4', '9', NULL, '2296', NULL
'282049', '3347', NULL, NULL, '4', '1', NULL, '496436', NULL, '282059', '3347', NULL, NULL, '4', '2', NULL, '496436', NULL
'282059', '3347', NULL, NULL, '4', '2', NULL, '496436', NULL, '282049', '3347', NULL, NULL, '4', '1', NULL, '496436', NULL
'282128', '3347', NULL, NULL, '3', '9', NULL, '496434', NULL, '282138', '3347', NULL, NULL, '3', '10', NULL, '496434', NULL
'282138', '3347', NULL, NULL, '3', '10', NULL, '496434', NULL, '282128', '3347', NULL, NULL, '3', '9', NULL, '496434', NULL
*/

select * from lims.biospecimen where id in(select a.biospecimen_id 
from lims.inv_cell a,
lims.inv_cell b
where b.id <> a.id
and b.biospecimen_id = a.biospecimen_id
);
/* produced
'2296', '11 000589 120811 90-1', '1', '590', '83', '137', NULL, '1019', '11 000589 120811 90', NULL, NULL, NULL, NULL, NULL, NULL, '2011-09-12 00:00:00', '2011-09-11 00:00:00', NULL, NULL, NULL, '1', NULL, '1', NULL, NULL, NULL, '0.500000000000000000000000000000', '17', '9', '0', '3', '1', NULL, '0', NULL, NULL, NULL
'2310', '11 000425 160811 91-1', '1', '426', '83', '140', NULL, '1026', '11 000425 160811 91', NULL, NULL, NULL, NULL, NULL, NULL, '2011-09-16 00:00:00', '2011-09-15 00:00:00', NULL, NULL, NULL, '1', NULL, '1', NULL, NULL, NULL, '0.500000000000000000000000000000', '17', '9', '0', '3', '1', NULL, '0', NULL, NULL, NULL
'496434', '12008050 041012 801', '1', '10473', '36', '53595', NULL, '496346', '12008050 041012', NULL, NULL, NULL, NULL, NULL, NULL, '2013-04-19 00:00:00', '2012-10-04 00:00:00', NULL, NULL, NULL, '1', NULL, '1', NULL, NULL, NULL, '3.000000000000000000000000000000', '17', '8', '0', NULL, NULL, NULL, NULL, NULL, NULL, NULL
'496436', '13032137 041012 801', '1', '5869', '36', '53596', NULL, '496347', '13032137 041012', NULL, NULL, NULL, NULL, NULL, NULL, '2013-04-19 00:00:00', '2012-10-04 00:00:00', NULL, NULL, NULL, '1', NULL, '1', NULL, NULL, NULL, '2.000000000000000000000000000000', '17', '8', '0', NULL, NULL, NULL, NULL, NULL, NULL, NULL

select * from study.study where id = 1
all of which are lifepool DEMO and not real, therefore will simply remove repeaters

So basically I am going to just empty out some of these and if you need to recover them we can do so from the back up I made priod to doing this...but i believe this is just demo anyway.
*/
select * from lims.inv_cell where id in (224759, 250719, 250759, 282128, 282049, 282138, 250729);

update lims.inv_cell set status = 'Empty', biospecimen_id = null 
where id in (224759, 250719, 250759, 282128, 282049, 282138, 250729);
