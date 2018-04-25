package com.huashengke.com.tools;

import net.sourceforge.pinyin4j.PinyinHelper;

import java.util.Arrays;
import java.util.List;

public class StringUtil {
    private static final char CHINESE_BLANK_CHAR = '\u3000';

    private static final List<String> CHINESE_OTHERS = Arrays.asList("其他", "其它");

    public static boolean isStringEmpty(String str){
        return str == null || str.trim().isEmpty();
    }

    public static Character toGroupLetter(String vocation) {

        if (isStringEmpty(vocation))
            return null;

        String v = vocation.trim();
        if (CHINESE_OTHERS.stream().anyMatch(vocation::startsWith)) {
            return CHINESE_BLANK_CHAR;
        }

        char ch = v.charAt(0);
        Character letter = null;

        if ((ch >= 'A' && ch <= 'Z') || (ch >= 'a' && ch <= 'z'))
            letter = Character.toUpperCase(ch);
        else if (ch >= '0' && ch <= '9') {
            letter = toNumChar(ch);
        } else {
            String[] pinyins = PinyinHelper.toHanyuPinyinStringArray(ch);

            if (pinyins == null || "none0".equals(pinyins[0]))
                return null;
            else {
                ch = pinyins[0].charAt(0);
                letter = Character.toUpperCase(ch);
            }
        }
        return letter;
    }

    public static String reverse(String str) {
        return str == null?null:(new StringBuffer(str)).reverse().toString();
    }

    private static Character toNumChar(char numLetter) {
        switch (numLetter) {
            case '0':
            case '6':
                return 'L';
            case '1':
                return 'Y';
            case '2':
                return 'E';
            case '3':
            case '4':
                return 'S';
            case '5':
                return 'W';
            case '7':
                return 'Q';
            case '8':
                return 'B';
            case '9':
                return 'J';
        }
        return null;
    }
}
