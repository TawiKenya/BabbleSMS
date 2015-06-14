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
package ke.co.tawi.babblesms.server.beans.messagetemplate;

import ke.co.tawi.babblesms.server.beans.StorableBean;

import org.apache.commons.lang3.StringUtils;

/**
 * A message status.
 * <p>
 *  
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */
public class MsgStatus extends StorableBean {
	
	/*
	 * These correspond to the SQL table 'messagestatus'
	 */	
	public static final String PENDING = "04C7CD60-9CC2-1B1A-EB6B-4A3E1FA8AC38";
	public static final String SENT = "2F4AF191-8557-86C5-5D72-47DD44D303B1";
	public static final String REJECTED = "49229BA2-91E5-7E64-F49C-923B7927C40D";
	public static final String RECEIVED = "BBA890E6-FDA9-77B0-03B7-1F6B000C85DD";

	
    private String description;
    
    public MsgStatus() {
        super();
        description = "";
    }
    
    public String getDescription() {
        return description;
    }

    /**
     *
     * @param description
     */
    public void setDescription(String description) {
        this.description = StringUtils.trimToEmpty(description);
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("MsgStatus ");
        builder.append("[id=");
        builder.append(getId());
        builder.append(", uuid=");
        builder.append(getUuid());
        builder.append(", description=");
        builder.append(description);
        builder.append("]");
        return builder.toString();
    }
    
}
