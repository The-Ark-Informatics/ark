'''
Created on 09/07/2013

@author: thilina
'''


import re
 
print "----------------------- SALIVA DNA BIOSPECIMEN ALLIQUOT ALLOCATION --------------------------------"

inputFile = open('../resource/DNA_SALIVA_SPECIMEN.csv', 'r')

def IsNotNull(value):
    return value is not None and len(value) > 0

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
rowMap["l"]="9"   

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
    
    line1=specimenUid1+","+tokens[11]+","+tokens[12]+","+tokens[13]+","+(rowMap.get(tokens[14]) if IsNotNull(tokens[14]) else "")+","+tokens[15]
    print line1
    
    line2=specimenUid2+","+tokens[19]+","+tokens[20]+","+tokens[21]+","+(rowMap.get(tokens[22]) if IsNotNull(tokens[22]) else "")+","+tokens[23]
    print line2
    
    output=output+line1+"\n"+line2+"\n"
    
inputFile.close() 

outputFile = open('../resource/SALIVA_DNA_ALLIQUOT_ALLOCATION_BIOSPECIMEN.csv', 'w')
outputFile.write(output)
outputFile.close()

print "----SALIVA DNA BIOSPECIMEN ALLIQUOT ALLOCATION DONE ----------------"