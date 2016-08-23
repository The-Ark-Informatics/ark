'''
Created on 08/07/2013

@author: thilina
'''

import re
 
print "----------------------- SALIVA DNA BIOSPECIMEN --------------------------------"

inputFile = open('../resource/DNA_SALIVA_SPECIMEN.csv', 'r')

firstLine=True
output=""

for line in inputFile:
    if firstLine :
        firstLine=False
        continue
    tokens = line.split(",")
       
    parentUid =  tokens[1].strip()
    specimenUid = parentUid+"-800"
    initQuantity = tokens[2].replace("OragenePurifier","").replace("ml","")
    quantity = tokens[4].strip()
    purity = tokens[6].strip()
    concentration = tokens[7].strip()
    operator = tokens[3].strip()
    qubit = tokens[5]
    
    line=parentUid+","+specimenUid+","+initQuantity+","+quantity+","+purity+","+concentration+","+operator+","+qubit
    print line
    output=output+line+"\n"
    
inputFile.close() 

outputFile = open('../resource/SALIVA_DNA_PROCESSED_BIOSPECIMEN.csv', 'w')
outputFile.write(output)
outputFile.close()

print "----SALIVA DNA BIOSPECIMEN DONE ----------------"

