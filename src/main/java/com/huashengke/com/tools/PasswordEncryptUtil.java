package com.huashengke.com.tools;
import java.security.MessageDigest;

/**
 * Created by chentz on 2017/10/17.
 */
public class PasswordEncryptUtil {

    public static String encryptedValue(String aPlainTextValue) {
        if (StringUtil.isStringEmpty(aPlainTextValue)) {
            throw new IllegalArgumentException("Plain text value to encrypt must be provided");
        }
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(aPlainTextValue.getBytes("UTF-8"));
            // 进行哈希计算并返回结果
            byte[] btResult = messageDigest.digest();
            // 进行哈希计算后得到的数据的长度
            StringBuffer sb = new StringBuffer();
            for (byte b : btResult) {
                int bt = b & 0xff;
                if (bt < 16) {
                    sb.append(0);
                }
                sb.append(Integer.toHexString(bt));
            }
            return sb.toString();

        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
