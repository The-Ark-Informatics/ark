
-- SITES ************************* NEARLY WORKS NEEDS TWEAKING
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
WHERE ldap_group != 'SJOG'and 			-- TRAV TODO Remove this line after initial insert
name not in (select name from lims.inv_site);


-- map the sites to studies
INSERT INTO lims.study_inv_site (study_id, inv_site_id)
SELECT id, (SELECT id FROM lims.inv_site WHERE name = @SITE_PERMITTED)  --  TODO TRAV long term remove this
FROM study.study
WHERE parent_id = @STUDYKEY;


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
AND s.NAME != 'SJOG'
AND t.TANKKEY NOT IN (222, 223, 224, 225)
and s.name = @SITE_PERMITTED	-- todo maybe group of sites
and t.sitekey = @SITE_PERMITTED;




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
AND t.TANKKEY NOT IN (222, 223, 224, 225);

-- BOXES
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
    ifnull((SELECT 
        `crt`.`ID`
    FROM
        `lims`.`inv_col_row_type` `crt`
    WHERE
        `NAME` = `t`.`COLNOTYPE`),123) as `COLNOTYPE_ID`,
    ifnull((SELECT 
        `crt`.`ID`
    FROM
        `lims`.`inv_col_row_type` `crt`
    WHERE
        `NAME` = `t`.`ROWNOTYPE`),123) as `ROWNOTYPE_ID`,
`t`.`TRAYKEY` as OLD_ID
FROM
    wagerlab.`IX_INV_TRAY` t, 
    wagerlab.`IX_INV_BOX` b,
    wagerlab.IX_INV_TANK tank, 
    lims.inv_freezer f
WHERE `t`.`BOXKEY` = `b`.`BOXKEY`
AND tank.TANKKEY = b.TANKKEY
AND tank.NAME = f.NAME
AND tank.TANKKEY NOT IN (222, 223, 224, 225);


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
        WHEN `c`.`BIOSPECIMENKEY` > -1 THEN 2
        ELSE 1
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

