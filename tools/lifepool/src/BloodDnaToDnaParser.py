'''
Created on 17/07/2013

@author: thilina
'''

import re
print "-----------------------BLOOD DNA DNA PROCESS ----------------------------------"

inputFile = open('../resource/BloodDnaDataSet.csv', 'r')

def getInitVolume(value):
    nums = value.split("x")
    initVol = (float(nums[0].strip())*float(nums[1].strip()[:-2]))/1000
    return initVol

def getCorrectVolume(value):
    volume = 0.0
    if  value  and  value.strip() not in "-" :
        volume = float(value.strip())/1000
    return volume

firstLine=True
output=""
for line in inputFile:
    if firstLine :
        firstLine=False
        continue
    
    
    tokens = line.split(",")
    participantId=tokens[2]
    specimenId = tokens[3]
    
    parentId =  participantId[0:2].strip()+" "+participantId[2:].strip()+" "+specimenId.strip()
    parentUid=parentId+"-500"
    
    
    initVolume = getInitVolume(tokens[8])
    
    vol50 = getCorrectVolume(tokens[13])
    vol51 = getCorrectVolume(tokens[23])
    vol52 = getCorrectVolume(tokens[31])
    vol53 = getCorrectVolume(tokens[39])
    vol54 = getCorrectVolume(tokens[47])
    
    con50 = getCorrectVolume(tokens[16])
    con51 = getCorrectVolume(tokens[24])
    con52 = getCorrectVolume(tokens[32])
    con53 = getCorrectVolume(tokens[40])
    con54 = getCorrectVolume(tokens[48])
    
    parentVolume = initVolume - (vol51+vol52+vol53+vol54)
    if vol50 > 0:
        biospecimenUid = parentId +"-" +tokens[12]
        output=output+participantId+","+parentUid+","+biospecimenUid+","+str(parentVolume)+","+str(vol50)+","+str(con50)+","+"P"+"\n"
    
    if vol51 > 0:
        biospecimenUid = parentId +"-" +tokens[22]
        output=output+participantId+","+parentUid+","+biospecimenUid+","+str(vol51)+","+str(vol51)+","+str(con51)+","+"A"+"\n"
    
    if vol52 > 0:
        biospecimenUid = parentId +"-" +tokens[30]
        output=output+participantId+","+parentUid+","+biospecimenUid+","+str(vol52)+","+str(vol52)+","+str(con52)+","+"A"+"\n"
    
    if vol53 > 0:
        biospecimenUid = parentId +"-" +tokens[38]
        output=output+participantId+","+parentUid+","+biospecimenUid+","+str(vol53)+","+str(vol53)+","+str(con53)+","+"A"+"\n"
    
    if vol54 > 0:
        biospecimenUid = parentId +"-" +tokens[46]
        output=output+participantId+","+parentUid+","+biospecimenUid+","+str(vol54)+","+str(vol54)+","+str(con54)+","+"A"+"\n"
        
    
    ''' 
    
    output=output+line
    
    
    '''
inputFile.close() 

print output

outputFile = open('../resource/BLOOD_DNA_DNA_PROCESS.csv', 'w')
outputFile.write(output)
outputFile.close()

print "----BLOOD DNA DNA PROCESS DONE ----------------"

