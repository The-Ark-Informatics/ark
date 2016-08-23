'''
Created on 08/07/2013

@author: thilina
'''

import re
 
print "----------------------- SALIVA DNA BIOSPECIMEN ALLIQUOT --------------------------------"

def getCorrectVolume(value):
    volume = 0.0
    if value  and  value.strip() not in "-" :
        volume = float(value.strip())
    return volume

inputFile = open('../resource/DNA_SALIVA_SPECIMEN.csv', 'r')

firstLine=True
output=""

for line in inputFile:
    if firstLine :
        firstLine=False
        continue
    tokens = line.split(",")
       
    superParentUid =  tokens[1].strip()
    parentUid = superParentUid+"-800"
    
    parentQuantity = float(tokens[4]) - getCorrectVolume(tokens[17])
    specimenUid1 = superParentUid+"-"+tokens[8].strip()
    quantity1=getCorrectVolume(tokens[9])
    concentration1 = getCorrectVolume(tokens[10])
    
    specimenUid2 = superParentUid+"-"+tokens[16].strip()
    quantity2=getCorrectVolume(tokens[17])
    concentration2 = getCorrectVolume(tokens[18])
    
    
    
    line1=parentUid+","+specimenUid1+","+str(quantity1)+","+str(concentration1)+","+str(parentQuantity)+",P"
    print line1
    line2=parentUid+","+specimenUid2+","+str(quantity2)+","+str(concentration2)+","+str(quantity2)+",A"
    print line2
    output=output+line1+"\n"+line2+"\n"
    
inputFile.close() 

outputFile = open('../resource/SALIVA_DNA_ALLIQUOT_BIOSPECIMEN.csv', 'w')
outputFile.write(output)
outputFile.close()

print "----SALIVA DNA BIOSPECIMEN ALLIQUOT DONE ----------------"

