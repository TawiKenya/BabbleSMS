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

import ke.co.tawi.babblesms.server.beans.StorableBean;

import java.util.Comparator;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

/**
 * A Group is a number of {@link Contact}s.
 * <p>
 *  
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */
public class Group extends StorableBean {

    private String statusuuid;
    private String description;
    private String accountsuuid;
    private String name;
    private Date creationdate;
    
    
    /**
     * 
     */
    public Group() {
        super();
        statusuuid = "";
        name = "";
        description = "";
        accountsuuid = "";
        creationdate= new Date();
    }

    public String getStatusuuid() {
        return statusuuid;
    }

    public void setStatusuuid(String statusuuid) {
        this.statusuuid = StringUtils.trimToEmpty(statusuuid);
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = StringUtils.trimToEmpty(name);
    }
    
    public Date getCreationdate() {
        return new Date(creationdate.getTime());
    }

    public void setCreationdate(Date date) {
        creationdate = new Date(date.getTime());
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = StringUtils.trimToEmpty(description);
    }

    public String getAccountsuuid() {
        return accountsuuid;
    }

    public void setAccountsuuid(String accountsuuid) {
        this.accountsuuid = StringUtils.trimToEmpty(accountsuuid);
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
    
    
    
    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Group ");
        builder.append("[uuid=");
        builder.append(getUuid());
        builder.append(", description=");
        builder.append(description);        
        builder.append(", name=");
        builder.append(name);
        builder.append(", creationdate=");
        builder.append(creationdate);
        builder.append(", accountsuuid=");
        builder.append(accountsuuid);
        builder.append(", statusuuid=");
        builder.append(statusuuid);
        builder.append("]");
        return builder.toString();
    }

}
