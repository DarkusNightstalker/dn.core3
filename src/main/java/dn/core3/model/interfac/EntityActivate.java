/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dn.core3.model.interfac;

import java.io.Serializable;

/**
 *
 * @author Jhoan Brayam
 */
public interface EntityActivate extends Serializable{
     public void setActive(Boolean active);
     public Boolean getActive();
}
