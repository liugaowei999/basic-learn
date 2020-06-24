package com.cttic.liugw.SecureRandom;

import java.security.SecureRandom;
import java.security.Security;

/**
 * @author liugw
 * @Package com.cttic.liugw.SecureRandom
 * @Description: ${TODO}
 * @date 2020/5/22 14:54
 */
public class SecureRandomTest {
    public static void main(String[] args) throws Exception {
        final SecureRandom random = new SecureRandom();
        long seedGeneratorEndTime = System.nanoTime();
        byte[] seed = random.generateSeed(8);
        long s = ((long) seed[0] & 0xff) << 56 |
                ((long) seed[1] & 0xff) << 48 |
                ((long) seed[2] & 0xff) << 40 |
                ((long) seed[3] & 0xff) << 32 |
                ((long) seed[4] & 0xff) << 24 |
                ((long) seed[5] & 0xff) << 16 |
                ((long) seed[6] & 0xff) <<  8 |
                (long) seed[7] & 0xff;
        System.out.println("s=" + s + " , cost=" + (System.nanoTime() - seedGeneratorEndTime)/1000000);


        SecureRandom sr;
        boolean pass = true;
        for (String mech : new String[]{"Hash_DRBG", "HMAC_DRBG", "CTR_DRBG"}) {
            try {
                System.out.println("Testing " + mech + "...");
                Security.setProperty("securerandom.drbg.config", mech);

                // Check auto reseed works
                sr = SecureRandom.getInstance("DRBG");
                sr.nextInt();
                sr = SecureRandom.getInstance("DRBG");
//                sr.reseed();
                sr = SecureRandom.getInstance("DRBG");
                byte[] bytes = sr.generateSeed(10);
            } catch (Exception e) {
                pass = false;
                e.printStackTrace(System.out);
            }
        }
        if (!pass) {
            throw new RuntimeException("At least one test case failed");
        }


    }
}
