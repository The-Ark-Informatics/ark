import MySQLdb

db = MySQLdb.connect(host="localhost", port=3306, user="XXXXX", passwd="YYYYYY")

siteId=0
studyId=0
lnFreezerId=0

cursor = db.cursor()

cursor.execute("""select id 
		  from study.study 
		  where name =  %s """,
               ("Lifepool"))

row = cursor.fetchone()

studyId =  row[0]

cursor.execute("""insert into lims.inv_site (NAME) values (%s) """,
               ("University of Melbourne"))

siteId=db.insert_id()

db.commit()


cursor.execute("""insert into lims.study_inv_site (STUDY_ID, INV_SITE_ID) values(%s,%s) """,
               (studyId,siteId))

db.commit()
