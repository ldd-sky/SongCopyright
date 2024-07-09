package com.ktvme.songcopyright.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * <p>Description: 爬虫工具类</p >
 * <p>Copyright: Copyright (c)2024</p >
 * <p>Company: </p >
 * <P>Created Date: 2024年07月04日</P>
 *
 * @author LiuYuHan
 * @version 1.0
 */
public class SpiderUtils {

    private static final String e = "010001";
    private static final String f = "00e0b509f6259df8642dbc35662901477df22677ec152b5ff68ace615bb7b725152b3ab17a876aea8a5aa76d2e417629ec4ee341f56135fccf695280104e0312ecbda92557c93870114af6c9d05c4f7f0c3685b7a46bee255932575cce10b424d813cfe4875d3e82047b97ddef52741d546b8e289dc6935b3ece0462db0a22b8e7";
    private static final String g = "0CoJUm6Qyw8W8jud";

    /**
     * 获取随机字符串
     */
    private static String generateStr(int length){
        String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder result = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            result.append(characters.charAt(index));
        }

        return result.toString();
    }

    /**
     * AES加密获得params
     */
    private static String AESEncryption(String text, String key) throws Exception {
        String iv = "0102030405060708"; // iv偏移量
        byte[] ivBytes = iv.getBytes(StandardCharsets.UTF_8);
        byte[] textBytes = text.getBytes(StandardCharsets.UTF_8);
        int pad = 16 - textBytes.length % 16;
        byte[] paddedBytes = new byte[textBytes.length + pad];
        System.arraycopy(textBytes, 0, paddedBytes, 0, textBytes.length);
        for (int i = textBytes.length; i < paddedBytes.length; i++) {
            paddedBytes[i] = (byte) pad;
        }
        byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);

        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(ivBytes);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);

        byte[] encryptedBytes = cipher.doFinal(paddedBytes);
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    /**
     * RSA加密获得encSec key
     */
    private static String RSAEncryption(String str, String key, String f){
        StringBuilder reversedStr = new StringBuilder(str).reverse(); // 随机字符串逆序排列
        byte[] strBytes = reversedStr.toString().getBytes(StandardCharsets.UTF_8); // 将随机字符串转换为byte数组
        BigInteger strInt = new BigInteger(1, strBytes); // 将byte数组转换为BigInteger

        BigInteger keyInt = new BigInteger(key, 16); // 将密钥转换为BigInteger
        BigInteger fInt = new BigInteger(f, 16); // 将f值转换为BigInteger

        BigInteger encryptedInt = strInt.modPow(keyInt, fInt); // RSA加密

        String encryptedStr = encryptedInt.toString(16); // 将加密后的BigInteger转换为十六进制字符串
        encryptedStr = String.format("%256s", encryptedStr).replace(' ', 'x'); // 补齐字符串长度为256

        return encryptedStr;
    }

    private static Map<String, String> getParams(String d) throws Exception {
//        String i = generateStr(16); // 生成一个16位的随机字符串
        String i = "aO6mqZksdJbqUygP";
        String encText = AESEncryption(d, SpiderUtils.g); // 第一次AES加密
        System.out.println(encText);
        String params = AESEncryption(encText, i); // 第二次AES加密
        String encSecKey = RSAEncryption(i, SpiderUtils.e, SpiderUtils.f); // RSA加密
        Map<String, String> map = new HashMap<>();
        map.put("params", params);
        map.put("encSecKey", encSecKey);
        return map;
    }

    public static String getData(String msg, String url) throws Exception {
        // 调用get_params方法获取参数
        Map<String, String> params = getParams(msg);

        // 发送POST请求
        URL requestUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) requestUrl.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);

        // 设置请求参数
        StringBuilder requestData = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (requestData.length() > 0) {
                requestData.append("&");
            }
            requestData.append(entry.getKey()).append("=").append(entry.getValue());
        }

        try (OutputStream outputStream = connection.getOutputStream()) {
            byte[] requestDataBytes = requestData.toString().getBytes("UTF-8");
            outputStream.write(requestDataBytes);
        }

        // 读取响应
        StringBuilder responseData = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                responseData.append(line);
            }
        }

        connection.disconnect();

        return responseData.toString();
    }

    public static void main(String[] args) throws Exception {
        String search_msg = "{\"hlpretag\":\"<span class=\\\"s-fc7\\\">\",\"hlposttag\":\"</span>\",\"s\":\"自由の翅\",\"type\":\"1\",\"offset\":\"0\",\"total\":\"true\",\"limit\":\"30\",\"csrf_token\":\"\"}";
        String search_url = "https://music.163.com/weapi/cloudsearch/get/web?csrf_token=";

        System.out.println(getData(search_msg, search_url));
    }
}