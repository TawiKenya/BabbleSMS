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

import ke.co.tawi.babblesms.server.beans.StorableBean;

import org.apache.commons.lang3.StringUtils;

/**
 * An SMS Mask
 * <p>
 *  
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */
public class Mask extends StorableBean {

    private String maskname;
    private String accountuuid;
    private String networkuuid;

    public Mask() {
        super();
        maskname = "";
        accountuuid = "";
        networkuuid = "";
    }

    public String getAccountuuid() {
         System.out.println(accountuuid);
        return accountuuid;
    }

    public void setAccountuuid(String accountuuid) {
        //System.out.println(accountuuid);
        this.accountuuid = StringUtils.trimToEmpty(accountuuid);
    }

    public String getNetworkuuid() {
        return networkuuid;
    }

    public void setNetworkuuid(String networkuuid) {
        this.networkuuid = StringUtils.trimToEmpty(networkuuid);
    }

    public String getMaskname() {
        return maskname;
    }

    public void setMaskname(String maskname) {
        this.maskname = StringUtils.trimToEmpty(maskname);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Mask ");
        builder.append("[id=");
        builder.append(getId());
        builder.append(", uuid=");
        builder.append(getUuid());
        builder.append(", maskname=");
        builder.append(maskname);
        builder.append(", accountuuid=");
        builder.append(accountuuid);
        builder.append(", networkuuid=");
        builder.append(networkuuid);
        builder.append("]");
        return builder.toString();
    }

}
