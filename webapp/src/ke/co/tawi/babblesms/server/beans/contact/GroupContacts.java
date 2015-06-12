package ke.co.tawi.babblesms.server.beans.contact;

import java.util.Comparator;

import ke.co.tawi.babblesms.server.beans.StorableBean;

import org.apache.commons.lang3.StringUtils;

public class GroupContacts extends StorableBean {

    private String uuid;
    private String contactuuid;
    private String accountsuuid;
        
    
    /**
     * 
     */
    public GroupContacts() {
        super();
        uuid = "";
        contactuuid= "";        
        accountsuuid = "";
       
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = StringUtils.trimToEmpty(uuid);
    }
    
    public String getContactUuid() {
        return contactuuid;
    }

    public void setContactUuid(String contactuuid) {
        this.contactuuid = StringUtils.trimToEmpty(contactuuid);
    }    
   
    public String getAccountsuuid() {
        return accountsuuid;
    }

    public void setAccountsuuid(String accountsuuid) {
        this.accountsuuid = StringUtils.trimToEmpty(accountsuuid);
    }      
        

   
    
    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Contact ");
        builder.append("[uuid=");
        builder.append(getUuid());
        builder.append(", contactuuid=");
        builder.append(contactuuid);
        builder.append(", accountuuid=");
        builder.append(accountsuuid);        
        builder.append("]");
        return builder.toString();
    }

}

