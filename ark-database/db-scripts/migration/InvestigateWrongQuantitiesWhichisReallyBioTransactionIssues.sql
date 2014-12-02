-- See ARK-1323 - https://the-ark.atlassian.net/browse/ARK-1323
select * from 
wagerlab.ix_biospecimen 
where biospecimenid = '0100SCZ01146H';


select * from 
wagerlab.ix_bio_transactions 
where biospecimenkey in
(select biospecimenkey from wagerlab.ix_biospecimen
where biospecimenid = '0100SCZ01146H');

-- it has a .65 and a -.0013


select * from 
lims.biospecimen 
where biospecimen_uid = '0100SCZ01146H';


SELECT 
    *
FROM
    lims.bio_transaction
WHERE
    biospecimen_id IN (SELECT 
            id
        FROM
            lims.biospecimen
        WHERE
            biospecimen_uid = '0100SCZ01146H');
            

-- looking at this I see all of these transactions that have been inserted and repeated 
-- it is fair that they were attempted to put in at Tegan's request.  
-- However they got repeated...or even wrongly entered...as is the constant risk when someone is manually trying to adjust a whole lot of data
-- '302264','532484','2007-11-13 00:00:00','0.650000000000000000000000000000','laura',NULL,NULL,NULL,'0'
-- '302265','532484','2013-04-05 00:00:00','-0.001300000000000000000000000000','tegan.mcnab@uwa.edu.au','CAR034',NULL,NULL,'0'
-- '322590','532484','2008-09-01 00:00:00','0.120000000000000000000000000000',NULL,'	WAFSS W Plate 01','1',NULL,'17'
-- '357788','532484','2008-09-01 00:00:00','0.044800000000000000000000000000',NULL,'WAFSS W Plate 01','1',NULL,'17'
-- '358076','532484','2008-09-01 00:00:00','0.044800000000000000000000000000',NULL,'WAFSS W Plate 01','1',NULL,'17'
-- '358360','532484','2008-09-01 00:00:00','0.044800000000000000000000000000',NULL,'WAFSS W Plate 01','1',NULL,'17'


select * from lims.bio_transaction where reason like '%WAFSS W Plate 01%'