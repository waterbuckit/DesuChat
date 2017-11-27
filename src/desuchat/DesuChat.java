/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package desuchat;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author waterbucket
 */
public class DesuChat {

    private ObjectInputStream ois;
    private ObjectOutputStream ops;
    private ServerSocket serverSocket;
    private Socket socket;
    private int portNumber;

    public DesuChat(int portNumber) {
        this.portNumber = portNumber;
        try {
            this.serverSocket = new ServerSocket(this.portNumber, 5);
            this.serverPrint("Waiting...");
            this.socket = this.serverSocket.accept();
            this.serverPrint("Connected to " + this.socket.getInetAddress().getHostName());
            this.ops = new ObjectOutputStream(this.socket.getOutputStream());
            this.ois = new ObjectInputStream(this.socket.getInputStream());
            this.startThreads();
            this.send();
        } catch (IOException e) {
            System.out.println("There was a problem-desu!");
        }
    }

    public static void main(String[] args) {
//        DesuChat chat = new DesuChat(5565);
        DesuChat chat = new DesuChat(Integer.parseInt(args[0]));
    }

    private void serverPrint(String msg) {
        System.out.println("[Server]>> " + msg);
    }

    private void startThreads() {
        Thread receiver = new Thread(new Connect(this.socket, this.ois));
        receiver.start();
    }

    private void send() {
        Scanner s = new Scanner(System.in);
        while (this.socket.isConnected()) {
            try {
                this.ops.writeObject(s.nextLine());
                this.ops.flush();
            } catch (IOException ex) {
                Logger.getLogger(DesuChat.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        System.exit(0);
    }

}

