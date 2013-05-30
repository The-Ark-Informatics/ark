# show full processlist

# SHOW INNODB STATUS

#  show lock
-- '327769', 'SLP#67', '1', '473026', '2', '16442', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '20', '17', '4', '0', NULL, NULL, NULL, '0'

-- select * from lims.biocollection where (id>16000 OR NAME ='20111118112010');

select * from lims.bio_transaction where id>260000;

select * from lims.biospecimen where id>327000;

select * from lims.inv_cell where id>655500
