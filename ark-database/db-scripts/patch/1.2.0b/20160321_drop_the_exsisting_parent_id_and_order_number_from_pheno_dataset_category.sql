use 'pheno';

alter table pheno_dataset_category drop ORDER_NUMBER;
alter table pheno_dataset_category drop FOREIGN KEY FK_CUSTOMFIELDCATEGORY_PARENT_ID;
alter table pheno_dataset_category drop PARENT_ID;
