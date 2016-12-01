package ru.spbau.mit;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import ru.spbau.mit.proto.Message;
import ru.spbau.mit.proto.MessengerGrpc;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The MessengerServer class provides realisation of server that can send and receive messages
 */
public class MessengerServer {
    private static Logger logger = Logger.getLogger(MessengerServer.class.getName());

    private MessagesReceiver messagesReceiver;
    private int port;
    private Server server;
    private StreamObserver<Message> observer;

    public MessengerServer(int port, MessengerGUIMain messengerGUIMain) {
        this.port = port;
        this.messagesReceiver = messengerGUIMain;
    }

    /**
     * This method starts server and receives messages
     */
    public synchronized void start() throws IOException {
        new Thread(() -> {
            try {
                server = ServerBuilder.forPort(port)
                        .addService(new MessengerService())
                        .build()
                        .start();
                server.awaitTermination();
            } catch (IOException | InterruptedException exception) {
                logger.log(Level.WARNING, "Error in receiving messages: ", exception);
            }
        }).start();
    }

    /**
     * This method sends message to client
     */
    public synchronized void sendMessage(String name, String message) throws IOException {
        if (observer == null) {
            throw new IOException("No client connected");
        }
        observer.onNext(Message.newBuilder().setName(name).setContent(message).build());
    }

    /**
     * This method stops server
     */
    public synchronized void stop() {
        if (server != null) {
            server.shutdown();
        }
    }

    /**
     * The MessengerService class is service class for using gprc
     */
    private class MessengerService extends MessengerGrpc.MessengerImplBase {
        @Override
        public StreamObserver<Message> chat(final StreamObserver<Message> responseObserver) {
            return new StreamObserver<Message>() {
                @Override
                public void onNext(Message message) {
                    messagesReceiver.receiveMessage(message.getName(), message.getContent());
                    observer = responseObserver;
                }

                @Override
                public void onError(Throwable exception) {
                    logger.log(Level.WARNING, "Error in grpc service: ", exception);
                }

                @Override
                public void onCompleted() {
                    responseObserver.onCompleted();
                }
            };
        }
    }
}
