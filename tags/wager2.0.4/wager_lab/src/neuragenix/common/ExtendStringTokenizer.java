/*

 * ExtendStringTokenizer.java

 *

 * Created on April 17, 2003, 9:18 AM

 */



package neuragenix.common;



/**

 * This class supports methods for manipulating with a string

 * @author  laptops'n'memory

 */



public class ExtendStringTokenizer {

    /**

     * String of this StringTokenizer

     */

    private String string;

    

    /**

     * Start position of the token

     */

    private int startToken;

    

    /**

     * Ending position of the token

     */

    private int endToken;

    

    /**

     * Delemiter

     */

    private String delimiter;

    

    /** Creates a new instance of ExtendStringTokenizer */

    public ExtendStringTokenizer(String str) {

        this(str, " ");

    }

    

    public ExtendStringTokenizer(String str, String delim) {

        string = str;

        delimiter = delim;

        startToken = 0;

        endToken = 0;

    }

    

    public boolean hasMoreTokens() {

        if (endToken >= string.length())

            return false;

        

        return true;

    }

    

    public boolean hasMoreElements() {

        return hasMoreTokens();

    }

    

    public String nextToken() {

        startToken = endToken;

        int index = string.indexOf(delimiter, endToken);

        

        if (index < 0)

        {

            endToken = string.length();

            return string.substring(startToken);

        }

        else

        {

            if (index == endToken)

            {

                endToken++;

                return "";

            }

            else

            {

                endToken = index + 1;

                return string.substring(startToken, endToken - 1);

            }

        }

    }

    

   /* public static void main(String[] args)

    {

        ExtendStringTokenizer token = new ExtendStringTokenizer("Huy,,Hoang", ",");

        

        while (token.hasMoreElements())

            System.err.println(token.nextToken());

    }*/

}
