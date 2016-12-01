package ru.spbau.mit;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import ru.spbau.mit.proto.Message;
import ru.spbau.mit.proto.MessengerGrpc;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The MessengerClient class provides realisation of client that can send and receive messages
 */
public class MessengerClient {
    private static Logger logger = Logger.getLogger(MessengerClient.class.getName());

    private MessagesReceiver messagesReceiver;
    private String host;
    private int port;
    private ManagedChannel channel;
    private MessengerGrpc.MessengerStub asyncStub;
    private StreamObserver<Message> requestObserver;

    public MessengerClient(String host, int port, MessengerGUIMain messengerGUIMain) {
        this.host = host;
        this.port = port;
        this.messagesReceiver = messengerGUIMain;
    }

    /**
     * This method starts client and receives messages
     */
    public synchronized void start() throws IOException {
        new Thread(() -> {
            ManagedChannelBuilder<?> channelBuilder =
                    ManagedChannelBuilder.forAddress(host, port).usePlaintext(true);
            channel = channelBuilder.build();
            asyncStub = MessengerGrpc.newStub(channel);

            requestObserver =
                    asyncStub.chat(new StreamObserver<Message>() {
                        @Override
                        public void onNext(Message message) {
                            messagesReceiver.receiveMessage(message.getName(), message.getContent());
                        }

                        @Override
                        public void onError(Throwable exception) {
                            logger.log(Level.WARNING, "Error in grpc chat: ", exception);
                        }

                        @Override
                        public void onCompleted() {
                        }
                    });
        }).start();
    }

    /**
     * This method sends message to server
     */
    public synchronized void sendMessage(String name, String message) throws IOException {
        requestObserver.onNext(Message.newBuilder().setName(name).setContent(message).build());
    }

    /**
     * This method stops client
     */
    public synchronized void stop() {
        try {
            channel.shutdown().awaitTermination(2, TimeUnit.SECONDS);
        } catch (InterruptedException ignored) {
        }
    }
}