/* 

-- select every cell which has a biospecimen we care about
select * from wagerlab.ix_inv_cell where biospecimenkey in (select biospecimenkey from wagerlab.ix_biospecimen where studykey=194);

-- select every tray  which has a cell which has a biospecimen we care about
select distinct traykey from wagerlab.ix_inv_cell where biospecimenkey in (select biospecimenkey from wagerlab.ix_biospecimen where studykey=194);

select * from wagerlab.ix_inv_cell where biospecimenkey in (select biospecimenkey from wagerlab.ix_biospecimen where studykey=194);

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
(select distinct traykey from wagerlab.ix_inv_cell where biospecimenkey in (select biospecimenkey from wagerlab.ix_biospecimen where studykey=194));



-- and 
select distinct tankkey from wagerlab.ix_inv_box where boxid in (1, 12, 12321 etc from previous query)

instead of

select distinct tankkey from wagerlab.ix_inv_tray where 
select distinct boxkey from wagerlab.ix_inv_tray where traykey in
(select distinct traykey from wagerlab.ix_inv_cell where biospecimenkey in (select biospecimenkey from wagerlab.ix_biospecimen where studykey=194));



select * from wagerlab.ix_inv_box where boxkey in 
select boxkey from wagerlab.ix_inv_tray where traykey in
(select distinct traykey from wagerlab.ix_inv_cell where biospecimenkey in (select biospecimenkey from wagerlab.ix_biospecimen where studykey=194);


select * from wagerlab.ix_inv_cell where biospecimenkey = -1; 

-- change cell 601 to point to biospecimenkey 2188706 (new bio id = 501036)
update wagerlab.ix_inv_cell set biospecimenkey = 2188706
where cellkey = 601;

select * from wagerlab.ix_inv_cell where cellkey = 601;


select * from lims.biospecimen where old_id in (select biospecimenkey from wagerlab.ix_biospecimen where studykey=194);

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

-- Allow large byte value for GROUP_CONCAT
SET SESSION group_concat_max_len = 30000;

/*
-- Insert HOSPITAL and REF_DOCTOR as custom biocollection fields
INSERT INTO `study`.`custom_field`
(`NAME`,
`DESCRIPTION`,
`FIELD_TYPE_ID`,
`STUDY_ID`,
`ARK_FUNCTION_ID`,
`UNIT_TYPE_ID`,
`MIN_VALUE`,
`MAX_VALUE`,
`ENCODED_VALUES`,
`MISSING_VALUE`,
`HAS_DATA`,
`CUSTOM_FIELD_LABEL`)
SELECT 'SURGEON' AS NAME, 'NOTE: Encoded values mapped from sortorder in old data' as DESCRIPTION, 
(SELECT ID FROM study.field_type WHERE name = 'CHARACTER') AS FIELD_TYPE,
@STUDYKEY AS STUDY_ID, (SELECT ID FROM study.ark_function WHERE name = 'LIMS_COLLECTION') AS ARK_FUNCTION_ID, NULL AS UNITS_TYPE_ID, NULL MIN_VALUE, NULL MAX_VALUE,
(SELECT (CONCAT(GROUP_CONCAT(CONCAT(FLOOR(SORTORDER),'=',TRIM(VALUE)) ORDER BY SORTORDER SEPARATOR ';'),';'))) AS ENCODED_VALUES,
NULL AS MISSING_VALUE,
0 AS HAS_DATA,
'Surgeon' AS CUSTOM_FIELD_LABEL
FROM wagerlab.IX_LISTOFVALUES 
WHERE TYPE ='REF_DOCTOR' AND DELETED =0 AND STUDYKEY IN (0, @STUDYKEY) AND VALUE IS NOT NULL;

INSERT INTO `study`.`custom_field`
(`NAME`,
`DESCRIPTION`,
`FIELD_TYPE_ID`,
`STUDY_ID`,
`ARK_FUNCTION_ID`,
`UNIT_TYPE_ID`,
`MIN_VALUE`,
`MAX_VALUE`,
`ENCODED_VALUES`,
`MISSING_VALUE`,
`HAS_DATA`,
`CUSTOM_FIELD_LABEL`)
SELECT 'HOSPITAL' AS NAME, 'NOTE: Encoded values mapped from sortorder in old data' as DESCRIPTION, 
(SELECT ID FROM study.field_type WHERE name = 'CHARACTER') AS FIELD_TYPE,
@STUDYKEY AS STUDY_ID, (SELECT ID FROM study.ark_function WHERE name = 'LIMS_COLLECTION') AS ARK_FUNCTION_ID, NULL AS UNITS_TYPE_ID, NULL MIN_VALUE, NULL MAX_VALUE,
(SELECT (CONCAT(GROUP_CONCAT(CONCAT(FLOOR(SORTORDER),'=',TRIM(VALUE)) ORDER BY SORTORDER SEPARATOR ';'),';'))) AS ENCODED_VALUES,
NULL AS MISSING_VALUE,
0 AS HAS_DATA,
'Hospital' AS CUSTOM_FIELD_LABEL
FROM wagerlab.IX_LISTOFVALUES 
WHERE TYPE ='HOSPITAL' AND DELETED =0 AND STUDYKEY IN (0, @STUDYKEY) AND VALUE IS NOT NULL;

-- Insert Custom field display (BioCollection)
INSERT INTO `study`.`custom_field_display`
(`CUSTOM_FIELD_ID`,
`CUSTOM_FIELD_GROUP_ID`,
`SEQUENCE`,
`REQUIRED`,
`REQUIRED_MESSAGE`)
SELECT ID, NULL, ID, 0 AS REQUIRED, NULL
FROM study.custom_field
WHERE study_id = @STUDYKEY
AND ark_function_id = (SELECT ID FROM study.ark_function WHERE name = 'LIMS_COLLECTION')
AND name IN ('HOSPITAL', 'SURGEON');

-- Insert BioCollection Custom fields
INSERT INTO `study`.`custom_field`
(`NAME`,
`DESCRIPTION`,
`FIELD_TYPE_ID`,
`STUDY_ID`,
`ARK_FUNCTION_ID`,
`UNIT_TYPE_ID`,
`MIN_VALUE`,
`MAX_VALUE`,
`ENCODED_VALUES`,
`MISSING_VALUE`,
`HAS_DATA`,
`CUSTOM_FIELD_LABEL`)
SELECT bf.COLUMNNAME AS NAME, IF(bf.LOVTYPE IS NOT NULL, 'NOTE: Encoded values mapped from sortorder in old data', null) as DESCRIPTION, 
(SELECT ID FROM study.field_type WHERE name = IF(bft.TYPENAME = 'string', 'CHARACTER', IF(bft.TYPENAME = 'number', 'NUMBER', 'DATE'))) AS FIELD_TYPE,
@STUDYKEY AS STUDY_ID, (SELECT ID FROM study.ark_function WHERE name = 'LIMS_COLLECTION') AS ARK_FUNCTION_ID, NULL AS UNITS_TYPE_ID, NULL MIN_VALUE, NULL MAX_VALUE,
(
SELECT CONCAT(GROUP_CONCAT(CONCAT(FLOOR(SORTORDER),'=',TRIM(VALUE)) ORDER BY SORTORDER SEPARATOR ';'),';') AS ENCODED_VALUES
FROM wagerlab.IX_LISTOFVALUES 
WHERE TYPE = bf.LOVTYPE
GROUP BY TYPE
) AS ENCODED_VALUES,
NULL AS MISSING_VALUE,
0 AS HAS_DATA,
bf.FIELDNAME AS CUSTOM_FIELD_LABEL
FROM wagerlab.IX_BIODATA_FIELD bf, wagerlab.IX_BIODATA_TYPES bft, wagerlab.IX_BIODATA_FIELD_GROUP bfg, wagerlab.IX_BIODATA_GROUP bg
WHERE bfg.GROUPKEY = bg.GROUPKEY
AND bfg.FIELDKEY = bf.FIELDKEY
AND bf.DOMAIN = bg.DOMAIN
AND bf.TYPEKEY = bft.TYPEKEY
AND bg.GROUP_NAME like 'WARTN%'
AND bg.DOMAIN = 'ADMISSIONS'
ORDER BY bfg.POSITION;

-- Insert Custom field display (BioCollection)
INSERT INTO `study`.`custom_field_display`
(`CUSTOM_FIELD_ID`,
`CUSTOM_FIELD_GROUP_ID`,
`SEQUENCE`,
`REQUIRED`,
`REQUIRED_MESSAGE`)
SELECT ID, NULL, ID, 0 AS REQUIRED, NULL
FROM study.custom_field
WHERE study_id = @STUDYKEY
AND ark_function_id = (SELECT ID FROM study.ark_function WHERE name = 'LIMS_COLLECTION');

-- HOSPITAL and SURGEON data from IX_ADMISSIONS
INSERT INTO `lims`.`biocollection_custom_field_data`
(`BIO_COLLECTION_ID`,
`CUSTOM_FIELD_DISPLAY_ID`,
`TEXT_DATA_VALUE`,
`DATE_DATA_VALUE`,
`NUMBER_DATA_VALUE`,
`ERROR_DATA_VALUE`)
SELECT bc.id AS BIO_COLLECTION_ID, cfd.id AS CUSTOM_FIELD_DISPLAY_ID, SUBSTRING_INDEX(TRIM(TRAILING  SUBSTRING(cf.encoded_values, INSTR(cf.ENCODED_VALUES, concat('=', bc.HOSPITAL, ';'))) FROM cf.ENCODED_VALUES), ';', -1) AS TEXT_DATA_VALUE, NULL AS `DATE_DATA_VALUE`, NULL AS`NUMBER_DATA_VALUE`, NULL AS `ERROR_DATA_VALUE`
FROM study.custom_field cf, study.custom_field_display cfd, lims.biocollection bc
WHERE cf.id = cfd.custom_field_id
AND cf.NAME = 'HOSPITAL'
AND cf.study_id IN (SELECT id FROM study.study WHERE parent_id = @STUDYKEY)
AND bc.study_id IN (SELECT id FROM study.study WHERE parent_id = @STUDYKEY)
AND bc.HOSPITAL IS NOT NULL;

INSERT INTO `lims`.`biocollection_custom_field_data`
(`BIO_COLLECTION_ID`,
`CUSTOM_FIELD_DISPLAY_ID`,
`TEXT_DATA_VALUE`,
`DATE_DATA_VALUE`,
`NUMBER_DATA_VALUE`,
`ERROR_DATA_VALUE`)
SELECT bc.id AS BIO_COLLECTION_ID, cfd.id AS CUSTOM_FIELD_DISPLAY_ID, SUBSTRING_INDEX(TRIM(TRAILING  SUBSTRING(cf.encoded_values, INSTR(cf.ENCODED_VALUES, concat('=', bc.REF_DOCTOR, ';'))) FROM cf.ENCODED_VALUES), ';', -1) AS TEXT_DATA_VALUE, NULL AS `DATE_DATA_VALUE`, NULL AS`NUMBER_DATA_VALUE`, NULL AS `ERROR_DATA_VALUE`
FROM study.custom_field cf, study.custom_field_display cfd, lims.biocollection bc
WHERE cf.id = cfd.custom_field_id
AND cf.NAME = 'SURGEON'
AND cf.study_id IN (SELECT id FROM study.study WHERE parent_id = @STUDYKEY)
AND bc.study_id IN (SELECT id FROM study.study WHERE parent_id = @STUDYKEY)
AND bc.REF_DOCTOR IS NOT NULL;

-- Normal text/character data
INSERT INTO `lims`.`biocollection_custom_field_data`
(`BIO_COLLECTION_ID`,
`CUSTOM_FIELD_DISPLAY_ID`,
`TEXT_DATA_VALUE`,
`DATE_DATA_VALUE`,
`NUMBER_DATA_VALUE`,
`ERROR_DATA_VALUE`)
SELECT bc.id AS BIO_COLLECTION_ID, cfd.id AS CUSTOM_FIELD_DISPLAY_ID, bd.STRING_VALUE AS TEXT_DATA_VALUE, NULL AS `DATE_DATA_VALUE`, NULL AS`NUMBER_DATA_VALUE`, NULL AS `ERROR_DATA_VALUE`
FROM wagerlab.IX_BIODATA bd, wagerlab.IX_BIODATA_FIELD bf, wagerlab.IX_BIODATA_TYPES bft, wagerlab.IX_BIODATA_FIELD_GROUP bfg, wagerlab.IX_BIODATA_GROUP bg, wagerlab.IX_ADMISSIONS adm,study.custom_field cf, study.custom_field_display cfd,
lims.biocollection bc
WHERE bfg.GROUPKEY = bg.GROUPKEY
AND bfg.FIELDKEY = bf.FIELDKEY
AND bf.DOMAIN = bg.DOMAIN
AND bf.TYPEKEY = bft.TYPEKEY
AND bg.GROUP_NAME like 'WARTN%'
AND bg.DOMAIN = 'ADMISSIONS'
AND bd.FIELDKEY = bf.FIELDKEY
AND bd.DOMAINKEY = adm.ADMISSIONKEY
AND adm.DELETED = 0
AND cf.study_id = @STUDYKEY
AND ark_function_id = (SELECT ID FROM study.ark_function WHERE name = 'LIMS_COLLECTION')
AND cf.id = cfd.custom_field_id
AND cf.NAME = bf.COLUMNNAME
AND bc.NAME = adm.ADMISSIONID
AND bc.STUDY_ID = adm.COLLECTIONGROUPKEY
AND bf.LOVTYPE IS NULL
AND STRING_VALUE IS NOT NULL;

-- Drop-down data as migrated into encoded values
INSERT INTO `lims`.`biocollection_custom_field_data`
(`BIO_COLLECTION_ID`,
`CUSTOM_FIELD_DISPLAY_ID`,
`TEXT_DATA_VALUE`,
`DATE_DATA_VALUE`,
`NUMBER_DATA_VALUE`,
`ERROR_DATA_VALUE`)
SELECT bc.id AS BIO_COLLECTION_ID, cfd.id AS CUSTOM_FIELD_DISPLAY_ID, SUBSTRING_INDEX(TRIM(TRAILING  SUBSTRING(cf.encoded_values, INSTR(cf.ENCODED_VALUES, concat('=', bd.STRING_VALUE, ';'))) FROM cf.ENCODED_VALUES), ';', -1) AS TEXT_DATA_VALUE, NULL AS `DATE_DATA_VALUE`, NULL AS`NUMBER_DATA_VALUE`, NULL AS `ERROR_DATA_VALUE`
FROM wagerlab.IX_BIODATA bd, wagerlab.IX_BIODATA_FIELD bf, wagerlab.IX_BIODATA_TYPES bft, wagerlab.IX_BIODATA_FIELD_GROUP bfg, wagerlab.IX_BIODATA_GROUP bg, wagerlab.IX_ADMISSIONS adm,study.custom_field cf, study.custom_field_display cfd,
lims.biocollection bc
WHERE bfg.GROUPKEY = bg.GROUPKEY
AND bfg.FIELDKEY = bf.FIELDKEY
AND bf.DOMAIN = bg.DOMAIN
AND bf.TYPEKEY = bft.TYPEKEY
AND bg.GROUP_NAME like 'WARTN%'
AND bg.DOMAIN = 'ADMISSIONS'
AND bd.FIELDKEY = bf.FIELDKEY
AND bd.DOMAINKEY = adm.ADMISSIONKEY
AND adm.DELETED = 0
AND cf.study_id = @STUDYKEY
AND ark_function_id = (SELECT ID FROM study.ark_function WHERE name = 'LIMS_COLLECTION')
AND cf.id = cfd.custom_field_id
AND cf.NAME = bf.COLUMNNAME
AND bc.NAME = adm.ADMISSIONID
AND bc.STUDY_ID = adm.COLLECTIONGROUPKEY
AND bf.LOVTYPE IS NOT NULL
AND STRING_VALUE IS NOT NULL;
 
-- Dates
INSERT INTO `lims`.`biocollection_custom_field_data`
(`BIO_COLLECTION_ID`,
`CUSTOM_FIELD_DISPLAY_ID`,
`TEXT_DATA_VALUE`,
`DATE_DATA_VALUE`,
`NUMBER_DATA_VALUE`,
`ERROR_DATA_VALUE`)
SELECT bc.id AS BIO_COLLECTION_ID, cfd.id AS CUSTOM_FIELD_DISPLAY_ID, NULL AS TEXT_DATA_VALUE, bd.DATE_VALUE AS `DATE_DATA_VALUE`, NULL AS`NUMBER_DATA_VALUE`, NULL AS `ERROR_DATA_VALUE`
FROM wagerlab.IX_BIODATA bd, wagerlab.IX_BIODATA_FIELD bf, wagerlab.IX_BIODATA_TYPES bft, wagerlab.IX_BIODATA_FIELD_GROUP bfg, wagerlab.IX_BIODATA_GROUP bg, wagerlab.IX_ADMISSIONS adm,study.custom_field cf, study.custom_field_display cfd,
lims.biocollection bc
WHERE bfg.GROUPKEY = bg.GROUPKEY
AND bfg.FIELDKEY = bf.FIELDKEY
AND bf.DOMAIN = bg.DOMAIN
AND bf.TYPEKEY = bft.TYPEKEY
AND bg.GROUP_NAME like 'WARTN%'
AND bg.DOMAIN = 'ADMISSIONS'
AND bd.FIELDKEY = bf.FIELDKEY
AND bd.DOMAINKEY = adm.ADMISSIONKEY
AND adm.DELETED = 0
AND cf.study_id = @STUDYKEY
AND ark_function_id = (SELECT ID FROM study.ark_function WHERE name = 'LIMS_COLLECTION')
AND cf.id = cfd.custom_field_id
AND cf.NAME = bf.COLUMNNAME
AND bc.NAME = adm.ADMISSIONID
AND bc.STUDY_ID = adm.COLLECTIONGROUPKEY
AND DATE_VALUE IS NOT NULL;

-- Insert Biospecimen Custom fields
INSERT INTO `study`.`custom_field`
(`NAME`,
`DESCRIPTION`,
`FIELD_TYPE_ID`,
`STUDY_ID`,
`ARK_FUNCTION_ID`,
`UNIT_TYPE_ID`,
`MIN_VALUE`,
`MAX_VALUE`,
`ENCODED_VALUES`,
`MISSING_VALUE`,
`HAS_DATA`,
`CUSTOM_FIELD_LABEL`)
SELECT bf.COLUMNNAME AS NAME, IF(bf.LOVTYPE IS NOT NULL, 'NOTE: Encoded values mapped from sortorder in old data', null) as DESCRIPTION, 
(SELECT ID FROM study.field_type WHERE name = IF(bft.TYPENAME = 'string', 'CHARACTER', IF(bft.TYPENAME = 'number', 'NUMBER', 'DATE'))) AS FIELD_TYPE,
@STUDYKEY AS STUDY_ID, (SELECT ID FROM study.ark_function WHERE name = 'BIOSPECIMEN') AS ARK_FUNCTION_ID, NULL AS UNIT_TYPE_ID, NULL MIN_VALUE, NULL MAX_VALUE,
(
SELECT CONCAT(GROUP_CONCAT(CONCAT(FLOOR(SORTORDER),'=',TRIM(VALUE)) ORDER BY SORTORDER SEPARATOR ';'),';') AS ENCODED_VALUES
FROM wagerlab.IX_LISTOFVALUES 
WHERE TYPE = bf.LOVTYPE
GROUP BY TYPE
) AS ENCODED_VALUES,
NULL AS MISSING_VALUE,
0 AS HAS_DATA,
bf.FIELDNAME AS CUSTOM_FIELD_LABEL
FROM wagerlab.IX_BIODATA_FIELD bf, wagerlab.IX_BIODATA_TYPES bft, wagerlab.IX_BIODATA_FIELD_GROUP bfg, wagerlab.IX_BIODATA_GROUP bg
WHERE bfg.GROUPKEY = bg.GROUPKEY
AND bfg.FIELDKEY = bf.FIELDKEY
AND bf.DOMAIN = bg.DOMAIN
AND bf.TYPEKEY = bft.TYPEKEY
AND bg.GROUP_NAME like (@STUDYNAME || '%')
AND bg.DOMAIN = 'BIOSPECIMEN'
ORDER BY bfg.POSITION;

-- Insert Custom field display (Biospecimen)
INSERT INTO `study`.`custom_field_display`
(`CUSTOM_FIELD_ID`,
`CUSTOM_FIELD_GROUP_ID`,
`SEQUENCE`,
`REQUIRED`,
`REQUIRED_MESSAGE`)
SELECT ID, NULL, ID, 0 AS REQUIRED, NULL
FROM study.custom_field
WHERE study_id = @STUDYKEY
AND ark_function_id = (SELECT ID FROM study.ark_function WHERE name = 'BIOSPECIMEN');

-- Characters
INSERT INTO `lims`.`biospecimen_custom_field_data`
(`BIOSPECIMEN_ID`,
`CUSTOM_FIELD_DISPLAY_ID`,
`TEXT_DATA_VALUE`,
`DATE_DATA_VALUE`,
`NUMBER_DATA_VALUE`,
`ERROR_DATA_VALUE`)
SELECT bs.id AS BIOSPECIMEN_ID, cfd.id AS CUSTOM_FIELD_DISPLAY_ID, SUBSTRING_INDEX(TRIM(TRAILING  SUBSTRING(cf.encoded_values, INSTR(cf.ENCODED_VALUES, concat('=', bd.STRING_VALUE, ';'))) FROM cf.ENCODED_VALUES), ';', -1) AS TEXT_DATA_VALUE, NULL AS `DATE_DATA_VALUE`, NULL AS`NUMBER_DATA_VALUE`, NULL AS `ERROR_DATA_VALUE`
FROM wagerlab.IX_BIODATA bd, wagerlab.IX_BIODATA_FIELD bf, wagerlab.IX_BIODATA_TYPES bft, wagerlab.IX_BIODATA_FIELD_GROUP bfg, wagerlab.IX_BIODATA_GROUP bg, wagerlab.IX_BIOSPECIMEN bio,study.custom_field cf, study.custom_field_display cfd,
lims.biospecimen bs
WHERE bfg.GROUPKEY = bg.GROUPKEY
AND bfg.FIELDKEY = bf.FIELDKEY
AND bf.DOMAIN = bg.DOMAIN
AND bf.TYPEKEY = bft.TYPEKEY
AND bg.GROUP_NAME like (@STUDYNAME || '%')
AND bg.DOMAIN = 'BIOSPECIMEN'
AND bd.FIELDKEY = bf.FIELDKEY
AND bd.DOMAINKEY = bio.BIOSPECIMENKEY
AND bio.DELETED = 0
AND cf.study_id = @STUDYKEY
AND ark_function_id = (SELECT ID FROM study.ark_function WHERE name = 'BIOSPECIMEN')
AND cf.id = cfd.custom_field_id
AND cf.NAME = bf.COLUMNNAME
AND STRING_VALUE IS NOT NULL
AND bf.LOVTYPE IS NOT NULL
AND bs.OLD_ID = bio.BIOSPECIMENKEY;
 
-- Dates
INSERT INTO `lims`.`biospecimen_custom_field_data`
(`BIOSPECIMEN_ID`,
`CUSTOM_FIELD_DISPLAY_ID`,
`TEXT_DATA_VALUE`,
`DATE_DATA_VALUE`,
`NUMBER_DATA_VALUE`,
`ERROR_DATA_VALUE`)
SELECT bs.id AS BIOSPECIMEN_ID, cfd.id AS CUSTOM_FIELD_DISPLAY_ID, NULL AS TEXT_DATA_VALUE, bd.DATE_VALUE AS `DATE_DATA_VALUE`, NULL AS`NUMBER_DATA_VALUE`, NULL AS `ERROR_DATA_VALUE`
FROM wagerlab.IX_BIODATA bd, wagerlab.IX_BIODATA_FIELD bf, wagerlab.IX_BIODATA_TYPES bft, wagerlab.IX_BIODATA_FIELD_GROUP bfg, wagerlab.IX_BIODATA_GROUP bg, wagerlab.IX_BIOSPECIMEN bio,study.custom_field cf, study.custom_field_display cfd,
lims.biospecimen bs
WHERE bfg.GROUPKEY = bg.GROUPKEY
AND bfg.FIELDKEY = bf.FIELDKEY
AND bf.DOMAIN = bg.DOMAIN
AND bf.TYPEKEY = bft.TYPEKEY
AND bg.GROUP_NAME like (@STUDYNAME || '%')
AND bg.DOMAIN = 'BIOSPECIMEN'
AND bd.FIELDKEY = bf.FIELDKEY
AND bd.DOMAINKEY = bio.BIOSPECIMENKEY
AND bio.DELETED = 0
AND cf.study_id = @STUDYKEY
AND ark_function_id = (SELECT ID FROM study.ark_function WHERE name = 'BIOSPECIMEN')
AND cf.id = cfd.custom_field_id
AND cf.NAME = bf.COLUMNNAME
AND DATE_VALUE IS NOT NULL
AND bs.OLD_ID = bio.BIOSPECIMENKEY;
*/

