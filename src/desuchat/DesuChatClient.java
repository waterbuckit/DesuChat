/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package desuchat;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author waterbucket
 */
public class DesuChatClient {

    private ObjectInputStream ois;
    private ObjectOutputStream ops;
    private Socket socket;
    private String host;
    private int portNumber;
    
    
    /**
     * creates a new client that can connect to a server, sets up the streams
     * and starts the receiver thread, then allows the user to send.
     * @param host
     * @param portNumber 
     */
    public DesuChatClient(String host, int portNumber) {
        this.host = host;
        this.portNumber = portNumber;
        try {
            System.out.println("Connecting...");
            this.socket = new Socket(InetAddress.getByName(this.host), this.portNumber);
            System.out.println("Connected!");
            this.ois = new ObjectInputStream(this.socket.getInputStream());
            this.ops = new ObjectOutputStream(this.socket.getOutputStream());
        } catch (IOException e) {
        }
        this.startThreads();
        this.send();
    }

    public static void main(String[] args) {
//        DesuChatClient client = new DesuChatClient("localhost",5565);
        DesuChatClient client = new DesuChatClient(args[0], Integer.parseInt(args[1]));
    }
    /**
     * Starts a thread for receiving messages and printing to the terminal.
     */
    private void startThreads() {
        Thread receiver = new Thread(new Connect(this.socket, this.ois));
        receiver.start();
    }
    
    /**
     * Loops forever, allowing the user to write to the output stream to their
     * heart's content
     */
    private void send() {
        Scanner s = new Scanner(System.in);
        while (true) {
            try {
                this.ops.writeObject(s.nextLine());
                this.ops.flush();
            } catch (IOException ex) {
                Logger.getLogger(DesuChat.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
