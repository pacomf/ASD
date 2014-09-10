package com.cryptull.asd;

import android.util.Base64;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.security.Key;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Paco on 08/09/2014.
 */
public class Utilities {

    public static List<Integer> isomorfismo;
    public static boolean[][] G;
    public static boolean[][] Gi;
    public static List<Integer> SolGi;
    public static List<Integer> SolG;
    public static byte[] key;
    public static int dim;

    public static SecretKeySpec getKey(byte[] key){
        // Set up secret key spec for 128-bit AES encryption and decryption

        SecretKeySpec sks = null;
        try {
            sks = new SecretKeySpec(key, "DES");
        } catch (Exception e) {}
        return sks;
    }

    public static byte[] cipher(byte[] plainText, byte[] key){
        // Encode the original data with AES
        byte[] encodedBytes = null;
        byte[] key8 = new byte[8];
        for (int i=0; i<8; i++){
            key8[i] = key[i];
        }
        try {
            Cipher c = Cipher.getInstance("DES/ECB/PKCS5Padding");
            c.init(Cipher.ENCRYPT_MODE, getKey(key8));
            encodedBytes = c.doFinal(plainText);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return encodedBytes;
    }

    public static List<Integer> generateIso (int nodesNumbers){
        Random rng = new Random(); // Ideally just create one instance globally
        // Note: use LinkedHashSet to maintain insertion order
        Set<Integer> generated = new LinkedHashSet<Integer>();
        while (generated.size() < nodesNumbers){
            Integer next = rng.nextInt(nodesNumbers) + 1;
            // As we're adding to a set, this will automatically do a containment check
            generated.add(next);
        }
        List<Integer> list = new ArrayList(generated);
        //list.add(3);list.add(5);list.add(2);list.add(1);list.add(4);
        return list;
    }

    public static void generateGi (){
        isomorfismo = generateIso(dim);
        Gi = new boolean[dim][dim];
        for (int i=0; i<dim; i++){
            for (int j=0; j<dim; j++){
                Gi[i][j] = G[isomorfismo.indexOf(i+1)][isomorfismo.indexOf(j+1)];
            }
        }

        SolGi = new ArrayList<Integer>();
        for (int i=0; i<dim; i++) {
            SolGi.add(isomorfismo.get(SolG.get(i)-1));
        }
    }

    public static boolean checkReto0 (boolean[][] Gi_, List<Integer> isomorfismo_){
        boolean[][] Gi_aux = new boolean[dim][dim];
        for (int i=0; i<dim; i++){
            for (int j=0; j<dim; j++){
                Gi_aux[i][j] = G[isomorfismo_.indexOf(i+1)][isomorfismo_.indexOf(j+1)];
            }
        }

        for (int i=0; i<dim; i++){
            for (int j=0; j<dim; j++){
                if (Gi_aux[i][j] != Gi_[i][j])
                   return false;
            }
        }

        List<Integer> SolGi_ = new ArrayList<Integer>();
        for (int i=0; i<dim; i++) {
            SolGi_.add(isomorfismo_.get(Utilities.SolG.get(i)-1));
        }
        Utilities.key = Utilities.hash(Utilities.list2String(SolGi_).getBytes());
        return true;
    }

    public static boolean checkReto1 (boolean[][] Gi_, List<Integer> SolGi_){
        boolean[] visited = new boolean[dim];
        visited[SolGi_.get(0)-1] = true;
        for (int i=0; i<dim-1; i++){
            if (Gi_[SolGi_.get(i)-1][SolGi_.get(i+1)-1]){
                if (visited[SolGi_.get(i+1)-1]){
                    return false;
                } else {
                    visited[SolGi_.get(i + 1)-1] = true;
                }
            } else {
                return false;
            }
        }

        for (int i=0; i<dim; i++){
            if (!visited[i])
                return false;
        }
        Utilities.key = Utilities.hash(Utilities.list2String(SolGi_).getBytes());
        // TODO: Concatener a esta clave la G
        return true;
    }

    public static boolean checkReto(boolean reto, boolean[][] Gi_, List<Integer> SolGi_, List<Integer> Iso_){
        if (reto)
            return checkReto1(Gi_, SolGi_);
        else
            return checkReto0(Gi_, Iso_);
    }

    public static byte[] decipher(byte[] cipherText, byte[] key){
        byte[] decodedBytes = null;
        byte[] key8 = new byte[8];
        for (int i=0; i<8; i++){
            key8[i] = key[i];
        }
        try {
            Cipher c = Cipher.getInstance("DES/ECB/PKCS5Padding");
            c.init(Cipher.DECRYPT_MODE, getKey(key8));
            decodedBytes = c.doFinal(cipherText);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return decodedBytes;
    }

    public static byte[] hash(byte[] text){
        try {
            return MessageDigest.getInstance("SHA256").digest(text);
        } catch (Exception e) {
            return null;
        }

    }

    public static boolean hashBinary(byte[] text){
        try {
            return Utilities.getLSB(MessageDigest.getInstance("SHA256").digest(text)[0]);
        } catch (Exception e) {
            return false;
        }

    }

    public static boolean getLSB(byte b){
       return ((b >> 7) & 1) == 1;
    }

    public static String Bytes2String(byte[] text){
        try {
            return new String(text, "UTF-8");
        } catch(Exception e){
            return null;
        }
    }

    public static byte[] String2Bytes(String text){
        return text.getBytes();
    }

    public static String Bytes2Binary(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * Byte.SIZE);
        for( int i = 0; i < Byte.SIZE * bytes.length; i++ )
            sb.append((bytes[i / Byte.SIZE] << i % Byte.SIZE & 0x80) == 0 ? '0' : '1');
        return sb.toString();
    }

    public static long timeFunctionMillis (){
        final long startTime = System.currentTimeMillis();
        // HERE THE FUNCTION TO CALCULATED
        final long duration = System.currentTimeMillis() - startTime;
        return duration;
    }

    public static long timeFunctionNano (){
        final long startTime = System.nanoTime();
        // HERE THE FUNCTION TO CALCULATED
        final long duration = System.nanoTime() - startTime;
        return duration;
    }

    public static void printGraph (boolean[][] graph){
        System.out.println("Grafo: ");
        for (int i=0; i<dim; i++){
            for (int j=0; j<dim; j++){
                System.out.print(graph[i][j] ? 1 : 0);
            }
            System.out.println();
        }
    }

    public static String bytesToHex(byte[] bytes) {
        final char[] hexArray = "0123456789ABCDEF".toCharArray();
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static byte[] hexToBytes(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    public static String graph2Bin(boolean[][] graph){
        String n = "";
        for (int i=0; i<dim; i++){
            for (int j=0; j<dim; j++){
                n += (graph[i][j] ? "1" : "0");
            }
        }
        return n;
    }

    public static boolean[][] bin2Graph (String number){
        final boolean[][] graph = new boolean[dim][dim];
        int base = dim*dim;
        for (int i = 0; i < base; i++) {
            graph[(base - 1 - i)/dim][(base - 1 - i)%dim] = (number.charAt((base-1)-i)) == '1' ? true : false;
        }
        return graph;
    }

    public static String list2String (List<Integer> list){
        return list.toString().replaceAll("\\[|\\]", "").replaceAll(" ", "");
    }

    public static List<Integer> string2List (String str){
        List<Integer> list = new ArrayList<Integer>();
        String[] ls = (str).split(",");
        for (int i=0; i<ls.length; i++){
            list.add(i, Integer.parseInt(ls[i]));
        }
        return list;
    }

    public static byte[] longToBytes(long l) {
        ArrayList<Byte> bytes = new ArrayList<Byte>();
        while (l != 0) {
            bytes.add((byte) (l % (0xff + 1)));
            l = l >> 8;
        }
        byte[] bytesp = new byte[bytes.size()];
        for (int i = bytes.size() - 1, j = 0; i >= 0; i--, j++) {
            bytesp[j] = bytes.get(i);
        }
        return bytesp;
    }

    public static String getSegment(boolean cipher, byte[] key){
        generateGi();
        String graphL = Utilities.graph2Bin(Utilities.Gi);
        String seg = graphL+":";
        byte[] key_aux = key;
        if (Utilities.hashBinary(graphL.getBytes())){
            seg+=Utilities.list2String(Utilities.SolGi);
            // TODO: Completar la KEY con la G
            Utilities.key = Utilities.hash(Utilities.list2String(Utilities.SolGi).getBytes());
        } else {
            seg+=Utilities.list2String(Utilities.isomorfismo);
            Utilities.key = Utilities.hash(Utilities.list2String(Utilities.SolGi).getBytes());
        }
        if (cipher){
            try {
                seg = Utilities.bytesToHex(Utilities.cipher(seg.getBytes(), key));
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        seg+="|";
        return seg;
    }

    public static String getPackage(int segments, String secret){
        String message = null;
        try {
            if (segments == 0)
                return message;
            message = getSegment(false, null);
            for (int i = 1; i < segments; i++) {
                message += getSegment(true, Utilities.key);
            }
            message += Utilities.bytesToHex(Utilities.cipher(secret.getBytes(), key)) + "|";
        } catch (Exception e){
            e.printStackTrace();
        }
        return message;
    }


    public static boolean unwrapSegment (String segment){
        String[] parts = segment.split(":");
        Utilities.Gi = Utilities.bin2Graph(parts[0]);
        if (Utilities.hashBinary(parts[0].getBytes())){
            Utilities.SolGi=Utilities.string2List(parts[1]);
            Utilities.isomorfismo = null;
            return true;
        } else {
            Utilities.isomorfismo=Utilities.string2List(parts[1]);
            Utilities.SolGi = null;
            return false;
        }
    }

    public static boolean unwrapSegmentCipher (String segmentCipher, byte[] key){
        try {
            String segment = new String(Utilities.decipher(Utilities.hexToBytes(segmentCipher), key), "UTF-8");
            return unwrapSegment(segment);
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public static String proccessPackage (String message){
        try {
            String[] segments = message.split("\\|");
            String ret = null;
            if (!Utilities.checkReto(Utilities.unwrapSegment(segments[0]), Utilities.Gi, Utilities.SolGi, Utilities.isomorfismo)) {
                System.out.println("Nop");
                return ret;
            }
            for (int i = 1; i < segments.length - 1; i++) {
                if (!Utilities.checkReto(Utilities.unwrapSegmentCipher(segments[i], Utilities.key), Utilities.Gi, Utilities.SolGi, Utilities.isomorfismo)) {
                    System.out.printf("Nop Cypher");
                    return ret;
                }
            }

            return new String(Utilities.decipher(Utilities.hexToBytes(segments[segments.length - 1]), Utilities.key), "UTF-8");
        } catch (Exception e){
            e.printStackTrace();
            System.out.println(e.getMessage());
            return null;
        }
    }

}
