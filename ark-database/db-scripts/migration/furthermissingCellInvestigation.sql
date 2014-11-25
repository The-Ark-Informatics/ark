
select * from lims.biospecimen where id in (
select biospecimen_id  from lims.inv_cell where box_id = 3931
)


select count(biospecimen_id)  from lims.inv_cell where box_id = 3931

select * from lims.inv_box where id = 3931

select * from study.study



SHOW FULL PROCESSLIST;
