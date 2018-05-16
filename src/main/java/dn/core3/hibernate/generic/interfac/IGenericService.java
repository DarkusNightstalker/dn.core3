/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dn.core3.hibernate.generic.interfac;

import dn.core3.hibernate.AliasList;
import dn.core3.hibernate.CriterionList;
import java.io.Serializable;
import java.util.List;
import org.hibernate.criterion.Criterion;

/**
 *
 * @author Abraham
 * @param <T>
 * @param <ID>
 */
public interface IGenericService<T, ID extends Serializable> extends Serializable{

    public boolean isActive(ID id);
    /**
     * Guarda un nuevo registro en la base de datos. Sus relaciones deben estar
     * completas, puesto que puede ocurrir un error al subir a la base de datos.
     *
     * @param object nuevo objeto llena con sus relaciones
     * @return retorna el ID del objeto subido.
     */
    public Serializable save(T object);

    /**
     * Realiza un <code>update</code> en la base de datos se debe tener en
     * cuenta que el objeto ya debe existir en la base de datos.
     *
     * @param object el Id debe existir en la base de datos.
     */
    public void update(T object);

    public int updateHQL(String hql) throws Exception;
    public int updateHQL(String hql,Object... parameters) throws Exception;

    /**
     * Realiza un <code>insert</code>, <code>update</code>, <code>delete</code>,
     * en la base de datos, segun sea el caso de sus relaciones.
     *
     * @param object Objeto relacionado.
     */
    public void saveOrUpdate(T object);

    /**
     * Borra en cascada todas las realciones del objeto en la base de datos
     *
     * @param object Tiene que tener lleno sus datos para poder borrar todo en
     * cascada.
     */
    public void delete(T object);

    /**
     * Retorna la lista completa de todos los objetos sin ningun tipo de
     * restricciones.
     *
     * @return Lista total.
     */
    public List list();

    /**
     * Metodo que retorna una lista de objetos, con una consulta HQL, es muy
     * similar a la consulta SQL.
     *
     * @param hql Consulta Hibernate Query Lenguage.
     * @return Lista de Objetos de la clase.
     */
    public List listHQL(String hql);
    /**
     * Metodo que retorna una lista de objetos, con una consulta HQL, es muy
     * similar a la consulta SQL.
     *
     * @param hql Consulta Hibernate Query Lenguage.
     * @param parameters
     * @return Lista de Objetos de la clase.
     */
    public java.util.List listHQL(String hql,Object... parameters);
    /**
     * Metodo que retorna una lista de objetos, con una consulta HQL, es muy
     * similar a la consulta SQL.
     *
     * @param hql Consulta Hibernate Query Lenguage.
     * @param page
     * @param recordsPerPage
     * @param parameters
     * @return Lista de Objetos de la clase.
     */
    public java.util.List listHQLPage(String hql,int page,int recordsPerPage,Object... parameters);
    /**
     * Metodo que retorna una lista de objetos, con una consulta HQL, es muy
     * similar a la consulta SQL.
     *
     * @param hql Consulta Hibernate Query Lenguage.
     * @return Lista de Objetos de la clase.
     */
    public Object getByHQL(String hql);
    /**
     * Metodo que retorna una lista de objetos, con una consulta HQL, es muy
     * similar a la consulta SQL.
     *
     * @param hql Consulta Hibernate Query Lenguage.
     * @param parameters Parametros de la consulta
     * @return Objeto de la clase.
     */
    public Object getByHQL(String hql,Object... parameters);

    /**
     * Metodo que ordena las columnas en forma ascendente o descendente,
     * dependiendo del parametro booleano. Se ingresa un arreglo del nombre de
     * las columnas de las clases que se desea ordenar.
     *
     * <ul>Se puede ordenar por "id" <-- Atributo de la clse.</ul> <ul>Tambien
     * se p uede poner un campo <code>date</code> para ordenarlo</ul>
     *
     * @param nameColumns Arreglo con los nombres de las columnas
     * @param asc <code>true</code> si se ordena en forma acsendente
     * @return Retorna la lista ordenada.
     */
    public List listOrderByColumns(String[] nameColumns, boolean asc);

    /**
     * Metodo que devuelve un objeto completo ingresando como parametro su Id
     *
     * @param id Su identificador de la clase que esta relacionado con la base
     * de datos
     *
     * @return Retorna un Objeto del tipo de la misma clase.
     */
    public T getById(ID id);

    /**
     * Cuenta todas las filas y trae el resultado
     *
     * @return retorna el resultado del conteo.
     */
    public Number count();


    public List getListByRestrictions(Serializable... variant);

    public List getPageableListByRestrictions(int rows, int page, Serializable... variant);


    public ID nextId(ID id, String idName, boolean withDisabled);

    public ID previousId(ID id, String idName, boolean withDisabled);

    public Number rowNumber(ID id, boolean withDisabled);

    public Number countRestrictions(List<Criterion> listCriterion);
    public Number countRestrictions(CriterionList criterionList,AliasList aliasList);
    
    public String getClassName();
}
