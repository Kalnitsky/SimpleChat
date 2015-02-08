package ru.ocrv.kalnitskiy.simplechat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws IOException {
        final Socket server = new Socket("127.0.0.1", 2345);

        // получение и вывод сообщений сервера в отдельном потоке
        new Thread() {
            @Override
            public void run() {
                try {
                    BufferedReader serverIn = new BufferedReader(new InputStreamReader(
                            server.getInputStream()));
                    while(true) {
                        System.out.println(serverIn.readLine());
                    }
                } catch(IOException e) {
                }
            }
        }.start();

        // ввод и отправка новых сообщений в основном потоке
        Scanner input = new Scanner(System.in);
        PrintWriter serverOut = new PrintWriter(server.getOutputStream());
        while(true) {
            serverOut.println(input.nextLine());
            serverOut.flush();
        }
    }
}