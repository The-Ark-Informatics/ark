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

public class BaseChannelInvalidFieldSupplied extends BaseChannelException {

    

    /**

     * Creates a new instance of <code>DAOException</code> without detail message.

     */

    public BaseChannelInvalidFieldSupplied() {

    }

    

    

    /**

     * Constructs an instance of <code>DAOException</code> with the specified detail message.

     * @param msg the detail message.

     */

    public BaseChannelInvalidFieldSupplied(String msg) {

        super(msg);

    }

    

    /**

     * Constructs an instance of <code>DAOException</code> with the specified detail message.

     * @param msg the detail message.

     */

    public BaseChannelInvalidFieldSupplied(Throwable t) {

        super(t);

    }

    

    /**

     * Constructs an instance of <code>DAOException</code> with the specified detail message.

     * @param msg the detail message.

     */

    public BaseChannelInvalidFieldSupplied(String msg, Throwable t) {

        super(msg, t);

    }

}

