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
package ke.co.tawi.babblesms.server.beans.maskcode;

import org.apache.commons.lang3.StringUtils;

/**
 * An SMS Mask
 * <p>
 *  
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */
public class Mask extends SMSSource {


    /**
     * 
     */
    public Mask() {
        super();
    }

    
    /**
     * @return the mask
     */
    public String getMaskname() {
        return getSource();
    }

    
    /**
     * @param maskname
     */
    public void setMaskname(String maskname) {
    	setSource(StringUtils.trimToEmpty(maskname));
    }

    
    /**
     * @see ke.co.tawi.babblesms.server.beans.maskcode.SMSSource#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Mask ");
        builder.append("[id=");
        builder.append(getId());
        builder.append(", uuid=");
        builder.append(getUuid());
        builder.append(", maskname=");
        builder.append(getSource());
        builder.append(", accountuuid=");
        builder.append(getAccountuuid());
        builder.append(", networkuuid=");
        builder.append(getNetworkuuid());
        builder.append("]");
        return builder.toString();
    }

}