-- BioCollection pattern
-- Trav : TODO CREATE PARAMS
DELETE FROM `lims`.`biocollectionuid_template` 
WHERE
    STUDY_ID = @STUDYKEY;
INSERT INTO `lims`.`biocollectionuid_template`
(`STUDY_ID`,
`BIOCOLLECTIONUID_PREFIX`,
`BIOCOLLECTIONUID_TOKEN_ID`,
`BIOCOLLECTIONUID_PADCHAR_ID`)
VALUES (@STUDYKEY, @BIOCOLLECTIONUID_PREFIX, @BIOCOLLECTIONUID_TOKEN_ID, @BIOCOLLECTIONUID_PADCHAR_ID);

-- Set base sequence count
-- Trav : TODO CREATE PARAMS
DELETE FROM `lims`.`biocollectionuid_sequence` 
WHERE
    `STUDY_NAME_ID` = @STUDYNAME;
INSERT INTO `lims`.`biocollectionuid_sequence`
(`STUDY_NAME_ID`,
`UID_SEQUENCE`,
`INSERT_LOCK`)
VALUES
(
@STUDYNAME,
(SELECT MAX(TRIM(@BIOCOLLECTIONUID_PREFIX FROM name)) + 1 -- NEED TO CHECK THIS!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
FROM lims.biocollection
WHERE study_id IN (SELECT id FROM study.study WHERE parent_id = @STUDYKEY)
AND name like concat(@BIOCOLLECTIONUID_PREFIX, '%')),  -- 'TN%'),
0
);



