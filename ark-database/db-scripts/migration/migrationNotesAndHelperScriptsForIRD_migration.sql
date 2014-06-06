SELECT count(*) FROM wagerlab.ix_biospecimen where studykey=18 and deleted = 0;

SELECT biospecimenkey FROM wagerlab.ix_biospecimen where studykey=18 and deleted = 0;

select * from study.person order by id desc;

select * from wagerlab.ix_admissions where admissionkey = 103075;
select * from wagerlab.ix_biospecimen where admissionkey = 103076; -- the actual one missing 103076  --
select * from wagerlab.ix_admissions where admissionkey = 103076; -- the actual one missing 103076  --
select * from wagerlab.ix_admissions where admissionkey = 103077;

SELECT * -- admissionid 
FROM wagerlab.ix_admissions where studykey=18 and deleted = 0 and admissionid = 103076;  -- !!!!!!!!!!!!!!!!!!! WE HAVE DETErmined that this admision doesnt come accross...this is because ...
select * from zeus.subject where subjectkey = 179322;									-- ... this subject actually does not exist


select * from zeus.subject where subjectkey = 179324; -- this is for admission 103075;  - so just maybe it should have belonged to someone around him...or maybe we did some manual deletes...this is highly unlikely


-- !!!!!!!!!!!!!!!!!!  we have determined the followig biospecimens will not be transfered - all belong to same patient above
-- 2222592.000000000000000000000000000000 -- this guy belongs to the biocollection with the missing subject and is therefore expected missing 
2222593.000000000000000000000000000000  -- these guys have VERY similary timesamps... within 15 seconds of each other and the guy above - this one has no admission key at all
2222594.000000000000000000000000000000 -- these guys have VERY similary timesamps... within 15 seconds of each other and the guy above - this one has no admission key at all

select * from wagerlab.ix_biospecimen 
where biospecimenkey in (
2222592.000000000000000000000000000000,
2222593.000000000000000000000000000000,
2222594.000000000000000000000000000000);


SELECT -- b.id, bt.transactiondate, bt.quantity, bt.recorder, bt.reason  
 count(*)
FROM wagerlab.IX_BIO_TRANSACTIONS bt
where bt.studykey IN (SELECT id FROM study.study WHERE parent_id = @STUDYKEY)
AND bt.DELETED = 0;
-- 8385

select * from zeus.subject 

SELECT bt.biospecimenkey-- b.id, bt.transactiondate, bt.quantity, bt.recorder, bt.reason  
 -- count(*)
FROM wagerlab.IX_BIO_TRANSACTIONS bt
where bt.biospecimenkey IN (SELECT biospecimenkey from wagerlab.ix_biospecimen where studykey = 18)
AND bt.DELETED = 0
and biospecimenkey not in (
2222592.000000000000000000000000000000,
2222593.000000000000000000000000000000,
2222594.000000000000000000000000000000)
and biospecimenkey not in 
(SELECT  bt.biospecimenkey-- b.id, bt.transactiondate, bt.quantity, bt.recorder, bt.reason  
FROM wagerlab.IX_BIO_TRANSACTIONS bt, lims.biospecimen b
WHERE bt.biospecimenkey = b.old_id
AND b.study_id IN (SELECT id FROM study.study WHERE parent_id = @STUDYKEY)
AND bt.DELETED = 0); -- 14636

select * from wagerlab.ix_biospecimen where biospecimenkey = 2031267   -- this guy is the difference...wager doesnt have him, themrefore leave him out...would say person also doesnt exist

select * from wagerlab.ix_admissions where admissionkey = 81410

select distinct deleted from wagerlab.ix_biospecimen

/***************SO we also hve some biospecimens in multiple positions in a box???????
*********************/
select * 
from inv_cell c , inv_box b 
where biospecimen_id = 521680
and c.box_id = b.id

select * from biospecimen where id = '521680'

-- all biospecimens spread into multiple positions
select *
from inv_cell c , inv_cell b
where c.biospecimen_id = b.biospecimen_id 
and c.id <> b.id

select * from lims.biospecimen where id in(
	select c.biospecimen_id
	from inv_cell c , inv_cell b
	where c.biospecimen_id = b.biospecimen_id 
	and c.id <> b.id);
-- so ALL of these biospecimens are in multi positions
'521680', '11IRD02648G', '18', '98135', '2', '62908', NULL, NULL, NULL, '2026848', '-1', '21-DEC-11 12.13.48.580904 PM +08:00', '11IRD02648G', '5', '09:25:00', '2011-12-21 00:00:00', '2011-12-19 00:00:00', 'Blood', 'Buffy Coat', '12:20:00', '1', NULL, '1', NULL, NULL, NULL, NULL, '0', '1', '1', NULL, NULL, NULL, '0', NULL, NULL, NULL
'522163', '10IRD02772H', '18', '98259', '24', '63022', NULL, '521986', '10IRD02772X', '2032932', '2031221', '29-MAR-12 11.09.17.261550 AM +08:00', '10IRD02772H', '1', '01:00:00', '2012-03-29 00:00:00', '2012-03-29 00:00:00', 'Nucleic Acid', 'DNA', '01:00:00', '2', '1', '1', '0.175', NULL, NULL, '0.175000000000000000000000000000', '0', '1', '1', NULL, NULL, NULL, '0', '17.2', '8', '1.45'
'522959', '12IRD02830H', '18', '98317', '24', '63103', NULL, '522268', '12IRD02830X', '2187843', '2183522', '29-MAY-12 01.25.19.969272 PM +08:00', '12IRD02830H', '1', '01:00:00', '2012-05-29 00:00:00', '2012-05-29 00:00:00', 'Nucleic Acid', 'DNA', '01:00:00', '2', '1', '1', '0.2', NULL, 'RP', '0.200000000000000000000000000000', '0', '1', '1', NULL, NULL, NULL, '0', '474.9', '8', '1.8'
