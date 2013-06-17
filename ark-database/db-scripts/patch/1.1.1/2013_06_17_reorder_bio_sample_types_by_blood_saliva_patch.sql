use lims;

update bio_sampletype
set order_id = 0;

select @orderId := 0;

update bio_sampletype
set order_id = (select @orderId := @orderId + 1)
where name = 'Blood / Whole Blood';

update bio_sampletype
set order_id = (select @orderId := @orderId + 1)
where name = 'Blood / Peripheral Blood Lysed';

update bio_sampletype
set order_id = (select @orderId := @orderId + 1)
where name = 'Blood / Peripheral Blood DNA';

update 
(select bs.* 
 from bio_sampletype bs
 where bs.name like 'Blood%' and bs.order_id=0
 order by bs.name)p, bio_sampletype pp
set pp.order_id = (select @orderId := @orderId + 1)
where p.id=pp.id;

update 
(select bs.* 
 from bio_sampletype bs
 where bs.name like 'Saliva%' and bs.order_id=0
 order by bs.name)p, bio_sampletype pp
set pp.order_id = (select @orderId := @orderId + 1)
where p.id=pp.id;

update 
(select bs.* 
 from bio_sampletype bs
 where bs.order_id=0
 order by bs.name)p, bio_sampletype pp
set pp.order_id = (select @orderId := @orderId + 1)
where p.id=pp.id;
