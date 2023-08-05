import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server{
    private static ArrayList<ClientHandler> Clients=new ArrayList<>();
    private static ExecutorService playerNum= Executors.newFixedThreadPool(10);
    private static ArrayList<String> users=new ArrayList<>();
    private static ArrayList<String> roll=new ArrayList<>() ;
    private static ArrayList<String> rollArray=new ArrayList<>() ;
    private static ArrayList<String> citizen=new ArrayList<>() ;
    private static ArrayList<String> mafia=new ArrayList<>() ;
    private static ArrayList<Player> playerArrayList=new ArrayList<>();
    private static ArrayList<Vote> votes=new ArrayList<>();
    private static ArrayList<Integer> loop=new ArrayList<>();

    /**
     * this method made a arraylist with rolls
     */
    public void getRolls(){
        roll.add("godFather mafia");
        roll.add("drLecter mafia");
        roll.add("simple mafia");
        roll.add("drCity");
        roll.add("detective");
        roll.add("sniper");
        roll.add("citizen");
        roll.add("mayor");
        roll.add("psychologist");
        roll.add("hardDie");
    }

    public static void main(String[] args) throws IOException {
        int num=0;
        Server s=new Server();
        s.getRolls();
        ServerSocket server=new ServerSocket(1111);
        System.out.println("waiting for player");
        while (true){
            Socket socket=server.accept();
            System.out.println("player("+(num+1)+") added");
            ClientHandler Client=new ClientHandler(socket,Clients,users, roll , rollArray , citizen , mafia , playerArrayList , votes , loop);
            Clients.add(Client);
            playerNum.execute(Client);
            num++;
        }
    }
}

/**
 * this class made a connection between server and client
 */
class ServerHandler implements Runnable {
    private Socket socket;
    private BufferedReader in;

    public ServerHandler(Socket client) {
        this.socket = client;
        try {
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            String msg = "";
            while (true) {
                msg = in.readLine();
                System.out.println(msg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}