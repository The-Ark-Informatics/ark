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

public class DAOException extends java.lang.Exception {

    

    /**

     * Creates a new instance of <code>DAOException</code> without detail message.

     */

    public DAOException() {

    }

    

    

    /**

     * Constructs an instance of <code>DAOException</code> with the specified detail message.

     * @param msg the detail message.

     */

    public DAOException(String msg) {

        super(msg);

    }

    /**

     * Constructs an instance of <code>DAOException</code> with the specified detail message.

     * @param msg the detail message.

     */

    public DAOException(String msg, Exception e) {

        super(msg, e);

    }

}

