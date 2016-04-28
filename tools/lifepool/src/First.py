'''
Created on Mar 2, 2013

@author: thilina
'''
import re
print ("My first python script")



def add(a,b):
    return a+b

def addFixedValue(a):
    y = 5
    return y +a
  
print add(1,2)
print addFixedValue(1) 

def isNotNull(value):
    return value is not None and len(value) > 0

def isAlphaNumeric(val):
    print val
    valid = re.match('^[\w-]+$', val) is not None
    return valid
    
def insertNull(val):
    result="\\N";
    if isNotNull(val) and isAlphaNumeric(val):
        result = val
    return result

#print isNotNull(None)
print insertNull(None)

