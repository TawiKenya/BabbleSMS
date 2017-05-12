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
package ke.co.tawi.babblesms.server.beans.network;

import ke.co.tawi.babblesms.server.beans.StorableBeanByUUID;
import ke.co.tawi.babblesms.server.beans.geolocation.Country;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang3.StringUtils;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A mobile network operator (MNO).
 * <p>
 *  
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */
@Entity
@Table( name = "network" )
@Cache(usage=CacheConcurrencyStrategy.READ_ONLY)
public class Network extends StorableBeanByUUID {

	// This is to match what is in the RDBMS table 'network'
	public final static String YU_KE = "741AC38C-3E40-6887-9CD6-365EF9EA1EF0";
	public final static String AIRTEL_KE = "0DE968C9-7309-C481-58F7-AB6CDB1011EF";
	public final static String ORANGE_KE = "5C1D9939-136A-55DE-FD0E-61D8204E17C9";
	public final static String SAFARICOM_KE = "B936DA83-8A45-E9F0-2EAE-D75F5C232E78";
	
    private String name;
    
    @ManyToOne
	@JoinColumn(name="countryUuid", referencedColumnName="uuid")
    private Country country;

    /**
     * 
     */
    public Network() {
        super();
        name = "";
        country = new Country();
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
	 * @return the country
	 */
	public Country getCountry() {
		return country;
	}


	/**
	 * @param country the country to set
	 */
	public void setCountry(Country country) {
		this.country = country;
	}	
	
		
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {        
        boolean isEqual = false;
		
		if(obj instanceof Network) {	
			Network type = (Network)obj;
			
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
		builder.append("Network [getUuid()=");
		builder.append(getUuid());
		builder.append(", name=");
		builder.append(name);
		builder.append(", country=");
		builder.append(country.getName());
		builder.append("]");
		return builder.toString();
	}	
	
}
