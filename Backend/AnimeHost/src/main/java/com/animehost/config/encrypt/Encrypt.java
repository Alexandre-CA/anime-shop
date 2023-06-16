package com.animehost.config.encrypt;

import java.math.BigInteger;
import java.security.MessageDigest;

public class Encrypt {

    public static String EncryptPass(String password){
        String returnPass = "";
        MessageDigest md;
        try{
            md = MessageDigest.getInstance("MD5");
            BigInteger hash = new BigInteger(1,md.digest(password.getBytes()));
            returnPass = hash.toString(16);
        } catch (Exception e){}
        return returnPass;
    }
}
