package encryptionlab;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.ByteBuffer;


public class DesImageSolution {
    public static void main(String[] args) throws Exception {
        int image_width;
        int image_length;
        String[] files={"SUTD","triangle"}; //add here
        for (String file:files) {
            // read image file and save pixel value into int[][] imageArray
            BufferedImage img = ImageIO.read(new File(file + ".bmp"));
            image_width = img.getWidth();
            image_length = img.getHeight();
            // byte[][] imageArray = new byte[image_width][image_length];
            int[][] imageArray = new int[image_width][image_length];
            for (int idx = 0; idx < image_width; idx++) {
                for (int idy = 0; idy < image_length; idy++) {
                    imageArray[idx][idy] = img.getRGB(idx, idy);
                }
            }

            // TODO: generate secret key using DES algorithm
            KeyGenerator keyGen = KeyGenerator.getInstance("DES"); //instance of key generator
            SecretKey desKey = keyGen.generateKey(); // generate a key

            // TODO: Create cipher object, initialize the ciphers with the given key, choose encryption algorithm/mode/padding,
            //you need to try both ECB and CBC mode, use PKCS5Padding padding method
            Cipher ecbCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            ecbCipher.init(Cipher.ENCRYPT_MODE, desKey);
            Cipher cbcCipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            cbcCipher.init(Cipher.ENCRYPT_MODE, desKey);

            // define output BufferedImage, set size and format
            BufferedImage outImageECB = new BufferedImage(image_width, image_length, BufferedImage.TYPE_3BYTE_BGR);
            BufferedImage outImageCBC = new BufferedImage(image_width, image_length, BufferedImage.TYPE_3BYTE_BGR);
            BufferedImage outImageECBInv = new BufferedImage(image_width, image_length, BufferedImage.TYPE_3BYTE_BGR);
            BufferedImage outImageCBCInv = new BufferedImage(image_width, image_length, BufferedImage.TYPE_3BYTE_BGR);

            for (int idx = 0; idx < image_width; idx++) {
                // convert each column int[] into a byte[] (each_width_pixel)
                byte[] each_width_pixel = new byte[4 * image_length];
                for (int idy = 0; idy < image_length; idy++) {
                    ByteBuffer dbuf = ByteBuffer.allocate(4);
                    dbuf.putInt(imageArray[idx][idy]);
                    byte[] bytes = dbuf.array();
                    System.arraycopy(bytes, 0, each_width_pixel, idy * 4, 4);
                }

                //decoding upside down
                byte[] each_width_pixelInv = new byte[4 * image_length];
                for (int idy = image_length - 1; idy > 0; idy--) {
                    ByteBuffer dbufInv = ByteBuffer.allocate(4);
                    dbufInv.putInt(imageArray[idx][image_length - idy]);
                    byte[] bytesInv = dbufInv.array();
                    System.arraycopy(bytesInv, 0, each_width_pixelInv, idy * 4, 4);
                }

                // TODO: encrypt each column or row bytes
                byte[] ecbCiphered = ecbCipher.doFinal(each_width_pixel);
                byte[] cbcCiphered = cbcCipher.doFinal(each_width_pixel);
                byte[] ecbCipheredInv = ecbCipher.doFinal(each_width_pixelInv);
                byte[] cbcCipheredInv = cbcCipher.doFinal(each_width_pixelInv);

                // TODO: convert the encrypted byte[] back into int[] and write to outImage (use setRGB)
                for (int idy = 0; idy < image_length; idy++) {
                    byte[] bufECB = new byte[4];
                    byte[] bufCBC = new byte[4];
                    byte[] bufECBInv = new byte[4];
                    byte[] bufCBCInv = new byte[4];
                    ByteBuffer bufferECB = ByteBuffer.wrap(bufECB);
                    ByteBuffer bufferCBC = ByteBuffer.wrap(bufCBC);
                    ByteBuffer bufferECBInv = ByteBuffer.wrap(bufECBInv);
                    ByteBuffer bufferCBCInv = ByteBuffer.wrap(bufCBCInv);
                    System.arraycopy(ecbCiphered, 4 * idy, bufECB, 0, 4);
                    System.arraycopy(cbcCiphered, 4 * idy, bufCBC, 0, 4);
                    System.arraycopy(ecbCipheredInv, 4 * idy, bufECBInv, 0, 4);
                    System.arraycopy(cbcCipheredInv, 4 * idy, bufCBCInv, 0, 4);
                    outImageECB.setRGB(idx, idy, bufferECB.getInt());
                    outImageCBC.setRGB(idx, idy, bufferCBC.getInt());
                    outImageECBInv.setRGB(idx, idy, bufferECBInv.getInt());
                    outImageCBCInv.setRGB(idx, idy, bufferCBCInv.getInt());
                }

            }
            //write outImage into file
            ImageIO.write(outImageECB, "BMP", new File("ECB_" + file + ".bmp"));
            ImageIO.write(outImageCBC, "BMP", new File("CBC_" + file + ".bmp"));
            ImageIO.write(outImageECBInv, "BMP", new File("ECB_" + file + "_Inverted.bmp"));
            ImageIO.write(outImageCBCInv, "BMP", new File("CBC_" + file + "_Inverted.bmp"));
            System.out.println("Encryption Success for " + file);
        }
    }
}