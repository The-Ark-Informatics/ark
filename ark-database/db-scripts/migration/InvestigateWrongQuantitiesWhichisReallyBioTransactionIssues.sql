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

select * from lims.bio_transaction where biospecimen_id in
(select id from lims.biospecimen where biospecimen_uid = '0105SCZ01162H'
)
/*
'304649','534632','2008-01-02 00:00:00','0.475000000000000000000000000000','simone',NULL,NULL,NULL,'0'
'322557','534632','2008-09-01 00:00:00','0.120000000000000000000000000000',NULL,'	WAFSS W Plate 01','1',NULL,'17'
'357755','534632','2008-09-01 00:00:00','0.028100000000000000000000000000',NULL,'WAFSS W Plate 01','1',NULL,'17'
'358043','534632','2008-09-01 00:00:00','0.028100000000000000000000000000',NULL,'WAFSS W Plate 01','1',NULL,'17'
'358328','534632','2008-09-01 00:00:00','0.028100000000000000000000000000',NULL,'WAFSS W Plate 01','1',NULL,'17'
Excel
Parent Biospecimen ID	Volume Removed from Parent (mL)	STUDY KEY	Concentration ng/uL	Stored In	Transaction Date	Quantity (mL)	Site	Tank	Rack	Box	Position	Reason
0105SCZ01162H			0.0281							WAFSS		100					96 Well Plate	1/09/08			0.12	WADB (SCGH)	W -40C Chest Freezer	WAFSS W Plates	WAFSS W Plate 01	F10	WAFSS W Plate 01
*/


select * from lims.bio_transaction where biospecimen_id in
(select id from lims.biospecimen where biospecimen_uid = '0196SCZ00148H'
)
/*
'301192', '531683', '2008-01-02 00:00:00', '0.450000000000000000000000000000', 'simone', NULL, NULL, NULL, '0'
'322507', '531683', '2008-09-01 00:00:00', '0.120000000000000000000000000000', NULL, '	WAFSS W Plate 01', '1', NULL, '17'
Excel 
Parent Biospecimen ID	Volume Removed from Parent (mL)	STUDY KEY	Concentration ng/uL	Stored In	Transaction Date	Quantity (mL)	Site	Tank	Rack	Box	Position	Reason
0196SCZ00148H				0.0603						WAFSS		100					96 Well Plate	1/09/08			0.12	WADB (SCGH)	W -40C Chest Freezer	WAFSS W Plates	WAFSS W Plate 01	F1	WAFSS W Plate 01
*/

select * from lims.bio_transaction where biospecimen_id in
(select id from lims.biospecimen where biospecimen_uid = '0196SCZ00148H'
)
/*'301192','531683','2008-01-02 00:00:00','0.450000000000000000000000000000','simone',NULL,NULL,NULL,'0'
'322507','531683','2008-09-01 00:00:00','0.120000000000000000000000000000',NULL,'	WAFSS W Plate 01','1',NULL,'17'
Excel 
Parent Biospecimen ID	Volume Removed from Parent (mL)	STUDY KEY	Concentration ng/uL	Stored In	Transaction Date	Quantity (mL)	Site	Tank	Rack	Box	Position	Reason
0107SCZ07782H			0.0263							WAFSS				100		96 Well Plate	1/09/08				0.12	WADB (SCGH)	W -40C Chest Freezer	WAFSS W Plates	WAFSS W Plate 11	A4	WAFSS W Plate 11

*/


select count(*) from lims.bio_transaction where reason = 'WAFSS W Plate 01';
-- = 252    Vs   90 in the excel document
-- 252 including only the biospecimenuids from the list too


select count(*) from lims.bio_transaction where reason in ('WAFSS W Plate 01', '	WAFSS W Plate 01');
-- = 252    Vs   90 in the excel document
-- 252 including only the biospecimenuids from the list too
--  Vs 341 adding the tab in the reason with or without the subject uid list.


