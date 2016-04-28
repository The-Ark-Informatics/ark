'''
Created on 09/07/2013

@author: thilina
'''


import re
 
print "----------------------- SALIVA DNA BIOSPECIMEN ALLIQUOT ALLOCATION --------------------------------"

inputFile = open('../resource/DNA_SALIVA_SPECIMEN.csv', 'r')

def isNotNull(value):
    return value is not None and len(value) > 0

def isAlphaNumeric(val):
    valid = re.match('^[a-zA-Z0-9]*$', val) is not None
    return valid

def insertNull(val):
    result="\\N";
    if isNotNull(val) and isAlphaNumeric(val):
        result = val
    return result

def insertTextNull(val,text):
    result="\\N";
    if isNotNull(val) and isAlphaNumeric(val):
        result = text+"{0:0>2}".format(val)
    return result

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
       
    superParentUid =  tokens[1].strip()
    
    specimenUid1 = superParentUid+"-"+tokens[8].strip()
    quantity1=tokens[9].strip()
    concentration1 = tokens[10].strip()
    
    specimenUid2 = superParentUid+"-"+tokens[16].strip()
    quantity2=tokens[17].strip()
    concentration2 = tokens[18].strip()
    
    line1=specimenUid1+","+insertNull(tokens[11])+","+insertTextNull(tokens[12],"Rack ")+","+insertTextNull(tokens[13],"Box ")+","+(rowMap.get(tokens[14].upper()) if isNotNull(tokens[14]) else "\\N")+","+insertNull(tokens[15])
    print line1
    
    line2=specimenUid2+","+insertNull(tokens[19])+","+insertTextNull(tokens[20],"Rack ")+","+insertTextNull(tokens[21],"Box ")+","+(rowMap.get(tokens[22].upper()) if isNotNull(tokens[22]) else "\\N")+","+insertNull(tokens[23])
    print line2
    
    output=output+line1+"\n"+line2+"\n"
    
inputFile.close() 

outputFile = open('../resource/SALIVA_DNA_ALLIQUOT_ALLOCATION_BIOSPECIMEN.csv', 'w')
outputFile.write(output)
outputFile.close()

print "----SALIVA DNA BIOSPECIMEN ALLIQUOT ALLOCATION DONE ----------------"