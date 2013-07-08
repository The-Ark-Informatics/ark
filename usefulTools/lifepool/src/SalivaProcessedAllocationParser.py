'''
Created on 24/06/2013

@author: thilina
'''

import re
 
print "----------------------- SALIVA PROCESSED BIOSPECIMEN ALLOCATION --------------------------------"

inputFile = open('../resource/saliva-template.csv', 'r')

firstLine=True
output=""

rowMap ={}
rowMap["A"]="1"
rowMap["B"]="2"
rowMap["C"]="3"
rowMap["D"]="4"
rowMap["E"]="5"
rowMap["F"]="6"
rowMap["G"]="7"
rowMap["H"]="8"
rowMap["I"]="9"
rowMap["J"]="10"
rowMap["K"]="11"
rowMap["L"]="12"
rowMap["M"]="13"   

for line in inputFile:
    if firstLine :
        firstLine=False
        continue
    tokens = line.split(",")
    
    partcipantId = tokens[1]
    parentUid =  tokens[3].strip()
    
    sampleUID1=parentUid+" "+tokens[6]
    sampleUID2=parentUid+" "+tokens[13]
    
    line1=sampleUID1+","+tokens[8]+","+tokens[9]+","+tokens[10]+","+rowMap.get(tokens[11])+","+tokens[12]
    print line1
    line2=sampleUID2+","+tokens[15]+","+tokens[16]+","+tokens[17]+","+rowMap.get(tokens[18])+","+tokens[19][:-1]
    print line2
    output=output+line1+"\n"+line2+"\n"
    
inputFile.close() 

outputFile = open('../resource/SALIVA_PROCESSED_ALLOCATION.csv', 'w')
outputFile.write(output)
outputFile.close()

print "----SALIVA PROCESSED BIOSPECIMEN ALLOCATION----------------"


    
    
    