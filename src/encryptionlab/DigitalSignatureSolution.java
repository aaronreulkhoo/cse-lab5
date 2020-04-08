package encryptionlab;

import javax.crypto.Cipher;
import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.util.Base64;

public class DigitalSignatureSolution {
    public static void main(String[] args) throws Exception {
        //Read the text file and save to String data
        String fileName1 = "shorttext.txt";
        String shorttext = "";
        String line;
        BufferedReader bufferedReader1 = new BufferedReader( new FileReader(fileName1));
        while((line= bufferedReader1.readLine())!=null){
            shorttext = shorttext +"\n" + line;
        }
//        System.out.println("Original content: ["+ shorttext + "]");

        String fileName2 = "longtext.txt";
        String longtext = "";
        BufferedReader bufferedReader2 = new BufferedReader( new FileReader(fileName2));
        while((line= bufferedReader2.readLine())!=null){
            longtext = longtext +"\n" + line;
        }
//        System.out.println("Original content: ["+ longtext + "]");

//TODO: generate a RSA keypair, initialize as 1024 bits, get public key and private key from this keypair.
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(1024);
        KeyPair keyPair = keyGen.generateKeyPair();
        Key publicKey = keyPair.getPublic();
        Key privateKey = keyPair.getPrivate();


//TODO: Calculate message digest, using MD5 hash function
        MessageDigest md1 = MessageDigest.getInstance("MD5");
        byte[] dg1 = md1.digest(shorttext.getBytes(StandardCharsets.UTF_8));
        MessageDigest md2 = MessageDigest.getInstance("MD5");
        byte[] dg2 = md2.digest(longtext.getBytes(StandardCharsets.UTF_8));


//TODO: print the length of output digest byte[], compare the length of file shorttext.txt and longtext.txt
        System.out.println("longtext.txt digest length: " +dg1.length);
        System.out.println("longtext.txt digest length: " +dg2.length);
        String digest1=Base64.getEncoder().encodeToString(dg1);
        String digest2=Base64.getEncoder().encodeToString(dg2);
        System.out.println("shorttext.txt digest: " + digest1);
        System.out.println("longtext.txt digest: " + digest2);
           
//TODO: Create RSA("RSA/ECB/PKCS1Padding") cipher object and initialize is as encrypt mode, use PRIVATE key.
        Cipher rsaCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        rsaCipher.init(Cipher.ENCRYPT_MODE, privateKey);

//TODO: encrypt digest message
        byte[] cipherDigest1 = rsaCipher.doFinal(dg1);
        byte[] cipherDigest2 = rsaCipher.doFinal(dg2);

//TODO: print the encrypted message (in base64format String using Base64) 
        System.out.println("Encrypted shorttext.txt digest: " + Base64.getEncoder().encodeToString(cipherDigest1));
        System.out.println("Encrypted longtext.txt digest: " + Base64.getEncoder().encodeToString(cipherDigest2));

//TODO: Create RSA("RSA/ECB/PKCS1Padding") cipher object and initialize is as decrypt mode, use PUBLIC key.           
        Cipher rsaDecipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        rsaDecipher.init(Cipher.DECRYPT_MODE, publicKey);

//TODO: decrypt message
        String decipherDigest1= Base64.getEncoder().encodeToString(rsaDecipher.doFinal(cipherDigest1));
        String decipherDigest2= Base64.getEncoder().encodeToString(rsaDecipher.doFinal(cipherDigest2));

//TODO: print the decrypted message (in base64format String using Base64), compare with origin digest
        System.out.println("Deciphered shorttext.txt digest: " + decipherDigest1);
        System.out.println("Deciphered shorttext.txt digest: " + decipherDigest2);
        System.out.println("shorttext.txt digest is same: "+decipherDigest1.equals(Base64.getEncoder().encodeToString(dg1)));
        System.out.println("longtext.txt digest is same: "+decipherDigest2.equals(Base64.getEncoder().encodeToString(dg2)));
    }

}