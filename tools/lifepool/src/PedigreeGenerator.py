'''
Created on 22/03/2013

@author: thilina
'''

print "Pedigree Generator"

#Father Mother Son Daughter
output = ""
#father
output = output + "1\tA0001\t0\t0\t1\t1\t1\n"
#mother
output = output + "1\tA0002\t0\t0\t2\t1\t1\n"
#Son
output = output + "1\tA0005\tA0001\tA0002\t1\t1\t1\n"
#Daughter
output = output + "1\tA0009\tA0001\tA0002\t2\t1\t1\n"

outputFile = open('../resource/sample.ped', 'w')
outputFile.write(output)
outputFile.close()

print "----Pedigree Generator Completed ----------------"

