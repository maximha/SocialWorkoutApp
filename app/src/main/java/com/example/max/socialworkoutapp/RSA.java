package com.example.max.socialworkoutapp;

import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import android.util.Log;


public class RSA {

    private PrivateKey privKey;
    private PublicKey pubKey;

    public RSA(PrivateKey privKey, PublicKey pubKey) {
        this.privKey = privKey;
        this.pubKey = pubKey;
    }


    public static KeyPair generatePair() {

        KeyPairGenerator keyGen = null;
        try {
            keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(2048);
        } catch (NoSuchAlgorithmException ex) {
            Log.e("RSA", "Error NoSuchAlgorithmException in RSA class : " + ex.getMessage());
        }

        KeyPair pair = keyGen.generateKeyPair();

        return pair;
    }

    public byte[] encryptRSA(byte[] data) throws
            NoSuchAlgorithmException,
            NoSuchPaddingException,
            InvalidKeyException,
            IllegalBlockSizeException,
            BadPaddingException
    {

        Cipher ciph = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        ciph.init(Cipher.ENCRYPT_MODE, pubKey);

        return ciph.doFinal(data);
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

    public byte[] sign(byte[] data) throws
            SignatureException,
            InvalidKeyException,
            NoSuchAlgorithmException
    {
        Signature sig = Signature.getInstance("SHA1withRSA");
        sig.initSign(privKey);
        sig.update(data);
        byte[] signature = sig.sign();

        return signature;
    }

    public Boolean verifySignature(byte[] data, byte[] signature) throws
            NoSuchAlgorithmException,
            SignatureException,
            InvalidKeyException
    {
        Signature sig = Signature.getInstance("SHA1withRSA");
        System.out.println("VerSig, key: " + pubKey.getAlgorithm());
        sig.initVerify(pubKey);
        System.out.println("VerSig, data: " + data);
        sig.update(data);

        return sig.verify(signature);
    }
}
