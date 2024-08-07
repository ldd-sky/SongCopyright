package com.ktvme.songcopyright.utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * <p>Description: RSA加密解密</p >
 * <p>Copyright: Copyright (c)2024</p >
 * <p>Company: </p >
 * <P>Created Date: 2024年08月06日</P>
 *
 * @author LiuYuHan
 * @version 1.0
 */
public class RSAUtil {

    private static final String RSA_ALGORITHM = "RSA";

    /**
     * 生成RSA公钥和私钥，并写入指定文件
     *
     * @param pubKeyPath  公钥路径
     * @param priKeyPath  私钥路径
     * @param secret      用于生成密钥的密码
     * @throws Exception  如果发生异常
     */
    public static void generateKey(String pubKeyPath, String priKeyPath, String secret) throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(RSA_ALGORITHM);
        keyPairGenerator.initialize(2048); // 设置密钥长度为2048位
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        // 获取公钥和私钥
        byte[] pubKeyBytes = keyPair.getPublic().getEncoded();
        byte[] priKeyBytes = keyPair.getPrivate().getEncoded();

        // 将密钥写入文件
        writeFile(pubKeyPath, pubKeyBytes);
        writeFile(priKeyPath, priKeyBytes);
    }

    /**
     * 从指定路径读取公钥
     *
     * @param filePath 公钥文件路径
     * @return 公钥对象
     * @throws Exception 如果发生异常
     */
    public static PublicKey getPublicKey(String filePath) throws Exception {
        byte[] keyBytes = readFile(filePath);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
        return keyFactory.generatePublic(keySpec);
    }

    /**
     * 从指定路径读取私钥
     *
     * @param filePath 私钥文件路径
     * @return 私钥对象
     * @throws Exception 如果发生异常
     */
    public static PrivateKey getPrivateKey(String filePath) throws Exception {
        byte[] keyBytes = readFile(filePath);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
        return keyFactory.generatePrivate(keySpec);
    }

    /**
     * 将字节数组写入文件
     *
     * @param filePath 文件路径
     * @param content  内容字节数组
     * @throws IOException 如果发生IO异常
     */
    private static void writeFile(String filePath, byte[] content) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(content);
        }
    }

    /**
     * 从文件中读取字节数组
     *
     * @param filePath 文件路径
     * @return 文件内容字节数组
     * @throws IOException 如果发生IO异常
     */
    private static byte[] readFile(String filePath) throws IOException {
        return Files.readAllBytes(Paths.get(filePath));
    }
}