select distinct biospecimen_id from lims.bio_transaction where reason  in ('WAFSS W Plate 01', '	WAFSS W Plate 01')
 and transaction_date = '2008/09/01 00:00:00'
 and  biospecimen_id in 
 (select id from lims.biospecimen where biospecimen_uid in(
'0199SCZ00139H',
'0196SCZ00148H',
'0199SCZ00150H',
'0198SCZ00155H',
'0198SCZ00187H',
'0198SCZ00189H',
'0196SCZ00205H',
'0197SCZ00219H',
'0100SCZ00253H',
'0106SCZ00256H',
'0199SCZ00285H',
'0198SCZ00296H',
'0100SCZ00299H',
'0197SCZ00331H',
'0108SCZ00354H',
'0299SCZ00377H',
'0196SCZ00385H',
'0196SCZ00386H',
'0196SCZ00418H',
'0100SCZ00439H',
'0196SCZ00462H',
'0197SCZ00465H',
'0197SCZ00469H',
'0197SCZ00473H',
'0196SCZ00487H',
'0196SCZ00535H',
'0196SCZ00538H',
'0102SCZ00597H',
'0197SCZ00607H',
'0197SCZ00671H',
'0197SCZ00773H',
'0199SCZ00781H',
'0197SCZ00817H',
'0197SCZ00875H',
'0196SCZ00879H',
'0197SCZ00897H',
'0197SCZ00898H',
'0300SCZ00901H',
'0296SCZ00902H',
'0105SCZ00911H',
'0197SCZ00994H',
'0196SCZ01003H',
'0100SCZ01009H',
'0105SCZ01044H',
'0105SCZ01049H',
'0100SCZ01087H',
'0196SCZ01089H',
'0196SCZ01090H',
'0103SCZ01114H',
'0196SCZ01153H',
'0105SCZ01162H',
'0197SCZ01163H',
'0198SCZ01325H',
'0196SCZ01345H',
'0197SCZ01346H',
'0100SCZ01380H',
'0100SCZ01441H',
'0197SCZ01448H',
'0196SCZ01459H',
'0198SCZ01461H',
'0197SCZ01475H',
'0196SCZ01490H',
'0208SCZ00154H',
'0207SCZ00185H',
'0105SCZ00196H',
'0208SCZ00268H',
'0106SCZ00306H',
'0105SCZ00347H',
'0196SCZ00459H',
'0308SCZ00488H',
'0108SCZ00554H',
'0106SCZ00627H',
'0108SCZ00672H',
'0106SCZ00693H',
'0106SCZ00779H',
'0106SCZ00828H',
'0106SCZ00864H',
'0106SCZ00922H',
'0106SCZ00923H',
'0106SCZ01084H',
'0106SCZ01106H',
'0106SCZ01120H',
'0196SCZ01145H',
'0100SCZ01146H',
'0196SCZ01202H',
'0106SCZ01225H',
'0106SCZ01350H',
'0106SCZ01402H',
'0196SCZ01460H',
'0196SCZ01492H'));


select count(*) from lims.bio_transaction;

select (140428 - 
139755)

select (140428 - 
138784);

select * from  lims.bio_transaction where biospecimen_id in 
	            (select id 
    from 
    lims.biospecimen where  link_subject_study_id in 
	(select link_subject_study_id from lims.biospecimen where BIOSPECIMEN_UID = '0105SCZ01162H' and study_id = 17)); -- 534632 is the biospecimen of interest


select *
    from 
    lims.biospecimen where  link_subject_study_id in 
	(select link_subject_study_id from lims.biospecimen where BIOSPECIMEN_UID = '0105SCZ01162H' and study_id = 17);

select  * from lims.bio_transaction where biospecimen_id = 534632

select * from lims.biospecimen where id = 534632


select * from study.study



update lims.biospecimen toupdate
inner join (select b.id as bioid, b.biospecimen_uid, b.quantity as bioq, trans_total, cnt from 
	lims.biospecimen b, 
    (select biospecimen_id, sum(t.quantity) as trans_total, count(*) as cnt from lims.bio_transaction t, lims.biospecimen b where study_id = 17  and b.id = t.biospecimen_id group by biospecimen_id) tb
where study_id = 17 
and  b.id = tb.biospecimen_id
and trans_total <> b.quantity ) monster
on monster.bioid = toupdate.id
set toupdate.quantity = monster.trans_total
where study_id = 17

-- and abs(trans_total - b.quantity) > 0.1;
select b.id, b.biospecimen_uid, b.quantity, trans_total, cnt from 
	lims.biospecimen b, 
    (select biospecimen_id, sum(t.quantity) as trans_total, count(*) as cnt from lims.bio_transaction t, lims.biospecimen b where study_id = 17  and b.id = t.biospecimen_id group by biospecimen_id) tb
where 
-- study_id = 17 
-- and
  b.id = tb.biospecimen_id
and trans_total <> b.quantity




select biospecimen_id, sum(t.quantity), count(*) from lims.bio_transaction t, lims.biospecimen b
where study_id = 17 
-- and b.id = 534632
and b.id = t.biospecimen_id
group by biospecimen_id

select * from lims.bio_transaction where biospecimen_id in
(
select id from lims.biospecimen where biospecimen_uid = '0100SCZ04764H'
);



select * from wagerlab.ix_bio_transactions where biospecimenkey in
(
select biospecimenkey from wagerlab.ix_biospecimen where biospecimenid = '0100SCZ04764H'
);


select * from lims.biospecimen where biospecimen_uid = '0100SCZ04764H';



-- below are the notes from emails 9/12/14

And The One Anu looked at is the same.  So I think we can proceed.  The issue is that wager is not showing that transaction.

All other differences with ark and wager are simple rounding issues.

On 9/12/2014 5:24 pm, "Travis Endersby" <travis.endersby@uwa.edu.au> wrote:

OK, the first one on the list, it seems like the ark is correct.  I am not
sure what wager front end showsŠbut I would expect 1.5 + .23 = 1.72 should
be thereŠand it is in the ark.



On 9/12/2014 5:15 pm, "Travis Endersby" <travis.endersby@uwa.edu.au> wrote:



On 9/12/2014 5:09 pm, "Travis Endersby" <travis.endersby@uwa.edu.au>
wrote:

0100SCZ04764H 1.73 ARK    .23 WAGER

0100SCZ00439H 0.2WAGER     1.2ARK




Travis Endersby
Systems Development Manager - The Ark
Centre for Genetic Origins of Health and Disease
+618 9224 0365


