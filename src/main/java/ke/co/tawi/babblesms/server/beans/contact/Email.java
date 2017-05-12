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
package ke.co.tawi.babblesms.server.beans.contact;

import ke.co.tawi.babblesms.server.beans.StorableBeanById;
import ke.co.tawi.babblesms.server.beans.account.Status;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang3.StringUtils;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table( name = "email" )
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
/**
 * An email belonging to a {@link Contact}
 * <p>
 *  
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */
public class Email extends StorableBeanById {

	private String uuid;	
    private String address;
    
    @ManyToOne
	@JoinColumn(name="contactUuid", referencedColumnName="uuid")
    private Contact contact;
    
    @ManyToOne
	@JoinColumn(name="statusUuid", referencedColumnName="uuid")
    private Status status;

    /**
     * 
     */
    public Email() {
        super();
        
        address = "";
        uuid = UUID.randomUUID().toString();
        
        contact = new Contact();
        status = new Status();  
    }

    
    /**
     * @return an email address
     */
    public String getAddress() {
        return address;
    }

    
    
    /**
     * @param address
     */
    public void setAddress(String address) {
        this.address = StringUtils.trimToEmpty(address);
    }


	/**
	 * @return the uuid
	 */
	public String getUuid() {
		return uuid;
	}


	/**
	 * @param uuid the uuid to set
	 */
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}


	/**
	 * @return the contact
	 */
	public Contact getContact() {
		return contact;
	}


	/**
	 * @param contact the contact to set
	 */
	public void setContact(Contact contact) {
		this.contact = contact;
	}


	/**
	 * @return the status
	 */
	public Status getStatus() {
		return status;
	}


	/**
	 * @param status the status to set
	 */
	public void setStatus(Status status) {
		this.status = status;
	}

        
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		
		Email email;
		
		if(obj instanceof Phone) {
			email = (Email) obj;
			
			return uuid.equals(email.getUuid());
		}
		
		return false;
	}


	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return uuid.hashCode();
	}


	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Email [Id=");
		builder.append(getId());
		builder.append(", uuid=");
		builder.append(uuid);
		builder.append(", address=");
		builder.append(address);
		builder.append(", contact=");
		builder.append(contact.getName());
		builder.append(", status=");
		builder.append(status.getDescription());
		builder.append("]");
		return builder.toString();
	}
}
