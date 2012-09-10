ALTER TABLE `study`.`study` ADD COLUMN `AUTO_GENERATE_BIOSPECIMENUID` TINYINT NOT NULL DEFAULT 0  AFTER `PARENT_ID` , ADD COLUMN `AUTO_GENERATE_BIOCOLLECTIONUID` TINYINT NOT NULL DEFAULT 0  AFTER `AUTO_GENERATE_BIOSPECIMENUID` ;

-- now that we have the new structure, need to change the current data to guess autogenerate based on existing data.  So if they have the pattern, then just give them auto-generate.  It's a good enough guess for now as that seems to be how the app logic is guessing.
update study.study s
set auto_generate_biocollectionuid = 1
where s.id in (select study_id from biocollection)
and s.id in (select study_id from lims.biocollectionuid_template);

update study.study s
set auto_generate_biospecimenuid = 1
where s.id in (select study_id from biospecimen)
and s.id in (select study_id from lims.biospecimenuid_template);