-- Biospecimen pattern
-- Trav : TODO CREATE PARAMS
DELETE FROM `lims`.`biospecimenuid_template` 
WHERE
    `STUDY_ID` = @STUDYKEY;
INSERT INTO `lims`.`biospecimenuid_template`
(`STUDY_ID`,
`BIOSPECIMENUID_PREFIX`,
`BIOSPECIMENUID_TOKEN_ID`,
`BIOSPECIMENUID_PADCHAR_ID`)
VALUES (@STUDYKEY, @BIOSPECIMENUID_PREFIX, @BIOSPECIMENUID_TOKEN_ID, @BIOSPECIMENUID_PADCHAR_ID); -- NEED TO CHECK THIS!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

select @STUDYKEY, @BIOSPECIMENUID_PREFIX, @BIOSPECIMENUID_TOKEN_ID, @BIOSPECIMENUID_PADCHAR_ID;


-- Set base sequence count
DELETE FROM `lims`.`biospecimenuid_sequence` 
WHERE
    `STUDY_NAME_ID` = @STUDYNAME;
INSERT INTO `lims`.`biospecimenuid_sequence`
(`STUDY_NAME_ID`,
`UID_SEQUENCE`,
`INSERT_LOCK`)
VALUES
(
@STUDYNAME,
(SELECT MAX(ID) FROM lims.biospecimen WHERE study_id IN (SELECT ID FROM study.study WHERE parent_id = @STUDYKEY)),
0
);

