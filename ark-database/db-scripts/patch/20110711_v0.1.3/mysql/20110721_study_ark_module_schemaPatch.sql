USE study;

-- Ensures the module names are unique
ALTER TABLE `study`.`ark_module` 
ADD UNIQUE INDEX `NAME_UNIQUE` (`NAME` ASC) ;
