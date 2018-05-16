/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dn.core3.model.util;

import dn.core3.model.interfac.AuditoryEntity;
import java.util.Calendar;
import lombok.experimental.UtilityClass;

/**
 *
 * @author DARKUS
 */
@UtilityClass
public class Auditory {

    /**
     *
     * @param entity
     * @param user
     */
    public static void make(AuditoryEntity entity,Object user){
        if(entity.getId() == null){
            entity.setCreateUser(user);
            entity.setCreateDate(Calendar.getInstance().getTime());
            entity.setEditDate(null);
            entity.setEditUser(null);
        }else{
            entity.setEditUser(user);
            entity.setEditDate(Calendar.getInstance().getTime());
        }
    }
}
