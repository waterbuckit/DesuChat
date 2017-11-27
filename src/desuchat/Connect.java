/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package desuchat;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author waterbucket
 */
public class Connect implements Runnable {

    private final Socket socket;
    private final ObjectInputStream ois;

    public Connect(Socket socket, ObjectInputStream ois) {
        this.socket = socket;
        this.ois = ois;
    }

    @Override
    public void run() {
        this.chat();
    }

    private void chat() {
        String message = "";
        do {
            try {
                message = (String) this.ois.readObject();
                System.out.println("[" + this.socket.getInetAddress().getHostName()
                        + "] " + message);
            } catch (Exception e) {
            }
        } while (!message.equals("END"));
        try {
            socket.close();
            ois.close();
        } catch (IOException ex) {
            Logger.getLogger(Connect.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
