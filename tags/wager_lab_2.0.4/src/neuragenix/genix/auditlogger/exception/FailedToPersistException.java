/*
 * FailedToPersistException.java
 *
 * Created on 12 December 2005, 14:34
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package neuragenix.genix.auditlogger.exception;

/**
 *
 * @author dmurley
 */
public class FailedToPersistException extends java.lang.Exception
{
   
   /**
    * Creates a new instance of <code>FailedToPersistException</code> without detail message.
    */
   public FailedToPersistException()
   {
   }
   
   
   /**
    * Constructs an instance of <code>FailedToPersistException</code> with the specified detail message.
    * @param msg the detail message.
    */
   public FailedToPersistException(String msg)
   {
      super(msg);
   }
}
