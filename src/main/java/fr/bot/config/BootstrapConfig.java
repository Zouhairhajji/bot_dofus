/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.bot.config;
 
import fr.bot.threads.Client;
import java.lang.invoke.MethodHandles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.task.TaskExecutor;

/**
 *
 * @author zouhairhajji
 */
@Configuration
public class BootstrapConfig implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private TaskExecutor taskExecutor;
    
    @Autowired
    private Client client;

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    
    @Override
    public void onApplicationEvent(ContextRefreshedEvent e) {
        this.client.start_attributes("username", "password", "serverId", "playedId");
        this.client.start(); 
    }

}
