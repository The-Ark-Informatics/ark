/* 

 * Copyright (c) 2002 Neuragenix,  All Rights Reserved.

 * Password.java

 * Created on 8 October 2002, 15:24

 * @author  Shendon Ewans

 * 

 * Description: 

 * Class for verifying passwords. It will check the username's

 * old password with the one supplied and confirm the two new passwords

 * are correct. Returns a success code and the encrypted new password.

 */



package neuragenix.utils;



import java.beans.*;

import java.security.*;

import java.sql.*;

import org.jasig.portal.services.LogService;

import java.util.Date;

import org.jasig.portal.security.provider.AccountStoreFactory;



/**

 *

 * Password

 */

public class Password extends Object implements java.io.Serializable {



    // Variables

    private String strResults;

    private int intPasswordCheck;

    private String strUsername;

    private String strOldPassword;

    private String strNewPassword1;

    private String strNewPassword2;



    

/**

 *

 * Constructor for Password

 */

    public Password() {

        strResults = "";

        strUsername = "";

        strOldPassword = "";

        strNewPassword1 = "";

        strNewPassword2 = "";

        intPasswordCheck = 0;

    }

    

    public void CheckPassword() {       

       

       // Check whether the two new passwords are the same before proceeding

       if (strNewPassword1.compareTo(strNewPassword2) == 0) {

         if (isVerified(strUsername, strOldPassword) == true) {

           try {  

                 // Successful verification  

                 strResults = getEncrypt(strNewPassword1);

                 LogService.log(LogService.INFO, "Password verified for user " + strUsername);

                 intPasswordCheck = -1;  

           }

           catch (Exception e) {

                 LogService.log(LogService.INFO, "Password could not be encrypted for user " + strUsername);

                 strResults = "Password could not be encrypted for user " + strUsername;

                 intPasswordCheck = 0;             

           }

         }

         else {

           strResults = "Unable to verfiy old password.";

           LogService.log(LogService.INFO, "Unable to verfiy old password for user " + strUsername);

           intPasswordCheck = 0;

         }

       }

       else  { // Not same new passwords 

           strResults = "Confirmation of new password is inconsistant";    

           LogService.log(LogService.DEBUG, strResults + " for user: "  + strUsername);

           intPasswordCheck = 0;

       }

        

    }

    

    public static boolean isVerified(String strUsername, String strOldPassword){

        

        String strOldDBPassword;

        strOldDBPassword = "";

        String strHash;

        byte[] bWhole, salt = new byte[8], compare = new byte[16], dgx;

                    

              

        try {

              // Retrieve old password

              String acct[] = AccountStoreFactory.getAccountStoreImpl().getUserAccountInformation(strUsername);

              if (acct[0] != null) 

                  strOldDBPassword = acct[0];

              

              // Decode encrypted database password

              strHash = strOldDBPassword.substring(5);

              bWhole = decode(strHash);

              if (bWhole.length != 24) {

                LogService.log(LogService.INFO, "Invalid MD5 hash value provided for user:" + strUsername);

                return false;

              }

              // Distribute parts salt and password body

              System.arraycopy(bWhole, 0, salt, 0, 8);

              System.arraycopy(bWhole, 8, compare, 0, 16);



              // MD5 the given password

              MessageDigest md = MessageDigest.getInstance("MD5");



              md.update(salt);

              dgx = md.digest(strOldPassword.getBytes());



              // Perform compare

              boolean same = true;

              int i;

              for (i = 0; i < dgx.length; i++)

                  if (dgx[i] != compare[i])

                    same = false;      

              

              return same;

          



        } catch (Exception e) {

            LogService.instance().log(LogService.ERROR,e + "- CUserPassword.setRuntimeData: Error retrieving details for user:" + strUsername);

            return false;

        }

        

        

    }

