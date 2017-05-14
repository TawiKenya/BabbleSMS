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

import java.util.Comparator;
import java.util.Date;
import java.util.Random;
import java.util.UUID;
import java.util.Set;
import java.util.HashSet;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.FetchType;

import org.apache.commons.lang3.StringUtils;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Group is a number of {@link Contact}s.
 * <p>
 *  
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */
@Entity
@Table( name = "groups" )
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class Group extends StorableBeanById {
    
	private String uuid;
    private String description;
    private String name;
    private Date creationDate;
    
    @ManyToOne
	@JoinColumn(name="accountUuid", referencedColumnName="uuid")
    private Account account;
    
    @ManyToOne
	@JoinColumn(name="statusUuid", referencedColumnName="uuid")
    private Status status;
    
    
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "contact_group", joinColumns = { @JoinColumn(name = "groupUuid") }, 
    	inverseJoinColumns = { @JoinColumn(name = "contactUuid") })
    Set<Contact> contacts;
    
    
    /**
     * 
     */
    public Group() {
        super();
        
        uuid = UUID.randomUUID().toString();
        name = "";
        description = "";
        
        creationDate = new Date();
        
        account = new Account();
        status = new Status(); 
        
        contacts = new HashSet<Contact>();
    }

    
    /**
     * Comparator for sorting a list by Group Name
     */
    public static Comparator<Group> GroupNameComparator = new Comparator<Group>() {
	
		/**
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		@Override
		public int compare(Group s1, Group s2) {
			
		   return s1.getName().compareTo(s2.getName());
    	}
	};
	
    
    public String getName() {
        return name;
    }

    
    public void setName(String name) {
        this.name = StringUtils.trimToEmpty(name);
    }
    

    public String getDescription() {
        return description;
    }
    

    public void setDescription(String description) {
        this.description = StringUtils.trimToEmpty(description);
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
	 * @return the creationDate
	 */
	public Date getCreationDate() {
		return creationDate;
	}


	/**
	 * @param creationDate the creationDate to set
	 */
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
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
	 * @return the contacts
	 */
	public Set<Contact> getContacts() {
		return contacts;
	}


	/**
	 * @param contacts the contacts to set
	 */
	public void setContacts(Set<Contact> contacts) {
		this.contacts = contacts;
	}
	
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		boolean isEqual = false;
		
		if(obj instanceof Group) {	
			Group type = (Group)obj;
			
			isEqual = type.getUuid().equals(getId());		
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
		builder.append("Group [Id=");
		builder.append(getId());
		builder.append(", uuid=");
		builder.append(uuid);
		builder.append(", description=");
		builder.append(description);
		builder.append(", name=");
		builder.append(name);
		builder.append(", creationDate=");
		builder.append(creationDate);
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
