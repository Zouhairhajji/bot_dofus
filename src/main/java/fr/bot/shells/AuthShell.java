package fr.bot.shells;
        
        
import fr.bot.beans.BigTable;
import fr.bot.enums.ClientAttribute;
import fr.bot.threads.Client;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author zouhairhajji
 */
@ShellComponent
public class AuthShell {

    @Autowired
    private Client client;
    
    @ShellMethod(value = "Add two integers together.")
    public void send(String message) throws IOException {
        this.client.send_message(message);
    }
    
    @ShellMethod(value = "print all values")
    public BigTable<ClientAttribute, Object> print() {
        return this.client.getBigTable();
    }
}