-- Assign modules to all studies
-- Trav : In each study analyze which modules are needed - or select them all and add the appropriate for max coverage.
DELETE FROM `study`.`link_study_arkmodule` 
WHERE
    study_id IN (SELECT 
        ID
    FROM
        study.study
    
    WHERE
        parent_id = @STUDYKEY);
INSERT INTO `study`.`link_study_arkmodule`
(`STUDY_ID`,
`ARK_MODULE_ID`)
SELECT s.id as study_id, a.id as ark_module_id
FROM study.study s, study.ark_module a
WHERE s.parent_id = @STUDYKEY
AND a.name IN ('Study', 'Subject', 'Phenotypic', 'LIMS')
ORDER BY s.id, a.id;

-- SET starting subject increment
-- TRAV Params
UPDATE `study`.`subjectuid_sequence` 
SET 
    `UID_SEQUENCE` = (SELECT 
            TRIM('0' FROM TRIM(LEADING concat(@SUBJECT_PREFIX, @BIOCOLLECTIONUID_TOKEN_DASH) FROM ( SELECT         -- 'WTN-' FROM (SELECT 
                                max(subject_uid) maxid
                            FROM
                                study.link_subject_study
                            WHERE
                                study_id = @STUDYKEY)))
        )
WHERE
    `STUDY_NAME_ID` = @STUDYNAME;

select * from lims.biospecimen;


-- There is a difference in wager and ark units 
-- we are holding some biospcimens in ml type...move to mL type like the rest
update biospecimen
set unit_id = 17 -- current ark ie mL
where unit_id = 101 -- wager ml
and study_id = @STUDYKEY;

/*
select * from lims.biospecimen
where unit_id = 101 -- wager ml
and study_id = @STUDYKEY;
select * from lims.unit;
*/
-- select * from lims.biospecimen  where study_id = 194


-- update all transactions to have the units of their parents
-- script for this exists in some past update script in svn

/*
add units to transaction - update existing data to ensure unit matches that of the parent;
logic needs to make sure every transaction gets the unit of the biospecimen if it has one (app logic doesnt permit no unit but...), else use that of the parent, else use that of the grandparent, etc?

TODO:  Look at this when re-running

update lims.bio_transaction t 
inner join lims.biospecimen b on
    t.biospecimen_id = b.id
	and b.study_id = @study_id
	and (t.UNIT_id is null or t.unit_id = 0)
set
    t.unit_id = b.unit_id ;


select count(*) from lims.bio_transaction t 
 inner join lims.biospecimen b on
    t.biospecimen_id = b.id
	and b.study_id = @STUDYKEY
	and (t.UNIT_id is null or t.unit_id = 0);

select count(*) from  lims.bio_transaction t -- where  t.id = 0;
*/

select distinct hospital from wagerlab.ix_admissions;

select distinct hospital from lims.biocollection where study_id  = 194;


