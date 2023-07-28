package org.example.addon;

import be.alexandre01.dreamnetwork.api.connection.core.communication.CoreResponse;
import be.alexandre01.dreamnetwork.api.connection.core.communication.IClient;
import be.alexandre01.dreamnetwork.api.service.IJVMExecutor;
import be.alexandre01.dreamnetwork.core.utils.messages.Message;
import io.netty.channel.ChannelHandlerContext;
import lombok.Getter;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class StressTask {
    @Getter CoreResponse coreResponse;
    @Getter IClient iClient;

    public void pingPongStress(IClient iClient,boolean silent) {

        iClient.writeAndFlush(new Message().set("pingPong", 0));
        System.out.println("Start pingPong");
        System.out.println("PingPong: 0");
        System.out.println("Adding response");

        iClient.getCoreHandler().getResponses().add(coreResponse = new CoreResponse() {
            @Override
            public void onAutoResponse(Message message, ChannelHandlerContext ctx, IClient client) {
                if (message.get("pingPong") != null) {
                    int pingPong = message.getInt("pingPong");
                    pingPong++;
                    client.writeAndFlush(new Message().set("pingPong", pingPong));
                    if (pingPong % 5000 == 0){
                        if(!silent){
                            System.out.println("PingPong: " + pingPong);
                        }
                    }
                }
            }
        });
        Executor executor = Executors.newSingleThreadExecutor();

        executor.execute(() -> {
            long time = System.currentTimeMillis();
            for (int i = 0; i < 100000; i++) {
                iClient.writeAndFlush(new Message().set("test", i));
            }
            System.out.println("Time: " + (System.currentTimeMillis() - time + "ms"));
        });
    }

    public void cancel(){
        if(isRunning()){
            iClient.getCoreHandler().getResponses().remove(coreResponse);
            coreResponse = null;
            iClient = null;
        }
    }

    public boolean isRunning(){
        return coreResponse != null && iClient != null;
    }

}
