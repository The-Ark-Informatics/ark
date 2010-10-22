/**
 * 
 */
package au.org.theark.phenotypic.exception;

import au.org.theark.core.exception.ArkBaseException;

/**
 * @author elam
 *
 */
@SuppressWarnings("serial")
public class PhenotypicBaseException extends ArkBaseException {

	public PhenotypicBaseException(){
		super();
	}
	public PhenotypicBaseException(String message){ 
		super(message);
	}
}
