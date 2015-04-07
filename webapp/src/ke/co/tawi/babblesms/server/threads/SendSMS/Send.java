package ke.co.tawi.babblesms.server.threads.SendSMS;

import ke.co.tawi.babblesms.server.beans.StorableBean;
import org.apache.commons.lang3.StringUtils;

/**
 * Copyright (c) Tawi Commercial Services Ltd., Feb 11, 2014
 *
 * @author <a href="mailto:Erickm@tawi.mobi">Erick Murimi</a>
 * @version %I%, %G%
 */
public class Send extends StorableBean {

    private String source;
    private String destination;
    private String message;
    private String networkuuid;
    private String messagestatusuuid;

    public Send() {
        super();
        source = "";
        destination = "";
        message = "";
        networkuuid = "";
        messagestatusuuid = "";
    }

    /**
     *
     * @return
     */
    public String getSource() {
        return source;
    }

    /**
     *
     * @param source
     */
    public void setSource(String source) {
        this.source = StringUtils.trimToEmpty(source);
    }

    /**
     *
     * @return
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
     *
     * @return
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
     * @return
     */
    public String getNetworkuuid() {
        return networkuuid;
    }

    /**
     *
     * @param networkuuid
     */
    public void setNetworkuuid(String networkuuid) {
        this.networkuuid = StringUtils.trimToEmpty(networkuuid);
    }

    /**
     *
     * @return
     */
    public String getMessagestatusuuid() {
        return messagestatusuuid;
    }

    /**
     *
     * @param messagestatusuuid
     */
    public void setMessagestatusuuid(String messagestatusuuid) {
        this.messagestatusuuid = StringUtils.trimToEmpty(messagestatusuuid);
    }

    /**
     * @return @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Send ");
        builder.append("[id=");
        builder.append(getId());
        builder.append(", uuid=");
        builder.append(getUuid());
        builder.append(", source=");
        builder.append(source);
        builder.append(", destination=");
        builder.append(destination);
        builder.append(",message=");
        builder.append(message);
        builder.append(", networkuuid=");
        builder.append(networkuuid);
        builder.append(", messagestatusuuid=");
        builder.append(messagestatusuuid);
        builder.append("]");
        return builder.toString();
    }

}
