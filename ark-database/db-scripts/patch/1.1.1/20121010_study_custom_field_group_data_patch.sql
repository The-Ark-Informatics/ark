-- For ARK-849. Data uploaded from file, but logic implies new subject datasets cannot be created for unpublished custom field groups.
-- This patch simply amends the custom field groups with data associated to published
UPDATE study.custom_field_group
SET published = 1
WHERE id IN (SELECT DISTINCT cfg.id
FROM pheno.pheno_data p, study.custom_field_display cfd, study.custom_field_group cfg
WHERE p.custom_field_display_id = cfd.id
AND cfd.custom_field_group_id = cfg.id
AND cfg.published = 0);