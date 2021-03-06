package shu.nsd;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Vector;

public class Server {

    private final ServerSocket serverSocket;
    public static Vector<Responder> clients;
    public static JSONArray savedposts = new JSONArray();

    public Server(ServerSocket serverSocket){
        clients = new Vector<>();
        this.serverSocket = serverSocket;
    }
    public void listen() {
        while (true){
            try {
                System.out.println("Listen...");
                Socket socket = serverSocket.accept();
                clients.add(new Responder(socket));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void load(){
        new Thread(() -> {
            File path = new File("savedposts.json");
            if(path.exists()){
                JSONParser jsonParser = new JSONParser();
                try (FileReader reader = new FileReader(path)) {
                    Object obj = jsonParser.parse(reader);
                    savedposts = (JSONArray) obj;
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    public static void save(){
        new Thread(() -> {
            File path = new File("savedposts.json");
            try (FileWriter file = new FileWriter(path)) {
                file.write(savedposts.toJSONString());
                file.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
