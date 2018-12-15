/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.bot.states;

import fr.bot.abstracts.SocketParser;
import fr.bot.enums.ClientAttribute;
import fr.bot.enums.StateEnum;
import fr.bot.factories.SocketFactory;
import fr.bot.threads.Client;
import static fr.bot.utils.AuthUtils.crypt;
import java.net.Socket;

/**
 *
 * @author zouhairhajji
 */
public class AuthentificationState implements SocketParser {

    @Override
    public void parse(String datagram, Client client) throws Exception {
        if (datagram.startsWith("HC")) {
            String username = client.getBigTable().get(ClientAttribute.USERNAME, String.class);
            String password = client.getBigTable().get(ClientAttribute.PASSWORD, String.class);
            
            String auth_key = datagram.substring(2, datagram.length());
            
            client.send_message("1.29.1");
            client.send_message(username + ((char) 10) + crypt(password, auth_key));
            client.send_message("Af");

        } else if (datagram.startsWith("AQ")) {
            client.send_message("Ax");

        } else if (datagram.startsWith("AxK")) {
            String playedId = client.getBigTable().get(ClientAttribute.SERVER_ID, String.class);
            client.send_message("AX" + playedId);
        } else if (datagram.startsWith("AXK")) {
            String guid = datagram.substring(datagram.length() - 7, datagram.length());
            // create connection to server auth
            Socket socketAuth = SocketFactory.create_game_socket();

            client.setSocket(socketAuth);
            client.setSender(SocketFactory.create_socket_sender(socketAuth));
            client.setReader(SocketFactory.create_socket_reader(socketAuth));
            client.getBigTable().put(ClientAttribute.GUID, guid);

        } else if (datagram.startsWith("HG")) {
            String guid = client.getBigTable().get(ClientAttribute.GUID, String.class);
            client.send_message("AT" + guid);

        } else if (datagram.startsWith("ATK0")) {
            client.send_message("Ak0");
            client.send_message("AV");

        } else if (datagram.startsWith("AV0")) {
            client.send_message("Agrf");
            client.send_message("AL");
            client.send_message("Af");

        } else if (datagram.startsWith("ALK")) {
            String playedId = client.getBigTable().get(ClientAttribute.PLAYED_ID, String.class);
            client.send_message("AS" + playedId);
            client.send_message("Af");

        } else if (datagram.startsWith("TT32")) {
            client.setCurrentState(StateEnum.STANDING);
            client.getMessagesIn().addFirst(datagram);
            client.send_message("GC1");
        }
    }

}
