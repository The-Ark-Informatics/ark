SELECT count(*) FROM wagerlab.ix_biospecimen where studykey=18 and deleted = 0;

SELECT biospecimenkey FROM wagerlab.ix_biospecimen where studykey=18 and deleted = 0;

select * from study.person order by id desc;

select * from wagerlab.ix_admissions where admissionkey = 103075;
select * from wagerlab.ix_biospecimen where admissionkey = 103076; -- the actual one missing
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

SELECT -- b.id, bt.transactiondate, bt.quantity, bt.recorder, bt.reason  
 count(*)
FROM wagerlab.IX_BIO_TRANSACTIONS bt
where bt.biospecimenkey IN (SELECT biospecimenkey from wagerlab.ix_biospecimen where studykey = 18)
AND bt.DELETED = 0; -- 14636
