/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.training;

/**
 *
 * @author zoran
 */
public class BooleanExpression {
    
    String var;
    Object val;
    
   // eq, lt, gt, neq, ...
    
        
    public BooleanExpression(String var, Object val) {
        this.var = var;
        this.val = val;
    }
    
            
    public boolean isTrue() {
        //Object var = parent
        return true;
    }
    
    
}
