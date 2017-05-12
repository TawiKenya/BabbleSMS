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
import ke.co.tawi.babblesms.server.beans.network.Network;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang3.StringUtils;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A phone number belonging to a {@link Contact}
 * <p>
 *  
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */
@Entity
@Table( name = "phone" )
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class Phone extends StorableBeanById {
	
	private String uuid;
    private String phoneNumber;
    
    @ManyToOne
	@JoinColumn(name="contactUuid", referencedColumnName="uuid")
    private Contact contact;
    
    @ManyToOne
	@JoinColumn(name="statusUuid", referencedColumnName="uuid")
    private Status status;
    
    @ManyToOne
	@JoinColumn(name="networkUuid", referencedColumnName="uuid")
    private Network network;
        
    
    /**
     * 
     */
    public Phone() {
        super();
        
        phoneNumber = "";
        uuid = UUID.randomUUID().toString();
        
        contact = new Contact();
        status = new Status();        
        network = new Network();
    }
    

    /**
     * @return the phone number
     */
    public String getPhonenumber() {
        return phoneNumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phoneNumber = StringUtils.trimToEmpty(phonenumber);
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
	 * @return the phoneNumber
	 */
	public String getPhoneNumber() {
		return phoneNumber;
	}


	/**
	 * @param phoneNumber the phoneNumber to set
	 */
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
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
	 * @return the network
	 */
	public Network getNetwork() {
		return network;
	}


	/**
	 * @param network the network to set
	 */
	public void setNetwork(Network network) {
		this.network = network;
	}
	
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		
		Phone phone;
		
		if(obj instanceof Phone) {
			phone = (Phone) obj;
			
			return uuid.equals(phone.getUuid());
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
		builder.append("Phone [Id=");
		builder.append(getId());
		builder.append(", uuid=");
		builder.append(uuid);
		builder.append(", phoneNumber=");
		builder.append(phoneNumber);
		builder.append(", contact=");
		builder.append(contact.getName());
		builder.append(", status=");
		builder.append(status.getDescription());
		builder.append(", network=");
		builder.append(network.getName());
		builder.append("]");
		return builder.toString();
	}	
	
}
