-- SELECT * FROM study.upload_type;


-- add a new uploader type;

INSERT INTO `study`.`upload_type` (`NAME`, `DESCRIPTION`, `ARK_MODULE_ID`) VALUES ('Biospecimen Location Updater', 'Upload the locations of Biospecimen only', '5');



select * from lims.unit where name = 'ml';

select * from lims.biospecimen where unit_id = 17;

select * from lims.bio_transaction where unit_id = 17;


