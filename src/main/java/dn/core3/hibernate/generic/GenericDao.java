/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dn.core3.hibernate.generic;

import dn.core3.hibernate.AliasList;
import dn.core3.hibernate.CriterionList;
import dn.core3.hibernate.OrderList;
import dn.core3.hibernate.criterion.AssociationCriterion;
import dn.core3.hibernate.generic.interfac.IGenericDao;
import dn.core3.model.interfac.EntityActivate;
import org.hibernate.*;
import org.hibernate.criterion.*;
import org.hibernate.persister.entity.AbstractEntityPersister;
import org.hibernate.transform.ResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Set;

/**
 * @param <T>
 * @param <ID>
 * @author Abraham
 */
public class GenericDao<T, ID extends Serializable> implements IGenericDao<T, ID> {

    @Autowired
    @Qualifier("sessionFactory")
    protected SessionFactory sessionFactory;
    private final Class<T> oClass;

    public GenericDao() {
        this.oClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    @Override
    public boolean isActive(ID id) {
        if (EntityActivate.class.isAssignableFrom(this.oClass)) {
            return (boolean) getByHQL("SELECT e.active FROM " + this.oClass.getSimpleName() + " e WHERE e.id =?", id);
        } else {
            throw new UnsupportedOperationException("This method not supportes for not EntityActivate Objects");
        }
    }

    @Override
    public Class<T> getObjectClass() throws HibernateException {
        return this.oClass;
    }

    @Override
    public int save(T objeto) throws HibernateException {
        return (Integer) getSessionFactory().getCurrentSession().save(objeto);
    }    

    @Override
    public void update(T objeto) throws HibernateException {
        getSessionFactory().getCurrentSession().update(objeto);
    }
    
    @Override
    public int updateHQL(String hql) throws Exception {
        return getSessionFactory().getCurrentSession()
                .createQuery(hql)
                .executeUpdate();
    }

    @Override
    public int updateHQL(String hql, Object... parameters) {
        Query query = getSessionFactory().getCurrentSession().createQuery(hql);
        for (int i = 0; i < parameters.length; i++) {
            query.setParameter(i, parameters[i]);
        }
        return query.executeUpdate();
    }

    @Override
    public void saveOrUpdate(T object) throws HibernateException {
        getSessionFactory().getCurrentSession().saveOrUpdate(object);
    }

    @Override
    public void delete(T objeto) throws HibernateException {
        if (objeto instanceof EntityActivate) {
            ((EntityActivate) objeto).setActive(Boolean.FALSE);
            getSessionFactory().getCurrentSession().update(objeto);
        } else {
            getSessionFactory().getCurrentSession().delete(objeto);
        }
    }

    @Override
    public List list() throws HibernateException {
        List lista = getSessionFactory().getCurrentSession()
                .createCriteria(oClass).list();
        return lista;
    }

    @Override
    public List listHQL(String hql) throws HibernateException {
        List lista = getSessionFactory().getCurrentSession()
                .createQuery(hql).list();
        return lista;
    }

    @Override
    public List listOrderByColumns(String[] nameColumns, boolean asc) {

        Criteria criteria = getSessionFactory()
                .getCurrentSession().createCriteria(oClass);
    
        if (asc) {
            for (int i = 0; i < nameColumns.length; i++) {
                criteria = criteria.addOrder(Order.asc(nameColumns[i]));
            }
        } else {
            for (int i = 0; i < nameColumns.length; i++) {
                criteria = criteria.addOrder(Order.desc(nameColumns[i]));
            }
        }
        return criteria.list();
    }

    @Override
    public T getById(ID id) throws HibernateException {
        return (T) getSessionFactory()
                .getCurrentSession().get(oClass, id);
    }

    @Override
    public Number count() {
        return (Number) getSessionFactory().getCurrentSession()
                .createCriteria(oClass).setProjection(Projections.rowCount())
                .uniqueResult();
    }


    public Number countRestrictions(List<Criterion> listCriterion) {

        Criteria criteria = getSessionFactory().getCurrentSession()
                .createCriteria(oClass);

        for (Criterion item : listCriterion) {
            if (item instanceof AssociationCriterion) {
                AssociationCriterion association = (AssociationCriterion) item;
                criteria.createAlias(association.getProperty(), association.getAlias()).add(association.getCriterion());
            } else {
                criteria.add(item);
            }
        }
        Number rowCount = (Number) criteria.setProjection(Projections.rowCount()).uniqueResult();
        return rowCount;
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List getListByRestrictions(Serializable... variant) {
        Criteria criteria = getSessionFactory().getCurrentSession()
                .createCriteria(oClass);
        for (Serializable c : variant) {
            if (c instanceof ProjectionList) {
                criteria.setProjection((ProjectionList) c);
            } else if (c instanceof AliasList) {
                AliasList a = (AliasList) c;
                Set<String> properties = a.keySet();
                for (String property : properties) {
                    AliasList.AliasItem item = a.get(property);
                    criteria.createAlias(property, item.getAlias(), item.getJoinType());
                }
            } else if (c instanceof CriterionList) {
                for (Criterion item : (CriterionList) c) {
                    criteria.add(item);
                }
            } else if (c instanceof OrderList) {
                for (Order item : (OrderList) c) {
                    criteria.addOrder(item);
                }
            } else if (c instanceof ResultTransformer) {
                criteria.setResultTransformer((ResultTransformer) c);
            } else {
                throw new IllegalArgumentException("Illegal argument");
            }
        }
        return criteria.list();
    }


    @Override
    public List getPageableListByRestrictions(int rows, int page, Serializable... variant) {
        Criteria criteria = getSessionFactory().getCurrentSession()
                .createCriteria(oClass);
        for (Serializable c : variant) {
            if (c instanceof ProjectionList) {
                criteria.setProjection((ProjectionList) c);
            } else if (c instanceof AliasList) {
                AliasList a = (AliasList) c;
                Set<String> properties = a.keySet();
                for (String property : properties) {
                    AliasList.AliasItem item = a.get(property);
                    criteria.createAlias(property, item.getAlias(), item.getJoinType());
                }
            } else if (c instanceof CriterionList) {
                for (Criterion item : (CriterionList) c) {
                    criteria.add(item);
                }
            } else if (c instanceof OrderList) {
                for (Order item : (OrderList) c) {
                    criteria.addOrder(item);
                }
            } else if (c instanceof ResultTransformer) {
                criteria.setResultTransformer((ResultTransformer) c);
            } else {
                throw new IllegalArgumentException("Illegal argument");
            }
        }
        criteria.setMaxResults(rows);
        criteria.setFirstResult((page - 1) * rows);
        return criteria.list();
    }

    @Override
    public ID nextId(ID id, String idName, boolean withDisabled) {
        Criteria criteria = getSessionFactory().getCurrentSession()
                .createCriteria(oClass);
        criteria.setProjection(Projections.id());
        criteria.add(Restrictions.gt(idName, id));
        if (!withDisabled) {
            criteria.add(Restrictions.eq("active", true));
        }
        criteria.addOrder(Order.asc(idName));
        criteria.setMaxResults(1);
        return (ID) criteria.uniqueResult();
    }

    @Override
    public ID previousId(ID id, String idName, boolean withDisabled) {
        Criteria criteria = getSessionFactory().getCurrentSession()
                .createCriteria(oClass);
        criteria.setProjection(Projections.id());
        criteria.add(Restrictions.lt(idName, id));
        if (!withDisabled) {
            criteria.add(Restrictions.eq("active", true));
        }
        criteria.addOrder(Order.desc(idName));
        criteria.setMaxResults(1);
        return (ID) criteria.uniqueResult();
    }

    @Override
    public Number rowNumber(ID id, boolean withDisabled) {
        String tableName = "";
        System.out.println(getSessionFactory().getClassMetadata(oClass).getClass());
        tableName = ((AbstractEntityPersister) getSessionFactory().getClassMetadata(oClass)).getTableName();
        SQLQuery sq = getSessionFactory().getCurrentSession().createSQLQuery("SELECT sq.rnum FROM (SELECT id,(row_number() OVER())  as rnum FROM " + tableName + " " + (!withDisabled ? "WHERE active=true" : "") + ") sq WHERE sq.id = :id");
        sq.setParameter("id", id);
        return (Number) sq.uniqueResult();
    }

    @Override
    public Number countRestrictions(CriterionList criterionList, AliasList aliasList) {
        Criteria criteria = getSessionFactory().getCurrentSession()
                .createCriteria(oClass);

        for (Criterion item : criterionList) {
            criteria.add(item);
        }
        if (aliasList != null) {
            Set<String> properties = aliasList.keySet();
            for (String property : properties) {
                AliasList.AliasItem item = aliasList.get(property);
                criteria.createAlias(property, item.getAlias(), item.getJoinType());
            }
        }
        Number rowCount = (Number) criteria.setProjection(Projections.rowCount()).uniqueResult();
        return rowCount;
    }

    @Override
    public List listHQL(String hql, Object... parameters) {
        Query query = getSessionFactory().getCurrentSession().createQuery(hql);
        for (int i = 0; i < parameters.length; i++) {
            query.setParameter(i, parameters[i]);
        }
        return query.list();
    }

    @Override
    public Object getByHQL(String hql, String[] name, Object... parameters) {
        Query query = getSessionFactory().getCurrentSession().createQuery(hql);
        for (int i = 0; i < parameters.length; i++) {
            if (parameters[i] instanceof List) {
                query.setParameterList(name[i], (List) parameters[i]);
            } else {
                query.setParameter(name[i], parameters[i]);
            }
        }
        return query.uniqueResult();
    }

    @Override
    public List listHQL(String hql, String[] name, Object... parameters) {
        Query query = getSessionFactory().getCurrentSession().createQuery(hql);
        for (int i = 0; i < parameters.length; i++) {
            if (parameters[i] instanceof List) {
                query.setParameterList(name[i], (List) parameters[i]);
            } else {
                query.setParameter(name[i], parameters[i]);
            }
        }
        return query.list();
    }

    @Override
    public Object getByHQL(String hql) {
        Query query = getSessionFactory().getCurrentSession().createQuery(hql);
        return query.uniqueResult();
    }

    @Override
    public Object getByHQL(String hql, Object... parameters) {

        Query query = getSessionFactory().getCurrentSession().createQuery(hql);
        for (int i = 0; i < parameters.length; i++) {
            query.setParameter(i, parameters[i]);
        }
        return query.uniqueResult();
    }

    @Override
    public List listHQLPage(String hql, int page, int recordsPerPage, Object... parameters) {
        Query query = getSessionFactory().getCurrentSession().createQuery(hql);
        for (int i = 0; i < parameters.length; i++) {
            query.setParameter(i, parameters[i]);
        }
        return query.setFirstResult((page - 1) * recordsPerPage)
                .setMaxResults(recordsPerPage).list();
    }


}
