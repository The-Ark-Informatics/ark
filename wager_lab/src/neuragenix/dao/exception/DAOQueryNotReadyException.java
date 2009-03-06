/*

 * DAOQueryNotReadyException.java

 *

 * Created on October 28, 2002, 3:04 PM

 */



package neuragenix.dao.exception;



/**

 *

 * @author  Administrator

 */

public class DAOQueryNotReadyException extends DAOException {

    

    /**

     * Creates a new instance of <code>DAOQueryNotReadyException</code> without detail message.

     */

    public DAOQueryNotReadyException() {

    }

    

    

    /**

     * Constructs an instance of <code>DAOQueryNotReadyException</code> with the specified detail message.

     * @param msg the detail message.

     */

    public DAOQueryNotReadyException(String msg) {

        super(msg);

    }

}

