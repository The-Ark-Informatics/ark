select * from admin.billable_item_type;

delete from  admin.bilable_item  ;
delete from  admin.work_request  ;
delete from  admin.researcher  ;
delete from  admin.billable_item_type  ; -- not actually a look up...is study specific



/*******************
 LOOK UP TABLES DO NOT DELETE
-- delete from  admin.billing_type  ;
-- delete from  admin.billable_item_type  ;
-- delete from  admin.billable_item_type_status  ;
-- delete from  admin.researcher_role  ;
-- delete from  admin.researcher_status  ;
-- delete from  admin.work_request_status  ;
*******************/
