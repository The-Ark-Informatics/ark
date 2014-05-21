/* ALWAYS CHECK BEFORE RUNNING */
SET @STUDY_GROUP_NAME = 'IRD';
SET @STUDYKEY = 18;
SET @STUDYNAME= 'IRD';
SET @AUTOGEN_SUBJECT = 1;
SET @AUTOGEN_BIOSPECIMEN = 1;
SET @AUTOGEN_BIOCOLLECTION = 1;
-- before setting each of these params check that this can work...ie; that there is not some weird multiple prefix for a given study.

-- SET @SUBJECT_PADCHAR = 8; -- no of chars to pad out
-- apparently subject prefix comes from wager
-- SET @SUBJECT_PREFIX = 'RAV';

SET @BIOCOLLECTIONUID_PREFIX = 'IRD';
-- SET @BIOCOLLECTIONUID_TOKEN_ID = 1;
SET @BIOCOLLECTIONUID_TOKEN_DASH = '';
SET @BIOCOLLECTIONUID_PADCHAR_ID = 5;
	
SET @BIOSPECIMENUID_PREFIX = 'IRD';
-- SET @BIOSPECIMENUID_TOKEN_ID = 1;
SET @BIOSPECIMENUID_PADCHAR_ID = 6;


/* Vit A
-- INVENTORY SQL
SET @STUDYKEY = 22;
SET @STUDYNAME= 'Vitamin A';
SET @AUTOGEN_SUBJECT = 1;
SET @AUTOGEN_BIOSPECIMEN = 1;
SET @AUTOGEN_BIOCOLLECTION = 1;
-- before setting each of these params check that this can work...ie; that there is not some weird multiple prefix for a given study.
-- SET @SUBJECT_PADCHAR = 8; -- no of chars to pad out
-- apparently subject prefix comes from wager
-- SET @SUBJECT_PREFIX = 'RAV';
SET @BIOCOLLECTIONUID_PREFIX = 'VTA';
-- SET @BIOCOLLECTIONUID_TOKEN_ID = 1;
SET @BIOCOLLECTIONUID_TOKEN_DASH = '';
SET @BIOCOLLECTIONUID_PADCHAR_ID = 5;

SET @BIOSPECIMENUID_PREFIX = 'VTA';
-- SET @BIOSPECIMENUID_TOKEN_ID = 1;
SET @BIOSPECIMENUID_PADCHAR_ID = 5;
SET @SITE_PERMITTED = 'WADB (SCGH)' ; 
*/



/**** NOTE!!!   We only RAN (past tense) this the first time for VIT A ... then no new sites will need to be added...therefore commented out for after vitamin A

INSERT INTO `lims`.`inv_site`
(
`DELETED`,
`TIMESTAMP`,
`CONTACT`,
`ADDRESS`,
`NAME`,
`PHONE`)
SELECT `DELETED`,
`TIMESTAMP`,
`CONTACT`,
`ADDRESS`,
`NAME`,
`PHONE` 
FROM wagerlab.IX_INV_SITE 
WHERE ldap_group != 'SJOG' and 		
name not in (select name from lims.inv_site)
and 			-- TRAV TODO Remove this line after initial insert
name not in ('WADB (SCGH)');


PICK BETWEEN THESE

INSERT INTO `lims`.`inv_site`
(
`DELETED`,
`TIMESTAMP`,
`CONTACT`,
`ADDRESS`,
`NAME`,
`PHONE`)
SELECT `DELETED`,
`TIMESTAMP`,
`CONTACT`,
`ADDRESS`,
`NAME`,
`PHONE` 
FROM wagerlab.IX_INV_SITE  s
WHERE ldap_group != 'SJOG' 			-- TRAV TODO Remove this line after initial insert
-- and name not in (select name from lims.inv_site)
ON DUPLICATE KEY update DELETED = s.deleted, TIMESTAMP = s.TIMESTAMP, CONTACT = s.CONTACT, ADDRESS = s.ADDRESS, NAME = s.NAME, PHONE = s.PHONE;

****/






SELECT @STUDYKEY;

SELECT * FROM lims.study_inv_site;

