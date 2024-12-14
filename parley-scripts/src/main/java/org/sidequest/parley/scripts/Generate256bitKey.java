package org.sidequest.parley.scripts;

import java.security.SecureRandom;
import java.util.Base64;

public class Generate256bitKey {

    public static void main(String[] args) {
        SecureRandom secureRandom = new SecureRandom();
        byte[] key = new byte[32];
        secureRandom.nextBytes(key);
        String secret = Base64.getEncoder().encodeToString(key);
        System.out.println("The new secret key is: " + secret);
    }
}
