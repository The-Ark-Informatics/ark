
-- stops repeat locations for a given biospecimen
-- alter table lims.inv_cell 
-- add unique index (biospecimen_id);
-- of course this can't run yet in prod because it is not allowing it because WE DIDNT HAVE THIS...so logically there are some duplications;

ALTER TABLE `lims`.`inv_cell` 
ADD UNIQUE INDEX `uq_inv_cell_biospecimen` (`BIOSPECIMEN_ID` ASC) ;