-- map the sites to studies
INSERT INTO lims.study_inv_site (study_id, inv_site_id)
SELECT @STUDYKEY, id
FROM 
(select id from lims.inv_site
where (`NAME`) in
(
select `NAME` from wagerlab.ix_inv_site s
where sitekey in
(
select sitekey from wagerlab.ix_inv_tank where tankkey in
(
select tankkey from wagerlab.ix_inv_box b
where boxkey in
(select boxkey from wagerlab.ix_inv_tray t,
(select 
    distinct c.traykey
from
    wagerlab.ix_biospecimen b,
    wagerlab.ix_inv_cell c
where b.BIOSPECIMENKEY = c.BIOSPECIMENKEY
and b.studykey=@STUDYKEY) ct
where t.traykey = ct.traykey)
)
))) s;

select id, name from lims.inv_site;  -- ensure sites match wager vs prod for any of this to work!!! 
select * from lims.study_inv_site;

/*************
ALWAYS always ALWAYS 
		run the select first before the insert.
   plus follow all the rules regarding prod access;  
		WHAT ENV AM I IN?    
	is AUTO-COMMIT OFFFF?
etc
***************/
-- FREEZERS
INSERT INTO `lims`.`inv_freezer`
(
`DELETED`,
`TIMESTAMP`,
`LOCATION`,
`STATUS`,
`SITE_ID`,
`CAPACITY`,
`LASTSERVICENOTE`,
`NAME`,
`AVAILABLE`,
`DECOMMISSIONDATE`,
`COMMISSIONDATE`,
`LASTSERVICEDATE`,
`DESCRIPTION`)
SELECT t.DELETED, t.TIMESTAMP, t.LOCATION, t.STATUS, lims_site.ID, t.CAPACITY, t.LASTSERVICENOTE, t.NAME, 
            t.AVAILABLE, t.DECOMMISSIONDATE, t.COMMISSIONDATE, t.LASTSERVICEDATE,  t.DESCRIPTION
FROM wagerlab.IX_INV_TANK t, wagerlab.IX_INV_SITE s, lims.inv_site lims_site
WHERE t.SITEKEY = s.SITEKEY
AND s.NAME = lims_site.NAME
-- and lims_site.ID<>16 -- fore wasos got rid of already existing freezer/site
-- the below line eliminates already present freezers from  entry
AND t.name not in (select name from lims.inv_freezer)
AND t.TANKKEY IN 
(
select distinct tankkey from wagerlab.ix_inv_box b
where boxkey in
(select boxkey from wagerlab.ix_inv_tray t,
(select 
    distinct c.traykey
from
    wagerlab.ix_biospecimen b,
    wagerlab.ix_inv_cell c
where b.BIOSPECIMENKEY = c.BIOSPECIMENKEY
and b.studykey=@STUDYKEY) b
where t.traykey = b.traykey)
);



select * from  wagerlab.IX_INV_TANK t;

-- RACKS
INSERT INTO `lims`.`inv_rack`
(
`FREEZER_ID`,
`DELETED`,
`TIMESTAMP`,
`NAME`,
`AVAILABLE`,
`DESCRIPTION`,
`CAPACITY`,
`OLD_ID`)
SELECT f.ID, b.DELETED, b.TIMESTAMP, b.NAME, b.AVAILABLE, b.DESCRIPTION, b.CAPACITY, b.boxkey
FROM wagerlab.IX_INV_BOX b, wagerlab.IX_INV_TANK t, lims.inv_freezer f
WHERE t.TANKKEY = b.TANKKEY
AND t.NAME = f.NAME
AND b.boxkey IN
(
select distinct boxkey from wagerlab.ix_inv_tray where traykey in -- (3230, 3231)
(
		select 
			distinct c.traykey
		from
			wagerlab.ix_biospecimen b,
			wagerlab.ix_inv_cell c
		where b.BIOSPECIMENKEY = c.BIOSPECIMENKEY
		and b.studykey=@STUDYKEY
)
);

select distinct(colnotype) from     wagerlab.`IX_INV_TRAY` t;

select colnotype, count(*) from     wagerlab.`IX_INV_TRAY` t
group by colnotype;

select * from lims.inv_col_row_type;

