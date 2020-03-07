package com.beimi.util;

import java.util.Random;

public class RandomKey {  
    /** 
     * 生成随机密码 
     *  
     * @param pwd_len 
     *            生成的密码的总长度 
     * @return 密码的字符串 
     */  
    public static String genRandomNum(int pwd_len) {  
        int i; // 生成的随机数  
        int count = 0; // 生成的密码的长度  
        char[] str = { '0', '1', '2', '3', '4', '5', '6', '7', '8','9'};  
  
        final int maxNum = str.length - 1;  
        StringBuilder pwd = new StringBuilder("");  
  
        Random r = new Random();  
        while (count < pwd_len) {  
            // 生成随机数，取绝对值，防止生成负数，  
            i = Math.abs(r.nextInt(maxNum)); // 生成的数最大为36-1  
  
            if (i >= 0 && i < str.length) {  
                pwd.append(str[i]);  
                count++;  
            }  
        }  
  
        return pwd.toString();  
    }  
}  