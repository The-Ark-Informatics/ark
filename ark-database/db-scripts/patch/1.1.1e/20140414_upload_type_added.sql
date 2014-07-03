-- SELECT * FROM study.upload_type;


-- add a new uploader type;

INSERT INTO `study`.`upload_type` (`NAME`, `DESCRIPTION`, `ARK_MODULE_ID`) VALUES ('Biospecimen Location Updater', 'Upload the locations of Biospecimen only', '5');


-- INSERT INTO `study`.`upload_type` (`NAME`, `DESCRIPTION`, `ARK_MODULE_ID`) VALUES ('Biospecimen Uploader - Under Development', 'Upload the Biospecimen.  Only use supervised', '5');
/*
note that upon review the following two removals were also added given that paul asked we stop dev on these in 2013 - until such a time that we recommence dev, do not have this uploader in place
DELETE FROM `study`.`upload_type` WHERE name in ('Biocollection Custom Data', 'Biospecimen Custom Data');
*/


select * from lims.unit where name = 'ml';

select * from lims.biospecimen where unit_id = 17;

select * from lims.bio_transaction where unit_id = 17;


