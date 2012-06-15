USE lims;
INSERT INTO `lims`.`barcode_printer`
(`STUDY_ID`,
`NAME`,
`DESCRIPTION`,
`HOST`,
`PORT`)
SELECT s.id, p.name, p.description, 'XXX.XXX.XXX.XXX' AS host, p.port 
FROM `lims`.`barcode_printer` p, study.study s
WHERE s.id >1;
