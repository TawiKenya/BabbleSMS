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
package ke.co.tawi.babblesms.server.persistence;

import ke.co.tawi.babblesms.server.beans.StorableBean;
import ke.co.tawi.babblesms.server.beans.StorableBeanByUUID;
import ke.co.tawi.babblesms.server.beans.StorableBeanById;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;


/**
 * Implementation of a Storage facility.
 * <p>
 *  
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */
public class StorageImpl extends Storage {

	private SessionFactory sessionFactory;
	
	/**
	 * Disable default constructor
	 */
	private StorageImpl() {}
	
	
	/**
	 * @param sessionFactory
	 */
	public StorageImpl(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	
	/**
	 * @return
	 */
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	
	/**
	 * @see mobi.tawi.smsgw2.persistence.Storage#save(mobi.tawi.smsgw2.beans.StorableBean)
	 */
	@Override
	public StorableBean save(StorableBean bean) {
		Session session = sessionFactory.openSession();
        
        session.beginTransaction();            
        session.saveOrUpdate(bean);           
        session.getTransaction().commit();
        
        session.close();
        		
		return bean; 	// In the case where the primary key is an Id (integer), this bean
						// has the updated Id from the database.
	}
	

	/**
	 * @see mobi.tawi.smsgw2.persistence.Storage#get(java.lang.Class, java.lang.String)
	 */
	@Override
	public StorableBeanByUUID get(Class aClass, String uuid) {
		Session session = sessionFactory.openSession();
		
		session.getTransaction().begin();
		
		StorableBeanByUUID bean = (StorableBeanByUUID) session.get(aClass, uuid);
				
		session.close();
				
		return bean;
	}

	
	/**
	 * @see mobi.tawi.smsgw2.persistence.Storage#get(java.lang.Class, long)
	 */
	@Override
	public StorableBeanById get(Class aClass, long id) {
		Session session = sessionFactory.openSession();
		
		session.getTransaction().begin();
		
		StorableBeanById bean = (StorableBeanById) session.get(aClass, id);
				
		session.close();
		
		return bean;
	}
	

	/**
	 * @see mobi.tawi.smsgw2.persistence.Storage#getAll(java.lang.Class)
	 */
	@Override
	public List<StorableBeanByUUID> getAll(Class aClass) {
		List<StorableBeanByUUID> objectList = null;
		
		Session session = sessionFactory.openSession();
				
		objectList = session.createQuery("from " +  aClass.getSimpleName(), aClass).list();
				
		session.close();
		
		return objectList;
	}

	
	/**
	 * @see mobi.tawi.smsgw2.persistence.Storage#get(java.lang.Class, java.lang.Object, java.lang.Object)
	 */
	@Override
	public List<StorableBean> get(Class aClass, Object fieldName, Object fieldValue) {
		List<StorableBean> list = null;
		
		String hql = "from " + aClass.getSimpleName() + " where " + fieldName.toString() + " = :" + fieldName.toString();
		// Example of hql string: "from Account where username = :username"
		
		Session session = sessionFactory.openSession();
		
		Query query = session.createQuery(hql);
		query.setParameter(fieldName.toString(), fieldValue);
		list = query.list();						
				
		session.close();
		
		return list;
	}

}
