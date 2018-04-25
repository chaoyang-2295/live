package com.huashengke.com.tools.nim;

import com.huashengke.com.tools.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class EncodeUtil {

    private static Logger logger = LoggerFactory.getLogger(EncodeUtil.class);

    private static final char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public static String encode(String plaintext, String method) {
        if (StringUtil.isStringEmpty(plaintext)) {
            return "";
        }
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(method);
            messageDigest.update(plaintext.getBytes());
            return getFormattedText(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            logger.error("Encode error {"+plaintext+"} msg {"+e.getMessage()+"}");
        }

        return "";
    }

    /**
     * Takes the raw bytes from the digest and formats them correct.
     *
     * @param bytes the raw bytes from the digest.
     * @return the formatted bytes.
     */
    protected static String getFormattedText(byte[] bytes) {
        int len = bytes.length;
        StringBuilder buf = new StringBuilder(len * 2);
        // 把密文转换成十六进制的字符串形式
        for (byte aByte : bytes) {
            buf.append(HEX_DIGITS[(aByte >> 4) & 0x0f]);
            buf.append(HEX_DIGITS[aByte & 0x0f]);
        }
        return buf.toString();
    }

    public static String getCheckSum(String appSecret, String nonce, String curTime) {
        return encode( appSecret + nonce + curTime,"sha1");
    }

    // 计算并获取md5值
    public static String getMD5(String requestBody) {
        return encode( requestBody,"md5");
    }
}
