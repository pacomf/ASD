package com.cryptull.sockets;

import android.app.Activity;
import android.widget.TextView;

import com.cryptull.asd.Utilities;
import com.cryptull.pak.PAK;

import java.math.BigInteger;
import java.net.ServerSocket;

/**
 * Created by Paco on 21/10/2014.
 */
public class Utils {

    public static ServerSocket serverSocket;

    public static PAK pak;

    public static long startTime;

    public static String password = "password";

    public static void RunServer (Activity activity, Integer port, TextView consola){
        Thread socketServerThread = new Thread(new SocketServerThread(activity, port, consola));
        socketServerThread.start();
    }

    public static void ConnectClientToServer(String ip, Integer port, String msg, String password, int step, TextView consola, Activity activity){
        msg = Utilities.bytesToHex(Utilities.cipher(msg.getBytes(), password.getBytes()));
        MyClientTask myClientTask = new MyClientTask(ip,port, msg, step, consola, activity);
        myClientTask.execute();
    }

    public static String createPackage (int step, BigInteger p1, BigInteger p2){
        String ret = String.valueOf(step)+":";
        if (p1 != null){
            ret += p1.toString();
        }
        if (p2 != null){
            ret += ":"+p2.toString();
        }
        return ret;
    }

    public static BigInteger[] unrollPackage (String pack){
        String[] segs = pack.split(":");
        if (segs.length == 2){
            return new BigInteger[]{new BigInteger(segs[0]), new BigInteger(segs[1]), null};
        } else {
            return new BigInteger[]{new BigInteger(segs[0]), new BigInteger(segs[1]), new BigInteger(segs[2])};
        }
    }
}
