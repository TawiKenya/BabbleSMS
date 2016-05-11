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

import org.apache.commons.lang3.StringUtils;


/**
 * A generic status.
 * <p>
 *  
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */
public class Status extends StorableBean {

	
	/*
	 * These match what is in the SQL table "status"
	 */
	public final static String ACTIVE = "396F2C7F-961C-5C12-3ABF-867E7FD029E6";
	public final static String SUSPENDED = "19CAAC90-0D72-59D4-1DC1-2C86808459F9";
	public final static String PENDING  = "5A13538F-AC41-FDE2-4CD6-B939FA03123B";
	public final static String UNKNOWN = "8E1DEF0F-4DCC-E13B-F89D-35181AD4003D";
	
    private String description;

    
    /**
     * 
     */
    public Status() {
        super();
        
        description = "";
    }
    

    /**
     *
     * @return the description
     */
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

    
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Status [getUuid()=");
		builder.append(getUuid());
		builder.append(", description=");
		builder.append(description);
		builder.append("]");
		return builder.toString();
	}
	
	
	private static final long serialVersionUID = 1904746514478837017L;
}
