select * from lims.inv_box where name like '%RNA%95%'

-- Fix inv box to remove space at the start

UPDATE `lims`.`inv_box` SET `NAME`='RNA Box 059' WHERE `ID`='4577';

UPDATE `lims`.`inv_box` SET `NAME`='RNA Box 095' WHERE `ID`='4613';
