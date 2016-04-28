import MySQLdb

db = MySQLdb.connect(host="localhost", port=3306, user="XXXXX", passwd="YYYYY")

cursor = db.cursor()

rowTypeId=0
colTypeId=0

cursor.execute("""select id,name 
                    from lims.inv_col_row_type """)

data = cursor.fetchall()
for row in data :
    name=row[1]
    if "Numeric" == name:
        colTypeId = row[0]
    else:
        rowTypeId = row[0]


cursor.execute("""select id from lims.inv_site where name = %s """,
                              ("Peter MacCallum"))
row = cursor.fetchone()
siteId = row[0]


cursor.execute("""insert into lims.inv_freezer (SITE_ID,CAPACITY,NAME,AVAILABLE) values(%s,%s,%s,%s) """,
                              (siteId,30,"LN 1",10))

lnFreezerId = db.insert_id()

db.commit()

for r in range(1, 21, 1):
        print "Rack %d" % (r)
        cursor.execute("""insert into lims.inv_rack (FREEZER_ID,CAPACITY,NAME,AVAILABLE) values(%s,%s,%s,%s) """,
                       (lnFreezerId, 100, ("Rack " + "{0:0>2}".format(r)), 87))
        rackId = db.insert_id()
        db.commit()
        for b in range(1, 27, 2):
                print "Box" + str(b)
                cursor.execute("""insert into lims.inv_box (rack_ID,CAPACITY,NAME,AVAILABLE,COLNOTYPE_ID,ROWNOTYPE_ID,NOOFROW,NOOFCOL) values(%s,%s,%s,%s,%s,%s,%s,%s) """,
                               (rackId, 100, ("Box " + "{0:0>2}".format(b)), 100, colTypeId, rowTypeId,10,10))
                boxId = db.insert_id()
                db.commit()
                for x in range(1, 11, 1):
                        for y in range (1, 11, 1):
                                print "cell " + str(x) + " " + str(y)
                                cursor.execute("""insert into lims.inv_cell (box_ID,colno,rowno) values(%s,%s,%s) """, (boxId, x, y))
                                db.commit()
