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

import ke.co.tawi.babblesms.server.beans.StorableBeanByUUID;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang3.StringUtils;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;


/**
 * A country.
 * <p>
 *  
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */
@Entity
@Table( name = "country" )
@Cache(usage=CacheConcurrencyStrategy.READ_ONLY)
public class Country extends StorableBeanByUUID implements Comparable<Country> {

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
	 * 
	 * @param o
	 * @return int
	 */
	@Override
	public int compareTo(Country o) {		
		return name.compareTo(((Country) o).getName());
	}


	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		boolean isEqual = false;
		
		if(obj instanceof Country) {	
			Country type = (Country)obj;
			
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
		builder.append("Country [uuid()=");
		builder.append(getUuid());
		builder.append(", name=");
		builder.append(name);
		builder.append(", codeFIPS=");
		builder.append(codefips);
		builder.append("]");
		return builder.toString();
	}
}
