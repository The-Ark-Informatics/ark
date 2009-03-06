/*

 * ValidateFieldFunctions.java

 *

 * Created on July 2, 2002, 9:02 AM

 */



package neuragenix.common;



import neuragenix.common.Utilities;

import java.util.Calendar;

import java.util.Date;



/**

 *

 * @author  Administrator

 */

public class ValidateFieldFunctions {

    

    public final static int DOB_CHECK = 1;

    public final static int EMAIL_CHECK = 2;

    public final static int DATE_GREATER_THAN_TODAY = 3;

    public final static int MEDICARENO_CHECK = 4;

    public final static int DATE_NOT_IN_FUTURE = 5;

    public final static int DATE_GREATER_THAN_OR_EQUAL_TODAY = 6;

    

    private static final int intReasonableTime = -130; // This is the number of years the DOB makes sure it is greater than

    /** Creates a new instance of ValidateFieldFunctions */

    public ValidateFieldFunctions() {

        

    }

      public static void main(String[] args) {

     //     ValidateFieldFunctions my_test = new ValidateFieldFunctions();

   //     System.err.println(ValidateFieldFunctions.validateFields("12/06/1702", 1));



      }

    public static boolean validateFields(String strFieldValue, int intFunctionNumber)

    {

        boolean blPassedTest = true;

        switch(intFunctionNumber)

        {

            case DOB_CHECK: // This test checks to see if a date is a valid DOB

               // Get the current time

                Calendar calCurrent = Calendar.getInstance();

                calCurrent.getInstance();

                // Get the time supplied

                Calendar calSupplied = Calendar.getInstance();

                calSupplied.setTime(Utilities.convertStringToDate(strFieldValue, "dd/MM/yyyy"));

                // Get the time deemed reasonable oldest age

                Calendar calReasonableAge = Calendar.getInstance(); 

                calReasonableAge.add(Calendar.YEAR, intReasonableTime);

      

               // Check to see if the date supplied is over the allowed oldest age and younger than todays date.

                if(calSupplied.before(calCurrent) && calSupplied.after(calReasonableAge))

                {

                    

                    blPassedTest =  true;

                    break;

                }

                else

                {

                    

                    blPassedTest = false;

                    break;

                }

            case EMAIL_CHECK:

                // An email must have a @ and a .

                

                if(strFieldValue.indexOf('@') == -1 || strFieldValue.indexOf('.') == -1 || strFieldValue.indexOf(" ") != -1)

                {

            

                    blPassedTest = false;

                    break;

                }

                else

                {

                    break;

                }

            case DATE_GREATER_THAN_TODAY:

                if(strFieldValue != null)

                {

                 

                    // Get the current time

                    Calendar calCurrent2 = Calendar.getInstance();

                    calCurrent2.getInstance();

                    // Get the time supplied

                    Calendar calSupplied2 = Calendar.getInstance();

                    calSupplied2.setTime(Utilities.convertStringToDate(strFieldValue, "dd/MM/yyyy"));

               

                    // Check to see if the date supplied is after todays' date

                    if(calSupplied2.after(calCurrent2))

                    {

                        blPassedTest =  true;

                        break;

                    }

                    else

                    {

                    

                        blPassedTest = false;

                        break;

                    }

                }else{blPassedTest =  true;}

                 break;

           case MEDICARENO_CHECK:

              // int [] arrDigit = new int [11];

               int intMedicareEquationResult = 0;

               // Check to see it is 11 characters

            

               if (strFieldValue.length() != 11)

               { 

                   blPassedTest = false;

                   break;

               }

               else

                {

                 

                    // Check to see that the medicare number contains all int's and get the numbers for the magic medicare number check

                    for (int i = 0; i < strFieldValue.length(); i++)

                    {

                        if(Utilities.validateIntValue(strFieldValue.substring(i, (i + 1))) == false)

                        {   

                            blPassedTest = false;

                            break;

                        }

                    // arrDigit[i] = new Integer(strFieldValue.substring(i, (i + 1))).intValue();

                        if(i == 0)

                        {

                            intMedicareEquationResult += new Integer(strFieldValue.substring(i, (i + 1))).intValue();

                        }

                        else if(i == 1)

                        {

                            intMedicareEquationResult += (new Integer(strFieldValue.substring(i, (i + 1))).intValue() * 3);

                        }

                        else if(i == 2)

                        {

                            intMedicareEquationResult += (new Integer(strFieldValue.substring(i, (i + 1))).intValue() * 7);

                        }

                        else if(i == 3)

                        {

                            intMedicareEquationResult += (new Integer(strFieldValue.substring(i, (i + 1))).intValue() * 9);

                        }

                        else if(i == 4)

                        {

                            intMedicareEquationResult += new Integer(strFieldValue.substring(i, (i + 1))).intValue();

                        }

                        else if(i == 5)

                        {

                            intMedicareEquationResult += (new Integer(strFieldValue.substring(i, (i + 1))).intValue() * 3);

                        }

                        else if(i == 6)

                        {

                            intMedicareEquationResult += (new Integer(strFieldValue.substring(i, (i + 1))).intValue() * 7);

                        }

                        else if(i == 7)

                        {

                            intMedicareEquationResult += (new Integer(strFieldValue.substring(i, (i + 1))).intValue() * 9);

                        }

                        

                    }

               }

               if(blPassedTest == true)

               {

                   

                    // Did it pass the magic test?

                    if(intMedicareEquationResult%10 != new Integer(strFieldValue.substring(8, (8 + 1))).intValue())

                    {

                        blPassedTest = false;

                        break;     

                    }

               }

               break;

         case DATE_NOT_IN_FUTURE:

             if(strFieldValue != null)

            {

                // Get the current time

                Calendar calCurrent3 = Calendar.getInstance();

                calCurrent3.getInstance();

                // Get the time supplied

                Calendar calSupplied3 = Calendar.getInstance();

                calSupplied3.setTime(Utilities.convertStringToDate(strFieldValue, "dd/MM/yyyy"));

                calCurrent3.set(Calendar.HOUR_OF_DAY, 0);

                calCurrent3.set(Calendar.MINUTE, 0);

                calCurrent3.set(Calendar.SECOND, 0);

                calCurrent3.set(Calendar.MILLISECOND, 0);

               // Check to see if the date supplied is after todays' date

                if(calSupplied3.before(calCurrent3) || calSupplied3.equals(calCurrent3))

                {

                    blPassedTest =  true;

                    break;

                }

                else

                {

                    

                    blPassedTest = false;

                    break;

                }      

             }else{blPassedTest =  true;}

               // Perform the medicare magic check

             break;

           case DATE_GREATER_THAN_OR_EQUAL_TODAY:

                if(strFieldValue != null)

                {

              

                    // Get the current time

                    Calendar calCurrent2 = Calendar.getInstance();

                    calCurrent2.getInstance();

                    // Get the time supplied

                    Calendar calSupplied2 = Calendar.getInstance();

                    calCurrent2.set(Calendar.HOUR_OF_DAY, 0);

                    calCurrent2.set(Calendar.MINUTE, 0);

                    calCurrent2.set(Calendar.SECOND, 0);

                    calCurrent2.set(Calendar.MILLISECOND, 0);

                    calSupplied2.setTime(Utilities.convertStringToDate(strFieldValue, "dd/MM/yyyy"));

              

                    // Check to see if the date supplied is after or equal to todays' date

               

                    if(calSupplied2.after(calCurrent2) || calSupplied2.equals(calCurrent2))

                    {

                   

                        blPassedTest =  true;

                        break;

                    }

                    else

                    {

                 

                        blPassedTest = false;

                        break;

                    }

                }else{blPassedTest =  true;}

                 break;    

           default:

          //      blPassedTest = true;

        }

     

        return blPassedTest;

    }

}

