select distinct colnotype_id, count(*) from lims.inv_box group by colnotype_id;

 select * from   wagerlab.`IX_INV_BOX` b;

select distinct colnotype from  wagerlab.`IX_INV_TRAY`

SELECT 
    ifnull((SELECT 
        `crt`.`ID`
    FROM
        `lims`.`inv_col_row_type` `crt`
    WHERE
        `NAME` = `t`.`COLNOTYPE`),123) as `COLNOTYPE_ID`/*,
    ifnull((SELECT 
        `crt`.`ID`
    FROM
        `lims`.`inv_col_row_type` `crt`
    WHERE
        `NAME` = `t`.`ROWNOTYPE`),123) as `ROWNOTYPE_ID`,*/,
    count(*)
FROM
    wagerlab.`IX_INV_TRAY` t, 
    wagerlab.`IX_INV_BOX` b,
    wagerlab.IX_INV_TANK tank, 
    lims.inv_freezer f
WHERE `t`.`BOXKEY` = `b`.`BOXKEY`
AND tank.TANKKEY = b.TANKKEY
AND tank.NAME = f.NAME
AND tank.TANKKEY NOT IN (222, 223, 224, 225)
group by COLNOTYPE_ID;