package encryptionlab;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.charset.StandardCharsets;
import java.util.Base64;


public class DesSolution {
    public static void main(String[] args) throws Exception {
        String fileName1 = "shorttext.txt";
        String shorttext = "";
        String line;
        BufferedReader bufferedReader1 = new BufferedReader( new FileReader(fileName1));
        while((line= bufferedReader1.readLine())!=null){
            shorttext = shorttext +"\n" + line;
        }
        System.out.println("Original content: "+ shorttext.substring(1,20)+"...");

        String fileName2 = "longtext.txt";
        String longtext = "";
        BufferedReader bufferedReader2 = new BufferedReader( new FileReader(fileName2));
        while((line= bufferedReader2.readLine())!=null){
            longtext = longtext +"\n" + line;
        }
        System.out.println("Original content: "+ longtext.substring(1,20)+"...");

//TODO: generate secret key using DES algorithm
        KeyGenerator keyGen = KeyGenerator.getInstance("DES"); //instance of key generators
        SecretKey desKey = keyGen.generateKey(); // generate a key

//TODO: create cipher object, initialize the ciphers with the given key, choose encryption mode as DES
        Cipher desCipher = Cipher.getInstance("DES/ECB/PKCS5Padding"); //create a cipher with DES encryption
        desCipher.init(Cipher.ENCRYPT_MODE, desKey); //pass in generated des key

//TODO: do encryption, by calling method Cipher.doFinal().
        byte[] cipherBytes1=desCipher.doFinal(shorttext.getBytes(StandardCharsets.UTF_8)); // convert data to a bytearray
//        System.out.println(new String(cipherBytes1));
        byte[] cipherBytes2=desCipher.doFinal(longtext.getBytes(StandardCharsets.UTF_8));

//TODO: print the length of output encrypted byte[], compare the length of file shorttext.txt and longtext.txt
//        System.out.println(shorttext.getBytes(StandardCharsets.UTF_8).length);
//        System.out.println(longtext.getBytes(StandardCharsets.UTF_8).length);
        System.out.println("shorttext.txt length: " + cipherBytes1.length);
        System.out.println("longtext.txt length: " + cipherBytes2.length);

//TODO: do format conversion. Turn the encrypted byte[] format into base64format String using Base64
//TODO: print the encrypted message (in base64format String format)
        System.out.println("Ciphered shorttext.txt: " + Base64.getEncoder().encodeToString(cipherBytes1));
        System.out.println("Ciphered longtext.txt: " + Base64.getEncoder().encodeToString(cipherBytes2));

//TODO: create cipher object, initialize the ciphers with the given key, choose decryption mode as DES
        Cipher desDecipher = Cipher.getInstance("DES/ECB/PKCS5Padding"); //create a cipher with DES decryption
        desDecipher.init(Cipher.DECRYPT_MODE, desKey); //pass in generated des key

//TODO: do decryption, by calling method Cipher.doFinal().
        byte[] decipherBytes1=desDecipher.doFinal(cipherBytes1);
        byte[] decipherBytes2=desDecipher.doFinal(cipherBytes2);

//TODO: do format conversion. Convert the decrypted byte[] to String, using "String a = new String(byte_array);"
        String decipherText1= new String(decipherBytes1, StandardCharsets.UTF_8);
        String decipherText2= new String(decipherBytes2, StandardCharsets.UTF_8);

//TODO: print the decrypted String text and compare it with original text
        System.out.println("Deciphered shorttext.txt: " + decipherText1.substring(1,20)+"...");
        System.out.println("Deciphered shorttext.txt: " + decipherText2.substring(1,20)+"...");
        System.out.println("shorttext.txt is same: "+shorttext.equals(decipherText1));
        System.out.println("longtext.txt is same: "+longtext.equals(decipherText2));
    }
}