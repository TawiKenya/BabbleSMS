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
package ke.co.tawi.babblesms.server.beans.notification;

import ke.co.tawi.babblesms.server.beans.StorableBean;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;


/**
 * A notification.
 * <p>
 *  
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */
public class Notification extends StorableBean {

    private String origin;
    private String shortDesc;
    private String longDesc;
    private String published;
    private Date notificationDate;

    /**
     *
     */
    public Notification() {
        super();
        origin = "";
        shortDesc = "";
        longDesc = "";
        published="";
        notificationDate = new Date();
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
        this.origin = origin;
    }

    
    /**
     * @return the shortDesc
     */
    public String getShortDesc() {
        return shortDesc;
    }

    /**
     * @param shortDesc - the shortDesc to set
     */
    public void setShortDesc(String shortDesc) {
        this.shortDesc = StringUtils.trimToEmpty(shortDesc);
    }

    /**
     * @return the longDesc
     */
    public String getLongDesc() {
        return longDesc;
    }

    /**
     * @param longDesc - the longDesc to set
     */
    public void setLongDesc(String longDesc) {
        this.longDesc = StringUtils.trimToEmpty(longDesc);
    }
    
    /**
     * @return the published
     */
    public String getPublished() {
        return published;
    }

    /**
     * @param published - the published to set
     */
    public void setPublished(String published) {
        this.published = StringUtils.trimToEmpty(published);
    }

    /**
     * @return the notificationDate
     */
    public Date getNotificationDate() {
        return new Date(notificationDate.getTime());
    }

    /**
     * @param notificationDate - the notificationDate to set
     */
    public void setNotificationDate(Date notificationDate) {
        this.notificationDate = notificationDate;
    }

    /**
     * @return builder
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {

        StringBuilder builder = new StringBuilder();

        builder.append("Notification [id=");
        builder.append(getId());
        builder.append(", uuid=");
        builder.append(getUuid());
        builder.append(", origin=");
        builder.append(origin);
        builder.append(", shortDesc=");
        builder.append(shortDesc);
        builder.append(", longDesc=");
        builder.append(longDesc);
        builder.append(", notificationDate=");
        builder.append(notificationDate);
        builder.append("]");

        return builder.toString();
    }

}
