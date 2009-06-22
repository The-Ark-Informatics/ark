/*

 * DAOQueryInvalidDomain.java

 *

 * Created on October 28, 2002, 3:29 PM

 */



package neuragenix.dao.exception;



/**

 *

 * @author  Administrator

 */

public class DAOUpdateInvalidFieldValues extends DAOException{

    

    /**

     * Creates a new instance of <code>DAOQueryInvalidDomain</code> without detail message.

     */

    public DAOUpdateInvalidFieldValues() {

    }

    

    

    /**

     * Constructs an instance of <code>DAOQueryInvalidDomain</code> with the specified detail message.

     * @param msg the detail message.

     */

    public DAOUpdateInvalidFieldValues(String msg) {

        super(msg);

    }

}

