ALTER TABLE `wagerlab`.`ix_inv_site` 
ADD INDEX `ixinvsitename` (`NAME` ASC) ;

ALTER TABLE `wagerlab`.`ix_inv_tank` 
ADD INDEX `ixinvtankname` (`NAME` ASC) ;

ALTER TABLE `wagerlab`.`ix_inv_cell` 
ADD INDEX `ixinvcellbios` (`BIOSPECIMENKEY` ASC) ;

ALTER TABLE `wagerlab`.`ix_inv_tray` 
ADD INDEX `ixinvtravbox` (`BOXKEY` ASC) ;


ALTER TABLE `wagerlab`.`ix_biospecimen` 
ADD INDEX `ixbiospecstudy` (`STUDYKEY` ASC) ;



-- select * from  `wagerlab`.`ix_inv_tank` 