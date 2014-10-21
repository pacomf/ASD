package com.cryptull.sockets;

import android.app.Activity;
import android.widget.TextView;

import com.cryptull.asd.Utilities;
import com.cryptull.pak.PAK;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Paco on 21/10/2014.
 */
public class SocketServerThread extends Thread {

    int SocketServerPORT;
    int count = 0;
    Activity activity;
    String message="";
    PAK pak;
    TextView consola;
    int step;

    public SocketServerThread (Activity activity, Integer port, TextView consola) {
        this.activity = activity;
        this.SocketServerPORT = port;
        this.pak = new PAK("password");
        this.consola = consola;
        this.step=1;
    }

    @Override
    public void run() {
        Socket socket = null;
        DataInputStream dataInputStream = null;
        DataOutputStream dataOutputStream = null;

        try {
            Utils.serverSocket = new ServerSocket(SocketServerPORT);
            this.activity.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    consola.append("\nI'm waiting here: " + Utils.serverSocket.getLocalPort());
                }
            });

            while (true) {
                socket = Utils.serverSocket.accept();
                dataInputStream = new DataInputStream(socket.getInputStream());
                dataOutputStream = new DataOutputStream(socket.getOutputStream());

                String msgReply = "";

                //If no message sent from client, this code will block the program
                message = dataInputStream.readUTF();

                if (step < 4) {

                    message = new String(Utilities.decipher(Utilities.hexToBytes(message), Utils.password.getBytes()), "UTF-8");

                    BigInteger[] bi = Utils.unrollPackage(message);
                    step = bi[0].intValue();
                    step++;
                    BigInteger[] bir = this.pak.processPackage(step, bi[1], bi[2], "idA", "idB", Utils.password, consola, activity);
                    if (bir != null) {
                        msgReply = Utils.createPackage(step, bir[0], bir[1]);
                    } else {
                        msgReply = "ready";
                    }

                    msgReply = Utilities.bytesToHex(Utilities.cipher(msgReply.getBytes(), Utils.password.getBytes()));
                    dataOutputStream.writeUTF(msgReply);
                } else {
                    step = 1;
                    message = new String(Utilities.decipher(Utilities.hexToBytes(message), pak.K.toString().getBytes()), "UTF-8");
                    final long duration = System.currentTimeMillis() - Utils.startTime;

                    this.activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            consola.append("\nMensaje: "+message+"\nTiempo Requerido: " + duration + " ms.");
                        }
                    });
                    msgReply = "end";
                    msgReply = Utilities.bytesToHex(Utilities.cipher(msgReply.getBytes(), Utils.password.getBytes()));
                    dataOutputStream.writeUTF(msgReply);
                }

            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            final String errMsg = e.toString();
            this.activity.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    consola.append("\nServer: " + errMsg);
                }
            });

        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            if (dataInputStream != null) {
                try {
                    dataInputStream.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            if (dataOutputStream != null) {
                try {
                    dataOutputStream.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }
}

