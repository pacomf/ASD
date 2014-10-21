package com.cryptull.sockets;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.TextView;

import com.cryptull.asd.Utilities;
import com.cryptull.pak.PAK;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by Paco on 21/10/2014.
 */
public class MyClientTask extends AsyncTask<Void, Void, Void> {

    String dstAddress;
    int dstPort;
    String response = "";
    String msgToServer;
    int step;
    TextView consola;
    Activity activity;

    MyClientTask(String addr, int port, String msgTo, int step, TextView consola, Activity act) {
        dstAddress = addr;
        dstPort = port;
        msgToServer = msgTo;
        this.step = step;
        this.consola = consola;
        this.activity = act;
    }

    @Override
    protected Void doInBackground(Void... arg0) {

        Socket socket = null;
        DataOutputStream dataOutputStream = null;
        DataInputStream dataInputStream = null;

        try {
            socket = new Socket(dstAddress, dstPort);
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
            dataInputStream = new DataInputStream(socket.getInputStream());

            if(msgToServer != null){
                dataOutputStream.writeUTF(msgToServer);
            }

            response = dataInputStream.readUTF();

        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            response = "UnknownHostException: " + e.toString();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            response = "IOException: " + e.toString();
        } finally {
            if (socket != null) {
                try {
                    socket.close();
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

            if (dataInputStream != null) {
                try {
                    dataInputStream.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        try {
            response = new String(Utilities.decipher(Utilities.hexToBytes(response), Utils.password.getBytes()), "UTF-8");
            if ((!response.equals("ready")) && (!response.equals("end"))){
                BigInteger[] bi = Utils.unrollPackage(response);
                int step = bi[0].intValue();
                step++;
                BigInteger[] bir = Utils.pak.processPackage(step, bi[1], bi[2], "idA", "idB", Utils.password, consola, activity);
                if (bir != null) {
                    String msgReply = Utils.createPackage(step, bir[0], bir[1]);
                    Utils.ConnectClientToServer("127.0.0.1", 8080, msgReply, Utils.password, step, consola, activity);
                }
            } else if (response.equals("ready")){
                //final long duration = System.currentTimeMillis() - Utils.startTime;
                //consola.append("\nTiempo Requerido: " + duration + " ms.");
                if (Utils.pak.K != null){
                    String msg = "Hello world!";
                    Utils.ConnectClientToServer("127.0.0.1", 8080, msg, Utils.pak.K.toString(), ++step, consola, activity);
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        super.onPostExecute(result);
    }

}

