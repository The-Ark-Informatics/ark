/*

 * DAOException.java

 *

 * Created on October 28, 2002, 2:51 PM

 */



package neuragenix.common.exception;



/**

 *

 * @author  Administrator

 */

public class BaseChannelException extends java.lang.Exception {

    

    /**

     * Creates a new instance of <code>DAOException</code> without detail message.

     */

    public BaseChannelException() {

    }

    

    

    /**

     * Constructs an instance of <code>DAOException</code> with the specified detail message.

     * @param msg the detail message.

     */

    public BaseChannelException(String msg) {

        super(msg);

    }



    /**

     * Constructs an instance of <code>DAOException</code> to wrap the contained exception.

     * @param t the wrapped exception

     */

    public BaseChannelException(Throwable t) {

        super(t);

    }

    /**

     * Constructs an instance of <code>DAOException</code> with the specified detail message.

     * @param msg the detail message.

     * @param t the wrapped exception

     */

    public BaseChannelException(String msg, Throwable t) {

        super(msg, t);

    }

}

