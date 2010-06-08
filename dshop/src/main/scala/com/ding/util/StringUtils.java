/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ding.util;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.StringBufferInputStream;
import org.mozilla.intl.chardet.nsDetector;
import org.mozilla.intl.chardet.nsICharsetDetectionObserver;
import org.mozilla.intl.chardet.nsPSMDetector;

/**
 *
 * @author tiger
 */
public class StringUtils {

    static public byte[] gbk2utf8(String chenese) {

        char c[] = chenese.toCharArray();

        byte[] fullByte = new byte[3 * c.length];
        for (int i = 0; i < c.length; i++) {
            int m = (int) c[i];
            String word = Integer.toBinaryString(m);
            StringBuffer sb = new StringBuffer();
            int len = 16 - word.length();
            for (int j = 0; j < len; j++) {
                sb.append("0");
            }
            sb.append(word);
            sb.insert(0, "1110");
            sb.insert(8, "10");
            sb.insert(16, "10");
            String s1 = sb.substring(0, 8);
            String s2 = sb.substring(8, 16);
            String s3 = sb.substring(16);
            byte b0 = Integer.valueOf(s1, 2).byteValue();
            byte b1 = Integer.valueOf(s2, 2).byteValue();
            byte b2 = Integer.valueOf(s3, 2).byteValue();
            byte[] bf = new byte[3];
            bf[0] = b0;
            fullByte[i * 3] = bf[0];
            bf[1] = b1;
            fullByte[i * 3 + 1] = bf[1];
            bf[2] = b2;
            fullByte[i * 3 + 2] = bf[2];
        }
        return fullByte;
    }

//        static public boolean found = false;
    static public void stringCharsetDetect(int language, InputStream in) throws Exception {
        int lang = language;
        nsDetector det = new nsDetector(lang);
        // Set an observer...
        // The Notify() will be called when a matching charset is found.

        boolean found = false;
        det.Init(new nsICharsetDetectionObserver() {

            public void Notify(String charset) {
                System.out.println("CHARSET = " + charset);
            }
        });
        BufferedInputStream imp = new BufferedInputStream(in);
        byte[] buf = new byte[1024];
        int len;
        boolean done = false;
        boolean isAscii = true;
        while ((len = imp.read(buf, 0, buf.length)) != -1) {

            // Check if the stream is only ascii.
            if (isAscii) {
                isAscii = det.isAscii(buf, len);
            }

            // DoIt if non-ascii and not done yet.
            if (!isAscii && !done) {
                done = det.DoIt(buf, len, false);
            }
        }
        det.DataEnd();
        imp.close();
        in.close();
        
        if (isAscii) {
            System.out.println("CHARSET = ASCII");
            found = true;
        }

        if (!found) {
            String prob[] = det.getProbableCharsets();
            for (int i = 0; i < prob.length; i++) {
                System.out.println("Probable Charset = " + prob[i]);
            }
        }
    }
}
