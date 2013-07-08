'''
Created on 24/06/2013

@author: thilina
'''

import re
from datetime import datetime
 
print "----------------------- SALIVA PROCESSED BIOSPECIMEN --------------------------------"

inputFile = open('../resource/saliva-template.csv', 'r')

firstLine=True
output=""

for line in inputFile:
    if firstLine :
        firstLine=False
        continue
    tokens = line.split(",")
    
    partcipantId = tokens[1]
    parentUid =  tokens[3].strip()
    pids = partcipantId.split(" ")
    
    collectionDate = datetime.strptime(tokens[2],"%d/%m/%Y").strftime('%Y-%m-%d')  
    sampleDate = collectionDate
    processedDate = datetime.strptime(tokens[5],"%d/%m/%Y").strftime('%Y-%m-%d')
    
    sampleUID1=parentUid+" "+tokens[6]
    sampleUID2=parentUid+" "+tokens[13]
    
    quantity1 = tokens[7]
    quantity2 = tokens[14]
    
    subjectUid = pids[0]+pids[1] 
    collectionUid = "S-"+subjectUid+"-"+collectionDate
    
    line1=subjectUid+","+sampleUID1+","+parentUid+","+collectionUid+","+sampleDate+","+processedDate+","+quantity1
    print line1
    line2=subjectUid+","+sampleUID2+","+parentUid+","+collectionUid+","+sampleDate+","+processedDate+","+quantity2 
    print line2
    output=output+line1+"\n"+line2+"\n"
    
inputFile.close() 

outputFile = open('../resource/SALIVA_PROCESSED_BIOSPECIMEN.csv', 'w')
outputFile.write(output)
outputFile.close()

print "----SALIVA PROCESSED BIOSPECIMEN DONE ----------------"


    
    
    