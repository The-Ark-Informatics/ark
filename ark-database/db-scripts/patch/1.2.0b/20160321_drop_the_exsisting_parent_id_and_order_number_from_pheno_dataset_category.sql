use `pheno`;

set foreign_key_checks = 0;

alter table pheno_dataset_category drop ORDER_NUMBER;
alter table pheno_dataset_category drop PARENT_ID;

set foreign_key_checks = 1;

commit;
