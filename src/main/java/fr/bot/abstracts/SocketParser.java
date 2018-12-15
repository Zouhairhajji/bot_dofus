/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.bot.abstracts;

import fr.bot.threads.Client;

/**
 *
 * @author zouhairhajji
 */
public interface SocketParser {

    public default void parse(String datagram, Client client) throws Exception {
        
    }

}
