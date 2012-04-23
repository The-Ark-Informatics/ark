USE study;

-- Ensures the module names are unique
ALTER TABLE `study`.`ark_module` 
ADD UNIQUE INDEX `ARK_MODULE_NAME_UNIQUE` (`NAME` ASC) ;
