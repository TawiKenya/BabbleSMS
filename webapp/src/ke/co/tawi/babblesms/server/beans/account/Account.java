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

import ke.co.tawi.babblesms.server.beans.StorableBean;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;

/**
 * An account holder on the BabbleSMS platform.
 * <p>
 *  
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */
public class Account extends StorableBean {

    private static final long serialVersionUID = 1L;

    private String username;
    private String logpassword;
    private String usertype;
    private String name;
    private String mobile;
    private String email;
    private int dailysmslimit;
    private Date creationdate;
    private String statusuuid;

    /**
     *
     */
    public Account() {
        super();
        username = "";
        logpassword = "";
        usertype = "";
        name = "";
        mobile = "";
        email = "";
        dailysmslimit =0;
        creationdate = new Date();
        statusuuid = "";
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
     * @param usertype 
     */
    public void setUsertype(String usertype) {
        this.usertype = StringUtils.trimToEmpty(usertype);
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
        return creationdate;
    }

    /**
     * 
     * @param creationdate 
     */
    public void setCreationdate(Date creationdate) {
        this.creationdate = new Date(creationdate.getTime());
    }

    /**
     * 
     * @return statusuuid
     */
    public String getStatusuuid() {
        return statusuuid;
    }

    /**
     * 
     * @param statusuuid 
     */
    public void setStatusuuid(String statusuuid) {
        this.statusuuid = StringUtils.trimToEmpty(statusuuid);
    }



public void validate(){



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
		builder.append(", logpassword=");
		builder.append(logpassword);
		builder.append(", usertype=");
		builder.append(usertype);
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
		builder.append(", statusuuid=");
		builder.append(statusuuid);
		builder.append("]");
		return builder.toString();
	}
}
