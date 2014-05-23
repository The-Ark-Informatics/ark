ALTER TABLE `lims`.`inv_freezer` 
ADD UNIQUE INDEX `uq_freezer_site` (`SITE_ID` ASC, `NAME` ASC) ;

ALTER TABLE `lims`.`inv_rack` 
ADD UNIQUE INDEX `uq_rack_name_freezer` (`FREEZER_ID` ASC, `NAME` ASC) ;

-- maybe rename the uq constaint on box