    public static String getEncrypt(String strPassword) throws NoSuchAlgorithmException {

    

        // Variables for MD5 password verification

        MessageDigest oMDigest;  

        byte[] byteHash, rnd = new byte[8], fin = new byte[24];

    

    

        Long date = new Long((new Date()).getTime());

        SecureRandom r = new SecureRandom((date.toString()).getBytes()); 

        oMDigest = MessageDigest.getInstance("MD5"); 

        r.nextBytes(rnd);

        oMDigest.update(rnd);



        byteHash = oMDigest.digest(strPassword.getBytes());

        System.arraycopy(rnd, 0, fin, 0, 8);

        System.arraycopy(byteHash, 0, fin, 8, 16);

        

        return "(MD5)"+encode(fin);

        

    }

    

    

    public String getResults() {

        return strResults;

    }

    

    public int getintPasswordCheck() {

        return intPasswordCheck;

    }

    public void setOldPassword(String OldPassword) {

        strOldPassword = OldPassword;

    }

    public void setUsername(String Username) {

        strUsername = Username;

    }

    public void setNewPassword1(String NewPassword1) {

        strNewPassword1 = NewPassword1;

    }

 

    public void setNewPassword2(String NewPassword2){

       strNewPassword2 =  NewPassword2;

    }



 

//

// This was originally Jonathan B. Knudsen's Example from his book

// Java Cryptography published by O'Reilly Associates (1st Edition 1998)

//

  public static byte[] decode(String base64) {

    int pad = 0;

    for (int i = base64.length() - 1; base64.charAt(i) == '='; i--)

      pad++;

    int length = base64.length()*6/8 - pad;

    byte[] raw = new byte[length];

    int rawIndex = 0;

    for (int i = 0; i < base64.length(); i += 4) {

      int block = (getValue(base64.charAt(i)) << 18) + (getValue(base64.charAt(i + 1)) << 12) + (getValue(base64.charAt(

          i + 2)) << 6) + (getValue(base64.charAt(i + 3)));

      for (int j = 0; j < 3 && rawIndex + j < raw.length; j++)

        raw[rawIndex + j] = (byte)((block >> (8*(2 - j))) & 0xff);

      rawIndex += 3;

    }

    return  raw;

  }

      

  private static String encode(byte[] raw) {

    StringBuffer encoded = new StringBuffer();

    for (int i = 0; i < raw.length; i += 3) {

      encoded.append(encodeBlock(raw, i));

    }

    return encoded.toString();

  }

   private static char[] encodeBlock(byte[] raw, int offset) {

    int block = 0;

    int slack = raw.length - offset - 1;

    int end = (slack >= 2) ? 2 : slack;

    for (int i = 0; i <= end; i++) {

      byte b = raw[offset + i];

      int neuter = (b < 0) ? b + 256 : b;

      block += neuter << (8 * (2 - i));

    }

    char[] base64 = new char[4];

    for (int i = 0; i < 4; i++) {

      int sixbit = (block >>> (6 * (3 - i))) & 0x3f;

      base64[i] = getChar(sixbit);

    }

    if (slack < 1) base64[2] = '=';

    if (slack < 2) base64[3] = '=';

    return base64;

  }



  private static char getChar(int sixBit) {

    if (sixBit >= 0 && sixBit <= 25)

      return (char)('A' + sixBit);

    if (sixBit >= 26 && sixBit <= 51)

      return (char)('a' + (sixBit - 26));

    if (sixBit >= 52 && sixBit <= 61)

      return (char)('0' + (sixBit - 52));

    if (sixBit == 62) return '+';

    if (sixBit == 63) return '/';

    return '?';

  }

  



  protected static int getValue(char c) {

    if (c >= 'A' && c <= 'Z')

      return  c - 'A';

    if (c >= 'a' && c <= 'z')

      return  c - 'a' + 26;

    if (c >= '0' && c <= '9')

      return  c - '0' + 52;

    if (c == '+')

      return  62;

    if (c == '/')

      return  63;

    if (c == '=')

      return  0;

    return  -1;

  }

}



