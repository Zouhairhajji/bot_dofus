/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.bot.threads;
 
import fr.bot.abstracts.SocketParser;
import fr.bot.beans.BigTable;
import fr.bot.enums.ClientAttribute;
import static fr.bot.enums.ClientAttribute.PASSWORD;
import static fr.bot.enums.ClientAttribute.PLAYED_ID;
import static fr.bot.enums.ClientAttribute.SERVER_ID;
import static fr.bot.enums.ClientAttribute.USERNAME;
import fr.bot.enums.StateEnum;
import static fr.bot.enums.StateEnum.NOT_STARTED;
import fr.bot.factories.SocketFactory;
import fr.bot.states.AuthentificationState;
import fr.bot.states.NotStartedState;
import fr.bot.states.StandingState;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import javax.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 *
 * @author zouhairhajji
 */
@Component
@Getter
@Setter
public class Client extends Thread {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    
    private boolean running;
    private boolean pause;
    
    // big table '' google 
    private BigTable<ClientAttribute, Object> bigTable;

    // queue messages
    private LinkedList<String> messagesIn;

    // socket
    private Socket socket;
    private DataInputStream reader;
    private DataOutputStream sender;

    // state pattern
    private Map<StateEnum, SocketParser> parsers;
    private StateEnum currentState;

    @PostConstruct
    public void postConstruct() {
        this.bigTable = new BigTable<>();
        this.messagesIn = new LinkedList<>();
        this.parsers = new HashMap() {
            {
                put(StateEnum.NOT_STARTED, new NotStartedState());
                put(StateEnum.AUTHENTIFICATION, new AuthentificationState());
                put(StateEnum.STANDING, new StandingState());
            }
        };
        this.currentState = NOT_STARTED;
    }

    public void start_attributes(String username, String password, String serverId, String playerId) {
        this.bigTable.put(USERNAME, username);
        this.bigTable.put(PASSWORD, password);
        this.bigTable.put(SERVER_ID, serverId);
        this.bigTable.put(PLAYED_ID, playerId);

    }

    @Override
    public void run() {
        logger.info("thread started");
        try {
            this.socket = SocketFactory.create_auth_socket();
            this.sender = SocketFactory.create_socket_sender(this.socket);
            this.reader = SocketFactory.create_socket_reader(this.socket);
            this.running = Boolean.TRUE;
        } catch (Exception e) {
            this.running = Boolean.FALSE;
        }

        while (running) {
            pause(100);
            if (pause) {
                continue;
            }
            try {
                String message = this.read_message();
                this.parsers.get(this.currentState).parse(message, this);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
    }

    /**
     *
     * Helpers
     *
     *
     */
    public void pause(long duration) {
        try {
            Thread.sleep(duration);
        } catch (Exception e) {

        }
    }

    public String read_message() throws IOException {
        if (!messagesIn.isEmpty()) {
            String message = this.messagesIn.removeFirst();
            logger.info("[{}] [{}] {}", this.currentState, "<", message);
            return message;
        }
        byte[] byteMessage = new byte[20000];
        reader.read(byteMessage);
        
        int size = 0;
        while (size < byteMessage.length) {
            if (byteMessage[size] == 0 && byteMessage[size + 1] == 0) {
                break;
            }
            size++;
        }

        String message = new String(byteMessage, 0, size, "UTF-8");

        if (message == null) {
            // nothing to do
        } else if (message.split((char) 0 + "").length == 1) {
            // nothing to do
        } else {
            this.messagesIn.addAll(Arrays.asList(message.split((char) 0 + "")));
            message = this.messagesIn.removeLast();
        }
        logger.info("[{}] [{}] {}", this.currentState, "<", message);
        return message;
    }

    public void send_message(String message) throws IOException {
        byte[] bytes = (message + "\n\r").getBytes("UTF-8");

        logger.info("[{}] [{}] {}", this.currentState, ">", message);

        this.sender.write(bytes);
        this.sender.flush();
    }

}
