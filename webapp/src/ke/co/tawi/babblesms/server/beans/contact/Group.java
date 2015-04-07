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

import java.util.Comparator;
import java.util.Date;

import ke.co.tawi.babblesms.server.beans.StorableBean;

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
        return creationdate;
    }

    public void setCreationdate(Date creationdate) {
        this.creationdate = new Date(creationdate.getTime());
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
    /*Comparator for sorting the list by Group Name*/
    public static  Comparator<Group> GroupNameComparator = new Comparator<Group>() {

    	public int compare(Group s1, Group s2) {
    	   String groupName1 = s1.getName().toUpperCase();
    	   String groupName2 = s2.getName().toUpperCase();

    	   //ascending order
    	   return groupName1.compareTo(groupName2);

    	   //descending order
    	   //return groupName2.compareTo(groupName1);
        }};
    
    
    
    
    
    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CGroup ");
        builder.append("[uuid=");
        builder.append(getUuid());
        builder.append(", description=");
        builder.append(description);
        builder.append(", stausuuid=");
        builder.append(statusuuid);
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
