'''
Created on 24/06/2013

@author: thilina
'''

import re
from datetime import datetime
 
print "----------------------- SALIVA INITIAL BIOSPECIMEN --------------------------------"

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
    
    quantity = float(tokens[7])+float(tokens[14])
    
    subjectUid = pids[0]+pids[1] 
    collectionUid = "S-"+subjectUid+"-"+collectionDate
    
    line=subjectUid+","+parentUid+","+collectionUid+","+sampleDate+","+processedDate+","+str(quantity)
    print line
    output=output+line+"\n"
        
inputFile.close() 

outputFile = open('../resource/SALIVA_INITIAL_BIOSPECIMEN.csv', 'w')
outputFile.write(output)
outputFile.close()

print "----SALIVA INITIAL BIOSPECIMEN DONE ----------------"
