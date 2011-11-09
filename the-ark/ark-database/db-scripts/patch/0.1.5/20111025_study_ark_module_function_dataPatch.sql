USE study;

-- Remove the Pheno Data Uploader/Importer function from Pheno module
-- (Decision on 24 Oct 2011: disable the pheno data uploader for 0.1.6 till it can be fully converted)
DELETE amf 
FROM study.ark_module_function amf
INNER JOIN study.ark_function af
ON amf.ark_function_id = af.id
INNER JOIN study.ark_module am
ON amf.ark_module_id = am.id
WHERE af.name = 'FIELD_DATA_UPLOAD'
AND am.name = 'phenotypic';

-- Confirm that the following SQL should return nothing
SELECT * 
FROM study.ark_module_function amf,
study.ark_function af,
study.ark_module am
WHERE amf.ark_function_id = af.id
AND amf.ark_module_id = am.id
AND af.name = 'FIELD_DATA_UPLOAD'
AND am.name = 'phenotypic';

-- Remove the Pheno Summary Tab function from Pheno module
-- (Decision on 2 Nov 2011: disable the pheno summary stats in 0.1.6 till it can be fully converted)
DELETE amf 
FROM study.ark_module_function amf
INNER JOIN study.ark_function af
ON amf.ark_function_id = af.id
INNER JOIN study.ark_module am
ON amf.ark_module_id = am.id
WHERE af.name = 'SUMMARY'
AND am.name = 'phenotypic';

-- Confirm that the following SQL should return nothing
SELECT * 
FROM study.ark_module_function amf,
study.ark_function af,
study.ark_module am
WHERE amf.ark_function_id = af.id
AND amf.ark_module_id = am.id
AND af.name = 'SUMMARY'
AND am.name = 'phenotypic';

