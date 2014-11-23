select * from lims.inv_site;

select name, address, id from lims.inv_site where id not in (3,6, 17, 14, 12);

select * from study.study ;

-- sagic = 576
-- renal = 563
-- auscan risk = 586
-- raves = 194
-- 'KEMH',NULL,'18'
-- 'WADB (RPH)','RPH','16'
-- ''WADB (SCGH)',NULL,'13'
insert into lims.study_inv_site(study_id, inv_site_id)
values( 194, 18);
insert into lims.study_inv_site(study_id, inv_site_id)
values( 194, 16);
insert into lims.study_inv_site(study_id, inv_site_id)
values( 194, 13);
insert into lims.study_inv_site(study_id, inv_site_id)
values( 576, 18);
insert into lims.study_inv_site(study_id, inv_site_id)
values( 576, 16);
insert into lims.study_inv_site(study_id, inv_site_id)
values( 576, 13);
insert into lims.study_inv_site(study_id, inv_site_id)
values(563 , 18);
insert into lims.study_inv_site(study_id, inv_site_id)
values(563 , 16);
insert into lims.study_inv_site(study_id, inv_site_id)
values(563 , 13);
insert into lims.study_inv_site(study_id, inv_site_id)
values(586 , 18);
insert into lims.study_inv_site(study_id, inv_site_id)
values( 586, 16);
insert into lims.study_inv_site(study_id, inv_site_id)
values(586 , 13);
-- 5	WASHS
insert into lims.study_inv_site(study_id, inv_site_id)
values(5 , 16);
-- IORT under WARTN 417
insert into lims.study_inv_site(study_id, inv_site_id)
values(417 , 18);
insert into lims.study_inv_site(study_id, inv_site_id)
values( 417, 16);
insert into lims.study_inv_site(study_id, inv_site_id)
values(417 , 13);

select * from lims.study_inv_site