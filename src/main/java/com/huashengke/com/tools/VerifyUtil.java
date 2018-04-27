package com.huashengke.com.tools;

import java.security.MessageDigest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by yangc on 2018/4/27.
 */
public final class VerifyUtil {

    private static final String TELEPHONE_REGEX = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(18[0,5-9]))\\d{8}$";

    private static final String EMAIL_REGEX = "^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$";

    /**
     * 验证是否为邮箱
     * @param template 验证模板
     * @return         验证成功，返回邮箱，验证失败返回null
     */
    public static String isEmail(String template){

        Pattern p = Pattern.compile(EMAIL_REGEX);
        Matcher m = p.matcher(template);

        if(m.matches()){
            return m.group();
        }
        return null;
    }

    /**
     * 验证是否为手机号
     * @param template 验证模板
     * @return         验证成功，返回手机号，验证失败返回null
     */
    public static String isTelephone(String template){

        Pattern p = Pattern.compile(TELEPHONE_REGEX);
        Matcher m = p.matcher(template);

        if(m.matches()){
            return m.group();
        }
        return null;
    }


    /**
     *  加密
     * @param aPlainTextValue  加密模板
     * @return                 加密后的结果
     */
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
