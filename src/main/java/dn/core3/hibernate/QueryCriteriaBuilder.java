/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dn.core3.hibernate;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.sql.JoinType;
import org.hibernate.transform.ResultTransformer;

/**
 *
 * @author Darkus Nightmare
 */
public class QueryCriteriaBuilder implements java.io.Serializable {

    private ProjectionList projectionList;
    private List<Criterion> criterions;
    private AliasList aliasList;
    private List<Order> orders;

    private ResultTransformer resultTransformer;

    public QueryCriteriaBuilder() {
        this.projectionList = Projections.projectionList();
    }

    public QueryCriteriaBuilder addProperty(Projection projection) {
        projectionList.add(projection);
        return this;
    }

    public QueryCriteriaBuilder addAlias(String property, String alias) {
        aliasList.add(property, alias);
        return this;
    }

    public QueryCriteriaBuilder addAlias(String property, String alias, JoinType joinType) {
        aliasList.add(property, alias, joinType);
        return this;
    }

    public QueryCriteriaBuilder addFilter(Criterion criterion) {
        criterions.add(criterion);
        return this;
    }

    public QueryCriteriaBuilder addOrder(Order order) {
        orders.add(order);
        return this;
    }

    public QueryCriteriaBuilder withTransformer(ResultTransformer resultTransformer) {
        this.resultTransformer = resultTransformer;
        return this;
    }

    List executeList(Criteria criteria) {
        for (Criterion criterion : criterions) {
            criteria.add(criterion);
        }
        for (Order item : orders) {
            criteria.addOrder(item);
        }
        if (resultTransformer != null) {
            criteria.setResultTransformer(resultTransformer);
        }
//        for (Serializable c : variant) {
//            if (c instanceof ProjectionList) {
//                criteria.setProjection((ProjectionList) c);
//            } else if (c instanceof AliasList) {
//                AliasList a = (AliasList) c;
//                Set<String> properties = a.keySet();
//                for (String property : properties) {
//                    AliasList.AliasItem item = a.get(property);
//                    criteria.createAlias(property, item.getAlias(), item.getJoinType());
//                }
//            } else if (c instanceof CriterionList) {
//                for (Criterion item : (CriterionList) c) {
//                }
//            } else if (c instanceof OrderList) {
//            } else if (c instanceof ResultTransformer) {
//            } else {
//                throw new IllegalArgumentException("Illegal argument");
//            }
//        }

        return null;
    }

}
