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
package ke.co.tawi.babblesms.server.beans.log;

import ke.co.tawi.babblesms.server.beans.StorableBean;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;

/**
 * Represents an incoming or outgoing SMS..
 * <p>
 *  
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */
public class Log extends StorableBean {

    private static final long serialVersionUID = 1L;

    private String origin;
    private String message;
    private String destination;
    private String networkUuid;
    private Date logTime;

    
    /**
     *
     */
    public Log() {
        super();
        origin = "";
        message = "";
        destination = "";
        networkUuid = "";
        logTime = new Date();
    }

    /**
     *
     * @return origin
     */
    public String getOrigin() {
        return origin;
    }

    /**
     *
     * @param origin
     */
    public void setOrigin(String origin) {
        this.origin = StringUtils.trimToEmpty(origin);
    }

    /**
     *
     * @return message
     */
    public String getMessage() {
        return message;
    }

    /**
     *
     * @param message
     */
    public void setMessage(String message) {
        this.message = StringUtils.trimToEmpty(message);
    }

    /**
     *
     * @return destination
     */
    public String getDestination() {
        return destination;
    }

    /**
     *
     * @param destination
     */
    public void setDestination(String destination) {
        this.destination = StringUtils.trimToEmpty(destination);
    }

	/**
	 * @return the logTime
	 */
	public Date getLogTime() {            
		return new Date(logTime.getTime());                
	}

	/**
	 * @param logTime the logTime to set
	 */
	public void setLogTime(Date t) {            
		this.logTime = new Date(t.getTime());                
	}

	/**
	 * @return the networkUuid
	 */
	public String getNetworkUuid() {
		return networkUuid;
	}

	/**
	 * @param networkUuid the networkUuid to set
	 */
	public void setNetworkUuid(String networkUuid) {
		this.networkUuid = networkUuid;
	}
}
