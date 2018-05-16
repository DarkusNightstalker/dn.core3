/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dn.core3.hibernate.generic;

import dn.core3.hibernate.AliasList;
import dn.core3.hibernate.CriterionList;
import dn.core3.hibernate.generic.interfac.IGenericDao;
import dn.core3.hibernate.generic.interfac.IGenericService;
import dn.core3.model.interfac.EntityActivate;
import java.io.Serializable;
import java.util.List;

import org.hibernate.criterion.Criterion;

/**
 *
 * @author Jhoan Brayam
 * @param <T>
 * @param <ID>
 */
@org.springframework.transaction.annotation.Transactional(readOnly = true)
public abstract class GenericService<T, ID extends Serializable> implements IGenericService<T, ID> {

    protected abstract IGenericDao<T, ID> getBasicDao();

    @Override
    public boolean isActive(ID id) {
        if(EntityActivate.class.isAssignableFrom(getBasicDao().getObjectClass())){
            return (boolean) getBasicDao().getByHQL("SELECT e.active FROM "+getClassName()+" e WHERE e.id =?",id);
        }else{
            throw new UnsupportedOperationException("This methos not supportes not EntityActivate Objects");
        }
    }
    
    @org.springframework.transaction.annotation.Transactional(readOnly = false, rollbackFor = Exception.class)
    @Override
    public Serializable save(T object) {
        return getBasicDao().save(object);
    }

    @org.springframework.transaction.annotation.Transactional(readOnly = false, rollbackFor = Exception.class)
    @Override
    public void update(T object) {
        getBasicDao().update(object);
    }

    @org.springframework.transaction.annotation.Transactional(readOnly = false, rollbackFor = Exception.class)
    @Override
    public void saveOrUpdate(T object) {
        getBasicDao().saveOrUpdate(object);
    }

    @org.springframework.transaction.annotation.Transactional(readOnly = false, rollbackFor = Exception.class)
    @Override
    public void delete(T object) {
        getBasicDao().delete(object);
    }
    
    @org.springframework.transaction.annotation.Transactional(readOnly = false, rollbackFor = Exception.class)
    @Override
    public int updateHQL(String hql, Object... parameters) throws Exception {
        return getBasicDao().updateHQL(hql, parameters);
    }

    @Override
    public List list() {
        return getBasicDao().list();
    }

    @Override
    public List listHQL(String hql) {
        return getBasicDao().listHQL(hql);
    }

    @Override
    public T getById(ID id) {
        return getBasicDao().getById(id);
    }

    @Override
    public List listOrderByColumns(String[] nameColumns, boolean asc) {
        return getBasicDao().listOrderByColumns(nameColumns, asc);
    }

    @Override
    public Number count() {
        return getBasicDao().count();
    }

    @Override
    public int updateHQL(String hql) throws Exception {
        return getBasicDao().updateHQL(hql);
    }


    @Override
    public Number countRestrictions(List<Criterion> listCriterion) {
        return getBasicDao().countRestrictions(listCriterion);
    }

    @Override
    public ID nextId(ID id, String idName, boolean withDisabled) {
        return getBasicDao().nextId(id, idName, withDisabled);
    }

    @Override
    public ID previousId(ID id, String idName, boolean withDisabled) {
        return getBasicDao().previousId(id, idName, withDisabled);
    }

    @Override
    public List getListByRestrictions(Serializable... variant) {
        return getBasicDao().getListByRestrictions(variant);
    }

    @Override
    public Number rowNumber(ID id, boolean withDisabled) {
        return getBasicDao().rowNumber(id, withDisabled);
    }

    @Override
    public List getPageableListByRestrictions(int rows, int page, Serializable... variant) {
        return getBasicDao().getPageableListByRestrictions(rows, page, variant);
    }

    @Override
    public Number countRestrictions(CriterionList criterionList, AliasList aliasList) {
        return getBasicDao().countRestrictions(criterionList, aliasList);
    }

    @Override
    public List listHQL(String hql, Object... parameters) {
        return getBasicDao().listHQL(hql, parameters);
    }

    @Override
    public List listHQLPage(String hql, int page, int recordsPerPage, Object... parameters) {
        return getBasicDao().listHQLPage(hql, page, recordsPerPage, parameters);
    }

    @Override
    public Object getByHQL(String hql) {
        return getBasicDao().getByHQL(hql);
    }

    @Override
    public Object getByHQL(String hql, Object... parameters) {
        return getBasicDao().getByHQL(hql, parameters);
    }

    @Override
    public String getClassName() {
        return getBasicDao().getObjectClass().getSimpleName();
    }


}
