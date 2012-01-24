USE lims;

INSERT INTO barcode_printer (`STUDY_ID`,`NAME`,`DESCRIPTION`,`LOCATION`,`HOST`,`PORT`)
SELECT ID, 'zebra','zebra TLP 2844','Office','130.95.56.99',9100
FROM study.study;

INSERT INTO barcode_printer (`STUDY_ID`,`NAME`,`DESCRIPTION`,`LOCATION`,`HOST`,`PORT`)
SELECT ID, 'brady_bbp_11','Brady BBP 11','Office','130.95.56.99',9100
FROM study.study;
