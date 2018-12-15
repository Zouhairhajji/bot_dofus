/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.bot.factories;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 *
 * @author zouhairhajji
 */
public class SocketFactory {

    public final static Socket create_auth_socket() throws IOException {
        return new Socket("34.251.172.139", 5555);
    }

    public final static Socket create_game_socket() throws IOException {
        return new Socket("52.17.154.41", 443);
    }

    public final static DataInputStream create_socket_reader(Socket socket) throws IOException {
        return new DataInputStream(new BufferedInputStream(socket.getInputStream()));
    }
    
    public final static DataOutputStream create_socket_sender(Socket socket) throws IOException {
        return new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
    }

}
