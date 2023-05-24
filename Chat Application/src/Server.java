
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.nio.Buffer;

public class Server {
    ServerSocket server;
    Socket socket;
    BufferedReader br;
    PrintWriter out;
    // constructor

    public Server() {
        try {
            server = new ServerSocket(7777);
            System.out.println("Server is ready to accept connection");
            System.out.println("Waiting..");
            socket = server.accept();
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            out = new PrintWriter(socket.getOutputStream());

            startReading();
            startWriting();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // we have to use multithreading concept because startWriting and startReading
    // should be work at same time time
    // means they must be synchronized

    public void startReading() {
        // thread will continuously reading the data
        Runnable r1 = () -> {
            // lambda function implementing Runnable interface
            System.out.println("Reader started...");
            try {
                while (true) {
                    String msg=br.readLine();
                    if(msg.equals("exit")){
                        System.out.println("Client terminated the chat");
                        socket.close();
                        break;
                    }
                    System.out.println("Client : "+msg);

                }
            } catch (Exception e) {
                // e.printStackTrace();
                System.out.println("Connection closed !");

            }
        };
        new Thread(r1).start();

    }

    public void startWriting() {
        // and this thread will take data from users and sends the data to client
        // machine or client program
        Runnable r2 = () -> {
            System.out.println("Writer started...");
            try {
                while(!socket.isClosed()){

                    BufferedReader br1=new BufferedReader(new InputStreamReader(System.in));
                    String content=br1.readLine();
                    out.println(content);
                    out.flush();

                    if(content.equals("exit")){
                        socket.close();
                        break;
                    }

                }
            }catch (Exception e) {
                // e.printStackTrace();
                System.out.println("Connection closed !");

            }


        };
        new Thread(r2).start();
    }



    public static void main(String[] args) {
        System.out.println("This is Server");
        new Server();

    }
}
