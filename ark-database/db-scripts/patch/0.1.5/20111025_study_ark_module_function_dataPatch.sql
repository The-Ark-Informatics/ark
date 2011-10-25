USE study;

-- Remove the Pheno Data Uploader/Importer function from Pheno module
-- (Decision on 24 Oct 2011: disable the pheno data uploader for 0.1.6 till it can be fully converted)
DELETE amf FROM ark_module_function amf
INNER JOIN ark_function af
ON amf.ark_function_id = af.id
WHERE af.name = 'FIELD_DATA_UPLOAD';

-- Confirm that the following SQL should return nothing
SELECT * FROM study.ark_module_function amf,
study.ark_function af
WHERE amf.ark_function_id = af.id
AND af.name = 'FIELD_DATA_UPLOAD';

