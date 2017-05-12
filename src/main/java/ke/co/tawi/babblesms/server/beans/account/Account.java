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

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang3.StringUtils;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * An account holder on the BabbleSMS platform.
 * <p>
 *  
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */
@Entity
@Table( name = "account" )
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class Account extends StorableBeanByUUID {

    private String username;
    private String logpassword;
    private String name;
    private String mobile;
    private String email;
    private int dailysmslimit;
    private Date creationdate;    
    private String apiUsername;
    private String apiPassword;
    
    @ManyToOne
	@JoinColumn(name="statusUuid", referencedColumnName="uuid")
    private Status status;
    
    
    /**
     *
     */
    public Account() {
        super();
        username = "";
        logpassword = "";
        name = "";
        mobile = "";
        email = "";
        dailysmslimit = 0;
        creationdate = new Date();        
        apiUsername = "";
        apiPassword = "";
        
        status = new Status();
    }

    
    /**
     *
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     *
     * @param username
     */
    public void setUsername(String username) {
        this.username = StringUtils.trimToEmpty(username);
    }

    /**
     *
     * @return logpassword
     */
    public String getLogpassword() {
        return logpassword;
    }

    /**
     *
     * @param logpassword
     */
    public void setLogpassword(String logpassword) {
        this.logpassword = StringUtils.trimToEmpty(logpassword);
    }
    

    /**
     * 
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * 
     * @param name 
     */
    public void setName(String name) {
        this.name = StringUtils.trimToEmpty(name);
    }

    /**
     * 
     * @return mobile
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * 
     * @param mobile 
     */
    public void setMobile(String mobile) {
        this.mobile = StringUtils.trimToEmpty(mobile);
    }

    /**
     * 
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     * 
     * @param email 
     */
    public void setEmail(String email) {
        this.email = StringUtils.trimToEmpty(email);
    }

    /**
     * 
     * @return dailysmslimit
     */
    public int getDailysmslimit() {
        return dailysmslimit;
    }

    /**
     * 
     * @param dailysmslimit 
     */
    public void setDailysmslimit(int dailysmslimit) {
        this.dailysmslimit = dailysmslimit;
    }

    /**
     * 
     * @return creationdate
     */
    public Date getCreationdate() {
        return new Date(creationdate.getTime());
    }

    /**
     * 
     * @param creationdate 
     */
    public void setCreationdate(Date creationdate) {
        this.creationdate = new Date(creationdate.getTime());
    }
    

	/**
	 * @return the apiUsername
	 */
	public String getApiUsername() {
		return apiUsername;
	}

	/**
	 * @param apiUsername the apiUsername to set
	 */
	public void setApiUsername(String apiUsername) {
		this.apiUsername = StringUtils.trimToEmpty(apiUsername);
	}

	/**
	 * @return the apiPassword
	 */
	public String getApiPassword() {
		return apiPassword;
	}

	/**
	 * @param apiPassword the apiPassword to set
	 */
	public void setApiPassword(String apiPassword) {
		this.apiPassword = StringUtils.trimToEmpty(apiPassword);
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
        boolean isEqual = false;
		
		if(obj instanceof Account) {	
			Account type = (Account)obj;
			
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
		builder.append("Account [getUuid()=");
		builder.append(getUuid());
		builder.append(", username=");
		builder.append(username);
		builder.append(", name=");
		builder.append(name);
		builder.append(", mobile=");
		builder.append(mobile);
		builder.append(", email=");
		builder.append(email);
		builder.append(", dailysmslimit=");
		builder.append(dailysmslimit);
		builder.append(", creationdate=");
		builder.append(creationdate);
		builder.append(", apiUsername=");
		builder.append(apiUsername);
		builder.append(", status=");
		builder.append(status.getDescription());
		builder.append("]");
		return builder.toString();
	}	
	
}
