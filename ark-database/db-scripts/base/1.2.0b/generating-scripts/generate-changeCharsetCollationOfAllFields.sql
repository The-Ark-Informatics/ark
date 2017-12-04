-- copy the results of the following query as an .sql patch for your version of The Ark
SELECT CONCAT('ALTER TABLE `', t.`TABLE_SCHEMA`, '`.`', t.`TABLE_NAME`,   '` MODIFY `', t.`COLUMN_NAME`, '` ', t.`COLUMN_TYPE`,  ' CHARACTER SET utf8 COLLATE utf8_general_ci;') as stmt 
FROM `information_schema`.`COLUMNS` t
WHERE 1
and t.`data_type` in ('varchar', 'text', 'mediumtext', 'longtext')
and t.character_set_name='latin1'
AND t.`TABLE_SCHEMA`  in ('study', 'admin', 'audit', 'lims', 'pheno', 'reporting','calendar','config','disease','geno','spark','sys') ORDER BY 1;
