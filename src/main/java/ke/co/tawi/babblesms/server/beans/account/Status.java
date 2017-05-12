/**
 * Copyright 2015 Tawi Commercial Services Ltd
 * 
 * Licensed under the Open Software License, Version 3.0 (the “License”); you may
 * not use this file except in compliance with the License. You may obtain a copy
 * of the License at:
 * http://opensource.org/licenses/OSL-3.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the License is distributed on an “AS IS” BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied.
 * 
 * See the License for the specific language governing permissions and limitations
 * under the License.
 */
package ke.co.tawi.babblesms.server.beans.account;

import ke.co.tawi.babblesms.server.beans.StorableBeanByUUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * The status of an account holder on the system, for example Active & 
 * Suspended. Can also be used elsewhere e.g. Status of a shortcode
 * <p>
 * Copyright (c) Tawi Commercial Services Ltd., Dec 29, 2011  
 * 
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 * 
 */
@Entity
@Table( name = "status" )
@Cache(usage=CacheConcurrencyStrategy.READ_ONLY)
public class Status extends StorableBeanByUUID {
	
	// These match what is in the SQL table "AccountStatus"
	public final static String STATUS_ACTIVE = "acecb9fa-7e21-455d-8abb-c61a840cdbec";
	public final static String STATUS_TEST = "91bb7fb1-00d8-496d-97d8-e18e78ffc8d3";
	
	@Column(name="description", unique=true)
	private String description;
	
	
	/**
	 * 
	 */
	public Status() {
		super();
		description = "";
	}
		

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}


	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		boolean isEqual = false;
		
		if(obj instanceof Status) {	
			Status type = (Status)obj;
			
			isEqual = type.getUuid().equals(getUuid());		
		}
		
		return isEqual;		
	}
	
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return getUuid().hashCode();
	}
	
	
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Status [uuid=");
		builder.append(getUuid());
		builder.append(", description=");
		builder.append(description);
		builder.append("]");
		return builder.toString();
	}	
}
