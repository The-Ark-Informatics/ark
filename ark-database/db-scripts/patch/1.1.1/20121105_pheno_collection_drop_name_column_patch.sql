UPDATE pheno.pheno_collection
set DESCRIPTION=NAME;

ALTER TABLE `pheno`.`pheno_collection` DROP COLUMN `NAME` ;
