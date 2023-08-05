import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;
public class Client {

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket(InetAddress.getLocalHost(), 1111);
        BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter output = new PrintWriter(socket.getOutputStream(),true);

        ServerHandler s = new ServerHandler(socket);
        Thread t=new Thread(s);
        t.start();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String req = scanner.nextLine();
            if (req.equals("exit")) {
                break;
            }
            output.println(req);

        }
        input.close();
        output.close();
    }
}

