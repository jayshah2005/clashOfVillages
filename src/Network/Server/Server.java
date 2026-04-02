package src.Network.Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.IllegalBlockingModeException;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Server {

    int port = 2222;

    Server() {}

    public void start(){
        try (ServerSocket serverSocket = new ServerSocket(port)) {

            System.out.println("Server started on port " + port);


            while(true){
                Socket clientSocket = serverSocket.accept();
                ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newCachedThreadPool();

                threadPoolExecutor.execute(() -> {

                    try(
                            ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
                            ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
                    ){
                        ClientHandler clientHandler = new ClientHandler(in, out);
                        clientHandler.handleClient();
                    } catch (IOException e) {
                        // TODO: Handle exception accordingly
                    }
                });
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static void main(String[] args) {
        new Server().start();
    }
}
