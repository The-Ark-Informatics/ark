'''
Created on 24/06/2013

@author: thilina
'''

import re
from datetime import datetime
 
print "----------------------- Saliva Collection --------------------------------"

inputFile = open('../resource/saliva-template.csv', 'r')

firstLine=True
output=""

for line in inputFile:
    if firstLine :
        firstLine=False
        continue
    tokens = line.split(",")
    
    partcipantId = tokens[1]
    pids = partcipantId.split(" ")
    collectionDate = datetime.strptime(tokens[2],"%d/%m/%Y").strftime('%Y-%m-%d')  
    
    subjectUid = pids[0]+pids[1] 
    collectionUid = "S-"+subjectUid+"-"+datetime.strptime(tokens[2],"%d/%m/%Y").strftime('%Y%m%d')
    
    line=subjectUid+","+collectionUid+","+collectionDate
    print line
    output=output+line+"\n"
    
inputFile.close() 

outputFile = open('../resource/SALIVA_COLLECTION_ALLOCATION.csv', 'w')
outputFile.write(output)
outputFile.close()

print "----SALIVA COLLECTION ALLOCATION DONE ----------------"


    
    
    