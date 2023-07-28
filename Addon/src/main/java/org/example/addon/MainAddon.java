package org.example.addon;

import be.alexandre01.dreamnetwork.api.addons.Addon;
import be.alexandre01.dreamnetwork.api.addons.DreamExtension;
import org.example.addon.commands.StressCommand;

public class MainAddon extends DreamExtension {
    public MainAddon(Addon addon){
        super(addon);
    }

    @Override
    public void onLoad(){
        // If you want to compile it with Plugins in ressource use "mvn clean install -P PlugInAddon" instead of "mvn clean install"
        // Remove the comment if you want to register a plugin to the servers
        System.out.println("Registering plugin to servers");



        System.out.println( MainAddon.class.getClassLoader().getResourceAsStream(("Plugins-1.0-SNAPSHOT.jar")));

        registerPluginToServers( MainAddon.class.getClassLoader().getResourceAsStream("Plugins-1.0-SNAPSHOT.jar"),"StressDream.jar");
    }

    @Override
    public void start(){
        getDnCoreAPI().getCommandReader().getCommands().addCommands(new StressCommand("stress"));
    }

}