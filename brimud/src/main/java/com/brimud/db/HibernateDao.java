/**
 * 
 */
package com.brimud.db;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.google.inject.Inject;

/**
 * @author dan
 *
 */
public class HibernateDao<T, ID extends Serializable> {

  private final SessionFactory sessionFactory;
  
  private final Class<T> domainClass;
  
//  private final Class<ID> idClass;
  
  @Inject
  HibernateDao(Class<T> domainClass, Class<ID> idClass, SessionFactory sessionFactory) {
    this.domainClass = domainClass;
//    this.idClass = idClass;
    this.sessionFactory = sessionFactory;
  }
  
  public T getById(ID id) {
    return domainClass.cast(getSession().get(domainClass, id));
  }
  
  @SuppressWarnings("unchecked")
  public List<T> getAll() {
    return (List<T>)getSession().createCriteria(domainClass).list();
  }
  
  public void saveOrUpdate(T entity) {
    getSession().saveOrUpdate(entity);
  }
  
  public void delete(T entity) {
    getSession().delete(entity);
  }
  
  public Criteria createCriteria() {
    return getSession().createCriteria(domainClass);
  }
  
  private Session getSession() {
    return this.sessionFactory.getCurrentSession();
  }
}
