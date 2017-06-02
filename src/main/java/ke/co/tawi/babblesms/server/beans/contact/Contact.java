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
import ke.co.tawi.babblesms.server.beans.account.Account;
import ke.co.tawi.babblesms.server.beans.account.Status;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.CascadeType;
import javax.persistence.FetchType;

import org.apache.commons.lang3.StringUtils;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Contact within an Address Book.
 * <p>
 *  
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */
@Entity
@Table( name = "contact" )
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class Contact extends StorableBeanById implements Comparable<Contact> {

    private String name;
    private String description;
    
    
    private String uuid;
    
    @ManyToOne
	@JoinColumn(name="accountUuid", referencedColumnName="uuid")
    private Account account;
    
    @ManyToOne
	@JoinColumn(name="statusUuid", referencedColumnName="uuid")
    private Status status;
    
    @OneToMany(mappedBy="contact", cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
	private Set<Phone> phones;
    
    @OneToMany(mappedBy="contact", cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
	private Set<Email> emails;
    
    @ManyToMany(mappedBy="contacts", fetch = FetchType.EAGER)
    private Set<Group> groups = new HashSet<Group>();
    
    /**
     * 
     */
    public Contact() {
        super();
        name = "";
        description = "";
        uuid = UUID.randomUUID().toString();
        
        account = new Account();
        status = new Status();      
    }


    /**
	 * @return the name
	 */
	public String getName() {
		return name;
	}


	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = StringUtils.trimToEmpty(name);
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
		this.description = StringUtils.trimToEmpty(description);
	}


	/**
	 * @return the account
	 */
	public Account getAccount() {
		return account;
	}


	/**
	 * @param account the account to set
	 */
	public void setAccount(Account account) {
		this.account = account;
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
	 * @return the phones
	 */
	public Set<Phone> getPhones() {
		return phones;
		//return null;
	}


	/**
	 * @param phones the phones to set
	 */
	public void setPhones(Set<Phone> phones) {
		this.phones = phones;
	}


	/**
	 * @return the emails
	 */
	public Set<Email> getEmails() {
		return emails;
		//return null;
	}


	/**
	 * @param emails the emails to set
	 */
	public void setEmails(Set<Email> emails) {
		this.emails = emails;
	}


	/**
	 * @return the groups
	 */
	public Set<Group> getGroups() {
		return groups;
	}


	/**
	 * @param groups the groups to set
	 */
	public void setGroups(Set<Group> groups) {
		this.groups = groups;
	}
	
	
	/**
	 * @return the contactUuid
	 */
	public String getUuid() {
		return uuid;
	}


	/**
	 * @param contactUuid the contactUuid to set
	 */
	public void setUuid(String contactUuid) {
		this.uuid = contactUuid;
	}
	
	
	/**
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Contact c) {
		return name.compareTo(((Contact) c).getName());
	}
	
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		boolean isEqual = false;
		
		if(obj instanceof Contact) {	
			Contact type = (Contact)obj;
			
			isEqual = type.getUuid().equals(uuid);		
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
		builder.append("Contact [Id=");
		builder.append(getId());
		builder.append(", name=");
		builder.append(name);
		builder.append(", description=");
		builder.append(description);
		builder.append(", uuid=");
		builder.append(uuid);
		builder.append(", account=");
		builder.append(account.getUsername());
		builder.append(", status=");
		builder.append(status.getDescription());
		builder.append("]");
		return builder.toString();
	}


	/** For Serialization */
	public static final long serialVersionUID = new Random().nextLong();
	
}
