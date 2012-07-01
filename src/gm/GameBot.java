package gm;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class GameBot {
    public static void main(String[] args) throws Exception {
        Socket socket = new Socket("172.30.232.126", 5555);
        PrintWriter writer = new PrintWriter(socket.getOutputStream());
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        Scanner scanner = new Scanner(System.in);
        String local, remote;
        
        while((local = scanner.nextLine()) != null){
            writer.println(local);
            writer.flush();
            remote = reader.readLine();
            System.out.println(remote);
        }
        
        socket.close();
    }
}
