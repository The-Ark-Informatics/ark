select * from pmhdiaendo.ix_inv_cell c, pmhdiaendo.ix_inv_tray t
where `c`.`TRAYKEY` = `t`.`TRAYKEY` and c.deleted=0 and c.biospecimenkey >0 
 -- =6600 

select * from pmhdiaendo.ix_inv_cell c where deleted = 0 and c.biospecimenkey >0 
  -- 7381


select * from pmhdiaendo.ix_inv_cell c where deleted = 0  -- 7381
and traykey not in (select traykey from pmhdiaendo.ix_inv_tray)  -- 781 of the trays referenced by inv_cell don't exist!!!!!!!!!



select * from pmhdiaendo.ix_inv_cell c, pmhdiaendo.ix_inv_tray t
where `c`.`TRAYKEY` = `t`.`TRAYKEY` -- =660


select * from  pmhdiaendo.ix_inv_tray t where traykey not 

select * from study.link_subject_Study where study_id = 590
and subject_uid like '368%';
