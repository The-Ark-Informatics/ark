/**
 * 
 * This is a new file
 *
 *
 */
package au.org.theark.core.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import au.org.theark.core.model.study.entity.Address;

/**
 * @author nivedann
 *
 */
public class AddressVO implements Serializable{
	
	protected Address address;
	protected Collection<Address> addresses;
	
	
	/**
	 * 
	 */
	public AddressVO(){
		address = new Address();
		addresses = new ArrayList<Address>();
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public Collection<Address> getAddresses() {
		return addresses;
	}

	public void setAddresses(Collection<Address> addresses) {
		this.addresses = addresses;
	}


}
