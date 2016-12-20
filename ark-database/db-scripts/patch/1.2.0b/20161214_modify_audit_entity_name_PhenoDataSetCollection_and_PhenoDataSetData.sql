use audit;

-- Pick the current entity ids from the name
SET @entityIDPhenoDataSetCollection=(SELECT ID FROM audit_entity where class_identifier='au.org.theark.core.model.pheno.entity.PhenoCollection');
SET @entityIDPhenoDataSetData=(SELECT ID FROM audit_entity where class_identifier='au.org.theark.core.model.pheno.entity.PhenoData');

-- Update the names

UPDATE audit_entity SET class_identifier='au.org.theark.core.model.pheno.entity.PhenoDataSetCollection', name='Pheno Data Set Collection' where id=@entityIDPhenoDataSetCollection;
UPDATE audit_entity SET class_identifier='au.org.theark.core.model.pheno.entity.PhenoDataSetData', name='Pheno Data Set Data' where id=@entityIDPhenoDataSetData;
