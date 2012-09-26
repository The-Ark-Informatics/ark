USE `ccia-live-28July`;

-- SITES
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
FROM ix_inv_site;

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
FROM ix_inv_tank t, ix_inv_site s, lims.inv_site lims_site
WHERE t.SITEKEY = s.SITEKEY
AND s.NAME = lims_site.NAME;

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
FROM ix_inv_box b, ix_inv_tank t, lims.inv_freezer f
WHERE t.TANKKEY = b.TANKKEY
AND t.NAME = f.NAME;

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
    (SELECT 
        `crt`.`ID`
    FROM
        `lims`.`inv_col_row_type` `crt`
    WHERE
        `NAME` = `t`.`COLNOTYPE`) as `COLNOTYPE_ID`,
    (SELECT 
        `crt`.`ID`
    FROM
        `lims`.`inv_col_row_type` `crt`
    WHERE
        `NAME` = `t`.`ROWNOTYPE`) as `ROWNOTYPE_ID`,
`t`.`TRAYKEY` as OLD_ID
FROM
    `ccia-live-28july`.`ix_inv_tray` t, 
    `ccia-live-28july`.`ix_inv_box` b
WHERE `t`.`BOXKEY` = `b`.`BOXKEY`;

-- Insert a fake biospecimen for cell merging
INSERT INTO lims.biospecimen (id, study_id, sample_type_id, biocollection, old_id, treatment_type_id, deleted)
VALUES ('-1', (SELECT id FROM study.study WHERE name = 'CCIA'), (SELECT min(id) FROM study.link_subject_study WHERE study_id =(SELECT id FROM study.study WHERE name = 'CCIA')), '0', (SELECT min(id) FROM lims.biocollection WHERE study_id =(SELECT id FROM study.study WHERE name = 'CCIA')), '-1', '1', '-1');

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
    `c`.`COLNO`,
    `c`.`ROWNO`,
bio.id as biospecimen_id,
`c`.`BIOSPECIMENKEY`,
    (CASE
        WHEN `c`.`BIOSPECIMENKEY` > -1 THEN 2
        ELSE 1
    END) as `STATUS`
FROM
    `ix_inv_cell` `c`,
    `ix_inv_tray` `t`,
	lims.inv_box b,
	lims.biospecimen bio
WHERE
    `c`.`TRAYKEY` = `t`.`TRAYKEY`
AND b.OLD_ID = `t`.`TRAYKEY`
AND bio.OLD_ID = c.biospecimenkey
AND bio.study_id = (SELECT id FROM study.study WHERE name = 'CCIA');

-- Set -1 biospecimen_id to nulls
UPDATE `lims`.`inv_cell` SET biospecimen_id = null WHERE biospecimen_id = -1;
-- remove fake biospecimen
DELETE FROM lims.biospecimen WHERE id = -1;