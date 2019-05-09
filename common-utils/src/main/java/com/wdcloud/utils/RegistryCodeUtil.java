package com.wdcloud.utils;

public class RegistryCodeUtil {

    private static final char[] words = new char[]{'F', 'L', 'G', 'W', '5', 'X', 'C', '3', '9', 'Z',
            'M', '6', '7', 'Y', 'R', 'T', '2', 'H', 'S', '8',
            'D', 'V', 'E', 'J', '4', 'K', 'Q', 'P', 'U', 'A',
            'N', 'B'};

    private static final int binLen = words.length;

    private static final char[] replace = {words[0], words[0], words[0]};

    private static final int initLen = 4;

    public static String generateRegistryCode(Long id){
        assert id == null : "course id is needed!";

        StringBuffer code = new StringBuffer();
        while((id / binLen) > 0) {
            int index = (int)(id % binLen);
            code.append(words[index]);
            id /= binLen;
        }
        code.append(words[(int)(id % binLen)]);

        if(code.length() < initLen){
            code.append(replace, 0,initLen-code.length());
        }
        return code.reverse().toString().toUpperCase();
    }
}