select colnotype_id, count(*) from     lims.inv_box
group by colnotype_id;

SELECT 
        `crt`.`ID`
    FROM
        `lims`.`inv_col_row_type` `crt`
    WHERE
        `NAME` in(select distinct(colnotype) from     wagerlab.`IX_INV_TRAY` t);

-- I am proposing that we change all number colnotype to Numeric like in lims.

update wagerlab.ix_inv_tray
set colnotype = 'Numeric' where colnotype = 'Number';

update wagerlab.ix_inv_tray
set rownotype = 'Numeric' where rownotype = 'Number';

-- BOXES
/***** 
!!!!!!!!!!!!!!!!!!!!!!!!!!! caution 26 minutes only run on local box or allow significant time out.  Or fix query 
******/
INSERT INTO `lims`.`inv_box`
(`DELETED`,
`TIMESTAMP`,
`NAME`,
`NOOFCOL`,
`CAPACITY`,
`RACK_ID`,
`AVAILABLE`,
`NOOFROW`,
`COLNOTYPE_ID`,
`ROWNOTYPE_ID`,
`OLD_ID`)
	SELECT 
		`t`.`DELETED`,
		`t`.`TIMESTAMP`,
		`t`.`NAME`,
		`t`.`NOOFCOL`,
		`t`.`CAPACITY`,
		(SELECT id FROM `lims`.`inv_rack` WHERE OLD_ID = b.BOXKEY) as `RACK_ID`,
		`t`.`AVAILABLE`,
		`t`.`NOOFROW`,
		ifnull((SELECT `crt`.`ID`
				FROM `lims`.`inv_col_row_type` `crt`
				WHERE `NAME` = `t`.`COLNOTYPE`),123) as `COLNOTYPE_ID`,
		ifnull((SELECT `crt`.`ID`
				FROM `lims`.`inv_col_row_type` `crt`
				WHERE `NAME` = `t`.`ROWNOTYPE`),123) as `ROWNOTYPE_ID`,
		`t`.`TRAYKEY` as OLD_ID
	FROM
		wagerlab.`IX_INV_TRAY` t, 
		wagerlab.`IX_INV_BOX` b-- ,
		-- wagerlab.IX_INV_TANK tank, 
		-- lims.inv_freezer f
	WHERE `t`.`BOXKEY` = `b`.`BOXKEY`
	-- AND tank.TANKKEY = b.TANKKEY
	-- AND tank.NAME = f.NAME
	AND t.traykey in
	(
		select traykey boxkey from wagerlab.ix_inv_tray where traykey in
		(
			select 
				distinct c.traykey
			from
				wagerlab.ix_biospecimen b,
				wagerlab.ix_inv_cell c
			where b.BIOSPECIMENKEY = c.BIOSPECIMENKEY
			and b.studykey=@STUDYKEY)
	);

-- Insert a fake biospecimen for cell merging
INSERT INTO `lims`.`biospecimen`
(`ID`,
`BIOSPECIMEN_UID`,
`STUDY_ID`,
`LINK_SUBJECT_STUDY_ID`,
`SAMPLETYPE_ID`,
`BIOCOLLECTION_ID`,
`OLD_ID`,
`TREATMENT_TYPE_ID`,
`DELETED`)
SELECT '-1' AS ID, 
'FAKE_BIOSPECIMEN' AS BIOSPECIMEN_UID,
(SELECT id FROM study.study WHERE name = @STUDYNAME) AS STUDY_ID, 
(SELECT min(id) FROM study.link_subject_study WHERE study_id =(SELECT id FROM study.study WHERE name = @STUDYNAME)) AS LINK_SUBJECT_STUDY, 
'0' AS SAMPLETYPE_ID, 
(SELECT min(id) FROM lims.biocollection WHERE study_id IN (SELECT id FROM study.study WHERE parent_id =@STUDYKEY)) AS BIOCOLLECTION_ID, 
'-1' AS OLD_ID, 
'1' AS TREATMENT_TYPE_ID, 
'-1' AS DELETED
FROM dual;

select * from study.study;

