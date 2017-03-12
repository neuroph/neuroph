/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.training;

/**
 * Represents workflow parametar which has name and value
 * @author zoran
 */
public class Variable<V> {
    
    /**
     * Parametar name
     */
    String name;
    
    /**
     * Parametar value
     */
    V value;

    public Variable(String name, V value) {
        this.name = name;
        this.value = value;
    }
        

    public String getName() {
        return name;
    }

    public V getValue() {
        return value;
    }
    
    
    
    
    
}
