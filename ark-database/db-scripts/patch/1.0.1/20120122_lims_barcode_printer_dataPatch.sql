USE lims;

DELETE FROM barcode_printer WHERE ID > 0;
INSERT INTO barcode_printer (`STUDY_ID`,`NAME`,`DESCRIPTION`,`LOCATION`,`HOST`,`PORT`)
SELECT ID, 'zebra','zebra TLP 2844','Office','127.0.0.1',9100
FROM study.study
WHERE ID IN (SELECT DISTINCT study_id FROM `study`.`ark_user_role` WHERE ark_module_id=5 AND study_id IS NOT NULL);

INSERT INTO barcode_printer (`STUDY_ID`,`NAME`,`DESCRIPTION`,`LOCATION`,`HOST`,`PORT`)
SELECT ID, 'brady_bbp_11','Brady BBP 11','Office','127.0.0.1',9100
FROM study.study
WHERE ID IN (SELECT DISTINCT study_id FROM `study`.`ark_user_role` WHERE ark_module_id=5 AND study_id IS NOT NULL);
