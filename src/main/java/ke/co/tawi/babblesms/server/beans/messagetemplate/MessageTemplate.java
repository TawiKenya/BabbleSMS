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

/**
 * An SMS message template.
 * <p>
 *  
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */
public class MessageTemplate extends StorableBean {

    private String title;
    private String contents;
    private String accountuuid;

    /**
     * 
     */
    public MessageTemplate() {
        super();
        title = "";
        contents = "";
        accountuuid = "";
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String templatetitle) {
        this.title = templatetitle;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getAccountuuid() {
        return accountuuid;
    }

    public void setAccountuuid(String accountuuid) {
        this.accountuuid = accountuuid;
    }

    
    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Messagetemplate ");
        builder.append("[uuid=");
        builder.append(getUuid());
        builder.append(", title=");
        builder.append(title);
        builder.append(", contents=");
        builder.append(contents);
        builder.append(", accountuuid=");
        builder.append(accountuuid);
        builder.append("]");
        return builder.toString();
    }
}
