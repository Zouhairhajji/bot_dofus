/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.bot.states;

import fr.bot.abstracts.SocketParser;
import fr.bot.enums.StateEnum;
import fr.bot.threads.Client;

/**
 *
 * @author zouhairhajji
 */
public class NotStartedState implements SocketParser {
    
    @Override
    public void parse(String datagram, Client client) throws Exception {

        client.setCurrentState(StateEnum.AUTHENTIFICATION);
        client.getMessagesQueue().addFirst(datagram);
        
        
    }

}
