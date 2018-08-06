package com.example.administrator.demotest.Utils;

import java.io.ByteArrayOutputStream;

/**
 * @author zhangheng
 * @date 2018/5/31
 */

public class Base64 {
    static final char[] charTab = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"
            .toCharArray();

    /**
     * Create a base64 encoded string.
     *
     * @param data
     * @return encoded
     */
    public static String encode(byte[] data) {
        return encode(data, 0, data.length, null).toString();
    }

    /**
     * Encodes the part of the given byte array denoted by start and len to the
     * Base64 format. The encoded data is appended to the given StringBuffer. If
     * no StringBuffer is given, a new one is created automatically. The
     * StringBuilder is the return value of this method.
     *
     * @param data
     * @param start
     * @param len
     * @param buf
     * @return Encoded string
     */
    public static StringBuilder encode(byte[] data, int start, int len,
                                       StringBuilder buf) {

        if (buf == null)
            buf = new StringBuilder(data.length * 3 / 2);

        int end = len - 3;
        int i = start;
        int n = 0;

        while (i <= end) {
            int d = (((data[i]) & 0x0ff) << 16)
                    | (((data[i + 1]) & 0x0ff) << 8) | ((data[i + 2]) & 0x0ff);

            buf.append(charTab[(d >> 18) & 63]);
            buf.append(charTab[(d >> 12) & 63]);
            buf.append(charTab[(d >> 6) & 63]);
            buf.append(charTab[d & 63]);

            i += 3;

//            if (n++ >= 14) {
//                n = 0;
//                buf.append("\r\n");
//            }
        }

        if (i == start + len - 2) {
            int d = (((data[i]) & 0x0ff) << 16) | (((data[i + 1]) & 255) << 8);

            buf.append(charTab[(d >> 18) & 63]);
            buf.append(charTab[(d >> 12) & 63]);
            buf.append(charTab[(d >> 6) & 63]);
            buf.append("=");
        } else if (i == start + len - 1) {
            int d = ((data[i]) & 0x0ff) << 16;

            buf.append(charTab[(d >> 18) & 63]);
            buf.append(charTab[(d >> 12) & 63]);
            buf.append("==");
        }

        return buf;
    }

    static int decode(char c) {
        if (c >= 'A' && c <= 'Z')
            return (c) - 65;
        else if (c >= 'a' && c <= 'z')
            return (c) - 97 + 26;
        else if (c >= '0' && c <= '9')
            return (c) - 48 + 26 + 26;
        else
            switch (c) {
                case '+':
                    return 62;
                case '/':
                    return 63;
                case '=':
                    return 0;
                default:
                    String e = new StringBuilder().append("错误的数据格式引起decode出错: ").append(c).toString();
                    throw new RuntimeException();
            }
    }

    /**
     * Decodes the given Base64 encoded String to a new byte array. The byte
     * array holding the decoded data is returned.
     *
     * @param s
     * @return The real thingi
     */
    private static ByteArrayOutputStream decodeToStream(String s) {
        int i = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        int len = s.length();

        while (true) {
            while (i < len && s.charAt(i) <= ' ')
                i++;

            if (i == len)
                break;

            char ch0 = s.charAt(i);
            char ch1 = s.charAt(i + 1);
            char ch2 = s.charAt(i + 2);
            char ch3 = s.charAt(i + 3);
            int tri = (decode(ch0) << 18) + (decode(ch1) << 12)
                    + (decode(ch2) << 6) + (decode(ch3));

            bos.write((tri >> 16) & 255);
            if (s.charAt(i + 2) == '=')
                break;
            bos.write((tri >> 8) & 255);
            if (s.charAt(i + 3) == '=')
                break;
            bos.write(tri & 255);

            i += 4;
        }
        return bos;
    }

    public static byte[] decodeToBytes(String s) {
        return (s == null) ? null : decodeToStream(s).toByteArray();
    }

    public static String decodeToString(String s) {
        return (s == null) ? null : decodeToStream(s).toString();
    }
}
