/*******************************************************************************
 * Copyright (c) 2011  University of Western Australia. All rights reserved.
 * 
 * This file is part of The Ark.
 * 
 * The Ark is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 * 
 * The Ark is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package au.org.theark.report.model.vo.report;

import java.io.Serializable;
import java.util.Date;

public class StudyComponentDetailsDataRow implements Serializable {

	private static final long	serialVersionUID	= 1L; 

	protected String				subjectUID;
	protected String				subjectStatus;
	protected String				firstName;
	protected String				lastName;
	protected String				streetAddress;
	protected String				suburb;
	protected String				state;
	protected String				postcode;
	protected String				country;
	protected Date					dateOfBirth;
	protected Date					completedDate;
	protected Date					requestedDate;
	protected Date					receivedDate;
	protected String 				componentStatus;
	//Hidden field
	protected Long 					personId; 
	
	public StudyComponentDetailsDataRow(){
		
	}
	public StudyComponentDetailsDataRow(String subjectUID, String subjectStatus, String firstName, String lastName,String streetAddress, String suburb, String state, String postcode, 
			String country, Date dateOfBirth,Date completedDate,Date requestedDate,Date receivedDate, String componentStatus,Long personId) {
		super();
		this.subjectUID = subjectUID;
		this.subjectStatus = subjectStatus;
		this.firstName = firstName;
		this.lastName = lastName;
		this.streetAddress = streetAddress;
		this.suburb = suburb;
		this.state = state;
		this.postcode = postcode;
		this.country = country;
		this.dateOfBirth = dateOfBirth;
		this.completedDate=completedDate;
		this.requestedDate=requestedDate;
		this.receivedDate=receivedDate;
		this.componentStatus=componentStatus;
		this.personId=personId;
	}
	public String getSubjectUID() {
		return subjectUID;
	}
	public void setSubjectUID(String subjectUID) {
		this.subjectUID = subjectUID;
	}
	public String getSubjectStatus() {
		return subjectStatus;
	}
	public void setSubjectStatus(String subjectStatus) {
		this.subjectStatus = subjectStatus;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getStreetAddress() {
		return streetAddress;
	}
	public void setStreetAddress(String streetAddress) {
		this.streetAddress = streetAddress;
	}
	public String getSuburb() {
		return suburb;
	}
	public void setSuburb(String suburb) {
		this.suburb = suburb;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getPostcode() {
		return postcode;
	}
	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public Date getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	public Date getCompletedDate() {
		return completedDate;
	}
	public void setCompletedDate(Date completedDate) {
		this.completedDate = completedDate;
	}
	public Date getRequestedDate() {
		return requestedDate;
	}
	public void setRequestedDate(Date requestedDate) {
		this.requestedDate = requestedDate;
	}
	public Date getReceivedDate() {
		return receivedDate;
	}
	public void setReceivedDate(Date receivedDate) {
		this.receivedDate = receivedDate;
	}
	public String getComponentStatus() {
		return componentStatus;
	}
	public void setComponentStatus(String componentStatus) {
		this.componentStatus = componentStatus;
	}
	public Long getPersonId() {
		return personId;
	}
	public void setPersonId(Long personId) {
		this.personId = personId;
	}
	
}
