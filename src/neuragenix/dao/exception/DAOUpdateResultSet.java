/*

 * DAOException.java

 *

 * Created on October 28, 2002, 2:51 PM

 */



package neuragenix.dao.exception;



/**

 *

 * @author  Administrator

 */

public class DAOUpdateResultSet extends java.lang.Exception {

    

    /**

     * Creates a new instance of <code>DAOException</code> without detail message.

     */

    public DAOUpdateResultSet() {

    }

    

    

    /**

     * Constructs an instance of <code>DAOException</code> with the specified detail message.

     * @param msg the detail message.

     */

    public DAOUpdateResultSet(String msg) {

        super(msg);

    }

}

