package org.example.addon.commands;

import be.alexandre01.dreamnetwork.api.DNCoreAPI;
import be.alexandre01.dreamnetwork.api.commands.Command;
import be.alexandre01.dreamnetwork.api.commands.HelpBuilder;
import be.alexandre01.dreamnetwork.api.commands.ICommand;
import be.alexandre01.dreamnetwork.api.commands.sub.NodeBuilder;
import be.alexandre01.dreamnetwork.api.commands.sub.SubCommandExecutor;
import be.alexandre01.dreamnetwork.api.commands.sub.types.BundlesNode;
import be.alexandre01.dreamnetwork.api.commands.sub.types.ScreensNode;
import be.alexandre01.dreamnetwork.api.service.IJVMExecutor;
import be.alexandre01.dreamnetwork.api.service.IService;
import be.alexandre01.dreamnetwork.core.console.jline.completors.CustomTreeCompleter;
import org.example.addon.StressTask;
import org.jline.reader.Completer;

public class StressCommand extends Command {
    StressTask stressTask = new StressTask();
    public StressCommand(String name) {
        super(name);

        getHelpBuilder().setTitleUsage("Stress command");
        getHelpBuilder().setCmdUsage("Stress command","stress");

        new NodeBuilder(NodeBuilder.create("stress", NodeBuilder.create(new ScreensNode()),NodeBuilder.create("-s")));
    }

    @Override
    public boolean onCommand(String[] args) {
        System.out.println(stressTask.isRunning());
        System.out.println("Stress command");

        if(stressTask.isRunning()){
            System.out.println("Canceling stress task");
            stressTask.cancel();
            return true;
        }

        if(args.length == 1){
            sendHelp();
            return true;
        }

        IService service = DNCoreAPI.getInstance().getContainer().tryToGetService(args[1]);

        if(service == null){
            System.out.println("Cannot find executor");
            return true;
        }

        if(service.getClient() == null){
            System.out.println("Cannot find client");
            return true;
        }

        boolean silent = false;
        if(args.length == 3){
            if(args[2].equalsIgnoreCase("-s")){
                silent = true;
            }
        }
        stressTask.pingPongStress(service.getClient(),silent);
        return true;
    }
}
