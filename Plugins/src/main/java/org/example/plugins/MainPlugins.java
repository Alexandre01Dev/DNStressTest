package org.example.plugins;

import be.alexandre01.dnplugin.api.request.communication.ClientResponse;
import be.alexandre01.dnplugin.plugins.spigot.api.DNSpigotAPI;
import be.alexandre01.dnplugin.plugins.spigot.api.events.server.ServerAttachedEvent;
import be.alexandre01.dnplugin.utils.messages.Message;
import io.netty.channel.ChannelHandlerContext;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class MainPlugins extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        // Plugin startup logic
        System.out.println("Hello World!");

        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onDreamLoad(ServerAttachedEvent event){

        System.out.println("Server attached");

        DNSpigotAPI dnSpigotAPI = DNSpigotAPI.getInstance();

        dnSpigotAPI.getClientHandler().getResponses().add(new ClientResponse() {
            long time = System.currentTimeMillis();
            @Override
            public void onAutoResponse(Message message, ChannelHandlerContext ctx) {
                if(message.contains("pingPong")){
                    dnSpigotAPI.getClientHandler().writeAndFlush(message);
                }
                if(message.contains("test")){
                    if(message.getInt("test") == 0){
                        time = System.currentTimeMillis();
                        System.out.println("Start testt !!!!");
                    }
                    if(message.getInt("test") == 99999){
                        System.out.println("End of testt !!!!");
                        //get in ms
                        System.out.println("Time: "+(System.currentTimeMillis()-time+"ms"));
                    }
                }
            }
        });
    }
}