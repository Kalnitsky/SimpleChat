package ru.ocrv.kalnitskiy.simplechat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Server {
    public static void main(String[] args) throws IOException {
        final ServerSocket server = new ServerSocket(2345);
        final ArrayList<Socket> clients = new ArrayList<>();

        // прослушивание сообщений от каждого клиента в
        // отдельном потоке и отправка их всем остальным
        while(true) {   
            clients.add((Socket)server.accept());
            new Thread() {
                private final int clientNumber = clients.size();

                @Override
                public void run() {
                    try {
                        BufferedReader clientIn = new BufferedReader(new InputStreamReader(
                                clients.get(clientNumber - 1).getInputStream()));
                        while(true) {
                            String message = clientIn.readLine();
                            String date = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss").format(new Date ());
                            int index = 0;
                            for (Socket client : clients) {
                                index++;
                                PrintWriter clientOut = new PrintWriter(client.getOutputStream());
                                if (index == clientNumber) {
                                    clientOut.println(date + " Я: " + message);
                                } else {
                                    clientOut.println(date + " Собеседник " + clientNumber + ": " + message);
                                }
                                clientOut.flush();
                            }
                        }
                    } catch(IOException e) {
                    }
                }
            }.start();
        }
    }
}