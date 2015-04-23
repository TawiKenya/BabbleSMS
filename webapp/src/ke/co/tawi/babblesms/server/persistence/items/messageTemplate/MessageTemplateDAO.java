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
package ke.co.tawi.babblesms.server.persistence.items.messageTemplate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.BeanProcessor;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;

import ke.co.tawi.babblesms.server.beans.messagetemplate.MessageTemplate;
import ke.co.tawi.babblesms.server.persistence.GenericDAO;

/**
 * Copyright (c) Tawi Commercial Services Ltd., Jun 27, 2013
 *
 * @author <a href="mailto:japhethk@tawi.mobi">Japheth Korir</a>
 * @version %I%, %G%
 */
public class MessageTemplateDAO extends GenericDAO implements BabbleMessageTemplateDAO {

    private static MessageTemplateDAO messageTemplateDAO;

    private final Logger logger;

    public static MessageTemplateDAO getInstance() {
        if (messageTemplateDAO == null) {
            messageTemplateDAO = new MessageTemplateDAO();
        }
        return messageTemplateDAO;
    }

    /**
     *
     */
    protected MessageTemplateDAO() {
        super();
        logger = Logger.getLogger(this.getClass());
    }

    /**
     * Used for testing purposes only.
     *
     * @param dbName
     * @param dbHost
     * @param dbUsername
     * @param dbPassword
     * @param dbPort
     */
    public MessageTemplateDAO(String dbName, String dbHost, String dbUsername,
            String dbPassword, int dbPort) {
        super(dbName, dbHost, dbUsername, dbPassword, dbPort);
        logger = Logger.getLogger(this.getClass());
    }

    /**
     *
     */
    @Override
    public boolean putMessageTemplate(MessageTemplate messageTemplate) {
        boolean success = true;

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = dbCredentials.getConnection();
            pstmt = conn.prepareStatement("INSERT INTO MessageTemplate (Uuid,title,contents,accountuuid) VALUES (?,?,?,?);");
            pstmt.setString(1, messageTemplate.getUuid());
            pstmt.setString(2, messageTemplate.getTitle());
            pstmt.setString(3, messageTemplate.getContents());
            pstmt.setString(4, messageTemplate.getAccountuuid());
            
            System.out.println(messageTemplate);

            pstmt.execute();

        } catch (SQLException e) {
            logger.error("SQL Exception when trying to put messageTemplate: " + messageTemplate);
            logger.error(ExceptionUtils.getStackTrace(e));
            success = false;
        } finally {
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException e) {
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                }
            }
        }
        return success;
    }

    
    /**
     *
     */
    @Override
    public MessageTemplate getMessageTemplate(String uuid) {
        MessageTemplate messageTemplate = null;

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        BeanProcessor b = new BeanProcessor();
        
        try {
            conn = dbCredentials.getConnection();
            pstmt = conn.prepareStatement("SELECT * FROM MessageTemplate WHERE Uuid = ?;");
            pstmt.setString(1, uuid);
            rset = pstmt.executeQuery();
             
            if (rset.next()) {
                messageTemplate = b.toBean(rset, MessageTemplate.class);
            }

        } catch (SQLException e) {
            logger.error("SQL Exception when getting messageTemplate with uuid: " + uuid);
            logger.error(ExceptionUtils.getStackTrace(e));
        } finally {
            if (rset != null) {
                try {
                    rset.close();
                } catch (SQLException e) {
                }
            }
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException e) {
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                }
            }
        }
        return messageTemplate;
    }
    
    /**
     *
     */
    @Override
    public MessageTemplate titleexists(String title) {
        MessageTemplate messageTemplate = null;

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        BeanProcessor b = new BeanProcessor();

        try {
            conn = dbCredentials.getConnection();
            pstmt = conn.prepareStatement("SELECT * FROM MessageTemplate WHERE title = ?;");
            pstmt.setString(1, title);
            rset = pstmt.executeQuery();

            if (rset.next()) {
                messageTemplate = b.toBean(rset, MessageTemplate.class);
            }

        } catch (SQLException e) {
            logger.error("SQL Exception when getting messageTemplate with uuid: " + title);
            logger.error(ExceptionUtils.getStackTrace(e));
        } finally {
            if (rset != null) {
                try {
                    rset.close();
                } catch (SQLException e) {
                }
            }
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException e) {
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                }
            }
        }
        return messageTemplate;
    }
    
    /**
     *
     */
    @Override
    public List<MessageTemplate> getAllMessageTemplatesbyuuid(String accuuid) {
        List<MessageTemplate> list = null;

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        BeanProcessor b = new BeanProcessor();

        try {
            conn = dbCredentials.getConnection();
            pstmt = conn.prepareStatement("SELECT * FROM MessageTemplate WHERE accountuuid=? ORDER BY title ASC;");
            pstmt.setString(1, accuuid);
            rset = pstmt.executeQuery();

            list = b.toBeanList(rset, MessageTemplate.class);

        } catch (SQLException e) {
            logger.error("SQL Exception when getting all messageTemplates" + accuuid);
            logger.error(ExceptionUtils.getStackTrace(e));
        } finally {
            if (rset != null) {
                try {
                    rset.close();
                } catch (SQLException e) {
                }
            }
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException e) {
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                }
            }
        }
        return list;
    }


    /**
     *
     */
    @Override
    public List<MessageTemplate> getAllMessageTemplates() {
        List<MessageTemplate> list = null;

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        BeanProcessor b = new BeanProcessor();

        try {
            conn = dbCredentials.getConnection();
            pstmt = conn.prepareStatement("SELECT * FROM MessageTemplate;");
            rset = pstmt.executeQuery();

            list = b.toBeanList(rset, MessageTemplate.class);

        } catch (SQLException e) {
            logger.error("SQL Exception when getting all messageTemplates");
            logger.error(ExceptionUtils.getStackTrace(e));
        } finally {
            if (rset != null) {
                try {
                    rset.close();
                } catch (SQLException e) {
                }
            }
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException e) {
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                }
            }
        }
        return list;
    }

    /**
     * @param uuid
     * @param messageTemplate
     * @return success
     */
    @Override
    public boolean updateMessageTemplate(MessageTemplate template) {
        boolean success = true;

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = dbCredentials.getConnection();
            pstmt = conn.prepareStatement("UPDATE MessageTemplate SET title=?,contents=? WHERE uuid = ?;");
            pstmt.setString(1, template.getTitle());
            pstmt.setString(2, template.getContents());
            pstmt.setString(3, template.getUuid());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            logger.error("SQL Exception when deleting"+ template);
            logger.error(ExceptionUtils.getStackTrace(e));
            success = false;
        } finally {
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException e) {
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                }
            }
        }
        return success;
    }

    /**
     *
     */
    @Override
    public boolean deleteMessageTemplate(String uuid) {
        boolean success = true;

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = dbCredentials.getConnection();
            pstmt = conn.prepareStatement("DELETE FROM MessageTemplate WHERE Uuid = ?;");
            pstmt.setString(1, uuid);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            logger.error("SQL Exception when deleting messageTemplate with uuid " + uuid);
            logger.error(ExceptionUtils.getStackTrace(e));
            success = false;
        } finally {
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException e) {
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                }
            }
        }
        return success;
    }

   
}
