package com.beimi.util;

import java.security.MessageDigest;   

import javax.crypto.KeyGenerator;   
import javax.crypto.Mac;   
import javax.crypto.SecretKey;   
import javax.crypto.spec.SecretKeySpec;
   
public abstract class RSATool {   
    public static final String KEY_SHA = "SHA";   
    public static final String KEY_MD5 = "MD5";   
  
    /**  
     * MAC算法可选以下多种算法  
     *   
     * <pre>  
     * HmacMD5   
     * HmacSHA1   
     * HmacSHA256   
     * HmacSHA384   
     * HmacSHA512  
     * </pre>  
     */  
    public static final String KEY_MAC = "HmacMD5";   
  
    /**  
     * BASE64解密  
     *   
     * @param key  
     * @return  
     * @throws Exception  
     */  
    public static byte[] decryptBASE64(String key) throws Exception {   
        return hex2BYTE(key);   
    }   
  
    /**  
     * BASE64加密  
     *   
     * @param key  
     * @return  
     * @throws Exception  
     */  
    public static String encryptBASE64(byte[] key) throws Exception {   
        return byteHEX(key);   
    }   
  
    /**  
     * MD5加密  
     *   
     * @param data  
     * @return  
     * @throws Exception  
     */  
    public static byte[] encryptMD5(byte[] data) throws Exception {   
  
        MessageDigest md5 = MessageDigest.getInstance(KEY_MD5);   
        md5.update(data);   
  
        return md5.digest();   
  
    }   
  
    /**  
     * SHA加密  
     *   
     * @param data  
     * @return  
     * @throws Exception  
     */  
    public static byte[] encryptSHA(byte[] data) throws Exception {   
  
        MessageDigest sha = MessageDigest.getInstance(KEY_SHA);   
        sha.update(data);   
  
        return sha.digest();   
  
    }   
  
    /**  
     * 初始化HMAC密钥  
     *   
     * @return  
     * @throws Exception  
     */  
    public static String initMacKey() throws Exception {   
        KeyGenerator keyGenerator = KeyGenerator.getInstance(KEY_MAC);   
  
        SecretKey secretKey = keyGenerator.generateKey();   
        return encryptBASE64(secretKey.getEncoded());   
    }   
  
    /**  
     * HMAC加密  
     *   
     * @param data  
     * @param key  
     * @return  
     * @throws Exception  
     */  
    public static byte[] encryptHMAC(byte[] data, String key) throws Exception {   
  
        SecretKey secretKey = new SecretKeySpec(decryptBASE64(key), KEY_MAC);   
        Mac mac = Mac.getInstance(secretKey.getAlgorithm());   
        mac.init(secretKey);   
  
        return mac.doFinal(data);   
  
    }   
    
    /**
     * 编码byte
     * @param ib
     * @return
     */
    public static String byteHEX(byte[] ib) {
        char[] Digit = { '0','1','2','3','4','5','6','7','8','9',
        'A','B','C','D','E','F' };
        StringBuffer strb = new StringBuffer() ;
        for(byte data: ib){
        	char [] ob = new char[2];
	        ob[0] = Digit[(data >>> 4) & 0X0F];
	        ob[1] = Digit[data & 0X0F];
	        strb.append(ob);
        }
        return strb.toString();
    }
    
    public static byte[] hex2BYTE(String hex) throws IllegalArgumentException {    
        if (hex.length() % 2 != 0) {    
            throw new IllegalArgumentException();    
        }    
        char[] arr = hex.toCharArray();    
        byte[] b = new byte[hex.length() / 2];    
        for (int i = 0, j = 0, l = hex.length(); i < l; i++, j++) {    
            String swap = "" + arr[i++] + arr[i];    
            int byteint = Integer.parseInt(swap, 16) & 0xFF;    
            b[j] = new Integer(byteint).byteValue();    
        }    
        return b;    
    }  
} 