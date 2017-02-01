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
package ke.co.tawi.babblesms.server.beans.geolocation;

import org.apache.commons.lang3.StringUtils;

import ke.co.tawi.babblesms.server.beans.StorableBean;

/**
 * A country.
 * <p>
 *  
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */
public class Country extends StorableBean {

    private String name;
    private String codefips;

    
    /**
     * 
     */
    public Country() {
        super();
        name = "";
        codefips = "";
    }

    
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    
    /**
     * @param name
     */
    public void setName(String name) {
        this.name = StringUtils.trimToEmpty(name);
    }
    
    
    /**
     * @return the Code FIPS
     */
    public String getCodefips() {
        return codefips;
    }

    /**
     * @param codefips
     */
    public void setCodefips(String codefips) {
        this.codefips = StringUtils.trimToEmpty(codefips);
    }

    
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Country [getUuid()=");
		builder.append(getUuid());
		builder.append(", name=");
		builder.append(name);
		builder.append(", codefips=");
		builder.append(codefips);
		builder.append("]");
		return builder.toString();
	}    
}
