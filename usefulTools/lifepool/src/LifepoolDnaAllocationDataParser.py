'''
Created on Mar 2, 2013

@author: thilina

'''
import re
print "-----------------------DNA Allocations ----------------------------------"

inputFile = open('../resource/DNA-sample-allocation.csv', 'r')

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

#print inputFile
firstLine=True
output=""
for line in inputFile:
    if firstLine :
        firstLine=False
        continue
    tokens = line.split(",")
    participantId=tokens[2]
    specimenId = tokens[3]
    parentId =  participantId[0:2].strip() +" " +participantId[2:].strip()+" "+specimenId.strip()
    
    #50
    subjectUid = parentId +"-" +tokens[12]
    output=output+subjectUid
    freezer = insertNull(tokens[17])
    output=output+","+freezer
    rack = insertNull(tokens[18])
    output=output+","+rack
    box = insertNull(tokens[19])
    output=output+","+box
    row = insertNull(rowMap.get(tokens[20]))
    output=output+","+row
    col = insertNull(tokens[21])
    output=output+","+col+"\n"
    
    #51
    subjectUid = parentId +"-" +tokens[22]
    output=output+subjectUid
    freezer = insertNull(tokens[25])
    output=output+","+freezer
    rack = insertNull(tokens[26])
    output=output+","+rack
    box = insertNull(tokens[27])
    output=output+","+box
    row = insertNull(rowMap.get(tokens[28]))
    output=output+","+row
    col = insertNull(tokens[29])
    output=output+","+col+"\n"
    
    #52
    subjectUid = parentId +"-" +tokens[30]
    output=output+subjectUid
    freezer = insertNull(tokens[33])
    output=output+","+freezer
    rack = insertNull(tokens[34])
    output=output+","+rack
    box = insertNull(tokens[35])
    output=output+","+box
    row = insertNull(rowMap.get(tokens[36]))
    output=output+","+row
    col = insertNull(tokens[37])
    output=output+","+col+"\n"
    
    #53
    subjectUid = parentId +"-" +tokens[38]
    output=output+subjectUid
    freezer = insertNull(tokens[41])
    output=output+","+freezer
    rack = insertNull(tokens[42])
    output=output+","+rack
    box = insertNull(tokens[43])
    output=output+","+box
    row = insertNull(rowMap.get(tokens[44]))
    output=output+","+row
    col = insertNull(tokens[45])
    output=output+","+col+"\n"
    
    #54
    subjectUid = parentId +"-" +tokens[46]
    output=output+subjectUid
    freezer = insertNull(tokens[50])
    output=output+","+freezer
    rack = insertNull(tokens[51])
    output=output+","+rack
    box = insertNull(tokens[49])
    output=output+","+box
    row = insertNull(rowMap.get(tokens[52]))
    output=output+","+row
    col = insertNull(tokens[53])
    output=output+","+col+"\n"
    
    
inputFile.close() 

print output

outputFile = open('../resource/DNA_ALLOCATION.csv', 'w')
outputFile.write(output)
outputFile.close()

print "----DNA ALLOCATION DONE ----------------"