-- Trav 38893 cells!
-- CELLS
INSERT INTO `lims`.`inv_cell`
(`BOX_ID`,
`DELETED`,
`TIMESTAMP`,
`ROWNO`,
`COLNO`,
`BIOSPECIMEN_ID`,
`BIOSPECIMENKEY`,
`STATUS`)
SELECT 
    b.ID as BOX_ID,
    `c`.`DELETED`,
    `c`.`TIMESTAMP`,
    `c`.`ROWNO`,
    `c`.`COLNO`,
	bio.id as biospecimen_id,
	`c`.`BIOSPECIMENKEY`,
    (CASE
        WHEN `c`.`BIOSPECIMENKEY` > -1 THEN 'Not Empty'
        ELSE 'Empty'
    END) as `STATUS`
FROM
    wagerlab.IX_INV_CELL `c`,
    wagerlab.IX_INV_TRAY `t`, 
	lims.inv_box b,
	lims.biospecimen bio
WHERE
    `c`.`TRAYKEY` = `t`.`TRAYKEY`
AND b.OLD_ID = `t`.`TRAYKEY`	
AND bio.OLD_ID = c.biospecimenkey
AND bio.study_id = @STUDYKEY
and c.biospecimenkey >0 ;


select * from lims.inv_cell 
where box_id in 
	(select id from lims.inv_box where rack_id in
		(select id from lims.inv_rack where study_id = 18));


/* 

-- select every cell which has a biospecimen we care about
select * from wagerlab.ix_inv_cell where biospecimenkey in (select biospecimenkey from wagerlab.ix_biospecimen where studykey=22);

-- select every tray  which has a cell which has a biospecimen we care about
select distinct traykey from wagerlab.ix_inv_cell where biospecimenkey in (select biospecimenkey from wagerlab.ix_biospecimen where studykey=22);

select * from wagerlab.ix_inv_cell where biospecimenkey in (select biospecimenkey from wagerlab.ix_biospecimen where studykey=22);

-- select every box (rack in ark) which has a  tray (box in ark)  which has a cell which has a biospecimen we care about
-- as we go on with this copied unindexed data...these queries will get so big though that we may just run

-- 

select distinct boxkey from wagerlab.ix_inv_tray where traykey in (1, 2, 12212, 123213213, 123123212, etc>> (from the previous query
eg;

select distinct boxkey from wagerlab.ix_inv_tray where traykey in (61);
select * from wagerlab.ix_inv_tray where traykey in (61);

select * from wagerlab.ix_inv_cell where traykey in (61);

-- instead of

select distinct boxkey from wagerlab.ix_inv_tray where traykey in
(select distinct traykey from wagerlab.ix_inv_cell where biospecimenkey in (select biospecimenkey from wagerlab.ix_biospecimen where studykey=22));



-- and 
select distinct tankkey from wagerlab.ix_inv_box where boxid in (1, 12, 12321 etc from previous query)

instead of

select distinct tankkey from wagerlab.ix_inv_tray where 
select distinct boxkey from wagerlab.ix_inv_tray where traykey in
(select distinct traykey from wagerlab.ix_inv_cell where biospecimenkey in (select biospecimenkey from wagerlab.ix_biospecimen where studykey=22));



select * from wagerlab.ix_inv_box where boxkey in 
select boxkey from wagerlab.ix_inv_tray where traykey in
(select distinct traykey from wagerlab.ix_inv_cell where biospecimenkey in (select biospecimenkey from wagerlab.ix_biospecimen where studykey=22);


select * from wagerlab.ix_inv_cell where biospecimenkey = -1; 

-- change cell 601 to point to biospecimenkey 2188706 (new bio id = 501036)
update wagerlab.ix_inv_cell set biospecimenkey = 2188706
where cellkey = 601;

select * from wagerlab.ix_inv_cell where cellkey = 601;


select * from lims.biospecimen where old_id in (select biospecimenkey from wagerlab.ix_biospecimen where studykey=22);

*/

-- Set -1 biospecimen_id to nulls
UPDATE `lims`.`inv_cell` 
SET 
    biospecimen_id = null
WHERE
    biospecimen_id = - 1;
-- remove fake biospecimen
DELETE FROM lims.biospecimen 
WHERE
    id = - 1;