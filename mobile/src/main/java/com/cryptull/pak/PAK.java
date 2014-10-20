package com.cryptull.pak;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Random;

/**
 * Created by Paco on 20/10/2014.
 */
public class PAK {

    private String password;
    private BigInteger N, g, one, two;

    public PAK (String password){
        this.password = password;
        this.N = new BigInteger("FFFFFFFFFFFFFFFFC90FDAA22168C234C4C6628B80DC1CD129024E08" +
                                "8A67CC74020BBEA63B139B22514A08798E3404DDEF9519B3CD3A431B" +
                                "302B0A6DF25F14374FE1356D6D51C245E485B576625E7EC6F44C42E9" +
                                "A637ED6B0BFF5CB6F406B7EDEE386BFB5A899FA5AE9F24117C4B1FE6" +
                                "49286651ECE65381FFFFFFFFFFFFFFFF", 16);
        this.g = new BigInteger("13", 16);
        this.one = new BigInteger("1", 16);
        this.two = new BigInteger("2", 16);
    }

    public BigInteger generategRa(){
        return modPow(random());
    }

    public BigInteger generategRb(){
        return modPow(random());
    }

    public BigInteger modPow(BigInteger x){
        return this.g.modPow(x, this.N);
    }

    public BigInteger calculateX (String A, String B, BigInteger gRa){
        String str = A + B + this.password;
        return H1(str).multiply(gRa);
    }

    public BigInteger calculateXab (String A, String B, BigInteger Q){
        String str = A + B + this.password;
        return Q.divide(H1(str));
    }

    public BigInteger calculateY (String A, String B, BigInteger gRb){
        String str = A + B + this.password;
        return H2(str).multiply(gRb);
    }

    public BigInteger calculateYba (String A, String B, BigInteger Y){
        String str = A + B + this.password;
        return Y.divide(H2(str));
    }

    public BigInteger calculateS1 (String A, String B, BigInteger gRa, BigInteger gRb){
        BigInteger AB = modPow(gRa.multiply(gRb));
        return H3(A + B + this.password + gRa.toString(16) + gRb.toString(16) + AB.toString(16));
    }

    public BigInteger calculateS2 (String A, String B, BigInteger gRa, BigInteger gRb){
        BigInteger AB = modPow(gRa.multiply(gRb));
        return H4(A + B + this.password + gRa.toString(16) + gRb.toString(16) + AB.toString(16));
    }

    public BigInteger calculateK (String A, String B, BigInteger gRa, BigInteger gRb){
        BigInteger AB = modPow(gRa.multiply(gRb));
        return H5(A + B + this.password + gRa.toString(16) + gRb.toString(16) + AB.toString(16));
    }

    public BigInteger H1 (String str){
        return HA(str, 1);
    }

    public BigInteger H2 (String str){
        return HA(str, 2);
    }

    public BigInteger HA (String str, int type){
        String hash = "";
        try {
            for (int i = 0; i < 10; i++) {
                byte[] dig = (String.valueOf(type) + String.valueOf(i) + str).getBytes();
                byte[] tmp = MessageDigest.getInstance("SHA256").digest(dig);
                byte[] lsB = lsb128(tmp);
                hash += bytesToHex(lsB);
            }
        } catch (Exception e){
            System.out.println("Peto el SHA256");
        }
        return new BigInteger(hash, 16);
    }

    public BigInteger H3 (String str){
        return HB(str, 3);
    }

    public BigInteger H4 (String str){
        return HB(str, 4);
    }

    public BigInteger H5 (String str){
        return HB(str, 5);
    }

    public BigInteger HB (String str, int type){
        try {
            byte[] dig = (String.valueOf(type) + String.valueOf(str.length()) + str + str).getBytes();
            byte[] tmp = MessageDigest.getInstance("SHA256").digest(dig);
            byte[] lsB = lsb128(tmp);
            return new BigInteger(lsB);
        } catch (Exception e){
            System.out.println("Peto el SHA256 en HB");
            return null;
        }
    }

    public byte[] lsb128(byte[] tmp){
        byte[] slice = Arrays.copyOfRange(tmp, tmp.length/2, tmp.length);
        return slice;
    }

    public BigInteger random(){
        Random randomService = new Random();
        StringBuilder sb = new StringBuilder();
        final int RANDOM_HEX_LENGTH = 96; // 384/4
        while (sb.length() < RANDOM_HEX_LENGTH) {
            sb.append(Integer.toHexString(randomService.nextInt()));
        }
        sb.setLength(RANDOM_HEX_LENGTH);
        return new BigInteger(sb.toString(), 16);
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


}
