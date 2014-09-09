package com.cryptull.asd;

import android.util.Base64;

import java.security.Key;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.ArrayList;
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
        try {
            Cipher c = Cipher.getInstance("DES/ECB/PKCS5Padding");
            c.init(Cipher.ENCRYPT_MODE, getKey(key));
            encodedBytes = c.doFinal(plainText);
        } catch (Exception e) {}

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
        return true;
    }

    public static byte[] decipher(byte[] cipherText, byte[] key){
        byte[] decodedBytes = null;
        try {
            Cipher c = Cipher.getInstance("DES/ECB/PKCS5Padding");
            c.init(Cipher.DECRYPT_MODE, getKey(key));
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

    public static void setG (){
        Utilities.dim = 5;
        SolG = new ArrayList<Integer>();
        SolG.add(1);
        SolG.add(3);
        SolG.add(4);
        SolG.add(2);
        SolG.add(5);
        G = new boolean[][]{
                { false, true, true, true, true },
                { true, false, false, true, true },
                { true, false, false, true, false },
                { true, true, true, false, true },
                { true, true, false, true, false }
        };

    }


}
