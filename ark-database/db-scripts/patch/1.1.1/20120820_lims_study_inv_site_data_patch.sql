INSERT INTO lims.study_inv_site (study_id, inv_site_id)
SELECT study_id, id FROM lims.inv_site;