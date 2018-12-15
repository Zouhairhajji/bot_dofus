/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.bot.beans;

import java.util.HashMap;

/**
 *
 * @author zouhairhajji
 */
public class BigTable<K,V> extends HashMap<K, V>{
    
    public <T> T get(K key, Class<T> clazzType) {
        return (T) get(key);
    }
    
}
