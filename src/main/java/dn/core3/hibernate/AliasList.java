/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dn.core3.hibernate;

import java.util.HashMap;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.hibernate.sql.JoinType;

/**
 *
 * @author DannyPC
 */
public class AliasList extends HashMap<String,AliasList.AliasItem>  {

    public AliasList add(String property, String alias) {
        super.put(property, new AliasItem(alias, JoinType.INNER_JOIN)); //To change body of generated methods, choose Tools | Templates.
        return this;
    }
    public AliasList add(String property, String alias,JoinType joinType) {
        super.put(property, new AliasItem(alias, joinType)); //To change body of generated methods, choose Tools | Templates.
        return this;
    }
    
    @Data
    @RequiredArgsConstructor
    public  class AliasItem implements java.io.Serializable{
        @NonNull
        private String alias;
        @NonNull
        private JoinType joinType;
    }
    
    
}
