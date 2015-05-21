package com.example.max.socialworkoutapp;

import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import android.util.Log;


public class RSA {

    private PrivateKey privKey;

    public RSA(PrivateKey privKey) {
        this.privKey = privKey;
    }


    public static KeyPair generatePair() {

        KeyPairGenerator keyGen = null;
        try {
            keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(2048);
        } catch (NoSuchAlgorithmException ex) {
            Log.e("RSA", "Error NoSuchAlgorithmException in RSA class : " + ex.getMessage());
        }

        assert keyGen != null;
        return keyGen.generateKeyPair();
    }


    public byte[] decryptRSA(byte[] data) throws
            NoSuchAlgorithmException,
            NoSuchPaddingException,
            InvalidKeyException,
            IllegalBlockSizeException,
            BadPaddingException
    {

        Cipher ciph = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        ciph.init(Cipher.DECRYPT_MODE, privKey);

        return ciph.doFinal(data);
    }

}
