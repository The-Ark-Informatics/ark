'''
Created on 08/07/2013

@author: thilina
'''

import re
 
print "----------------------- SALIVA DNA BIOSPECIMEN ALLIQUOT --------------------------------"

inputFile = open('../resource/DNA_SALIVA_SPECIMEN.csv', 'r')

firstLine=True
output=""

for line in inputFile:
    if firstLine :
        firstLine=False
        continue
    tokens = line.split(",")
       
    superParentUid =  tokens[1].strip()
    parentUid = superParentUid[:-3]+"800"
    
    specimenUid1 = superParentUid+"-"+tokens[8].strip()
    quantity1=tokens[9].strip()
    concentration1 = tokens[10].strip()
    
    specimenUid2 = superParentUid+"-"+tokens[16].strip()
    quantity2=tokens[17].strip()
    concentration2 = tokens[18].strip()
    
    
    
    line1=parentUid+","+specimenUid1+","+quantity1+","+concentration1
    print line1
    line2=parentUid+","+specimenUid2+","+quantity2+","+concentration2
    print line2
    output=output+line1+"\n"+line2+"\n"
    
inputFile.close() 

outputFile = open('../resource/SALIVA_DNA_ALLIQUOT_BIOSPECIMEN.csv', 'w')
outputFile.write(output)
outputFile.close()

print "----SALIVA DNA BIOSPECIMEN ALLIQUOT DONE ----------------"

