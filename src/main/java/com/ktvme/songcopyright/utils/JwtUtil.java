package com.ktvme.songcopyright.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ktvme.songcopyright.model.entity.UserDO;
import com.ktvme.songcopyright.model.vo.LoginUserVO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;

/**
 * <p>Description: JWT工具类</p >
 * <p>Copyright: Copyright (c)2024</p >
 * <p>Company: </p >
 * <P>Created Date: 2024年08月06日</P>
 *
 * @author LiuYuHan
 * @version 1.0
 */

@Slf4j
@UtilityClass
public class JwtUtil {

    /**
     * 生成 JWT Token
     */
    public static String generateToken(LoginUserVO user, int expire, PrivateKey privateKey) {
        // 计算 token 的过期时间
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + (long) expire * 60 * 1000);

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String jsonString = objectMapper.writeValueAsString(user);
            // 生成 token
            return Jwts.builder()
                    .setSubject(jsonString)
                    .setIssuedAt(now)
                    .setExpiration(expiryDate)
                    .signWith(SignatureAlgorithm.RS256, privateKey) // 使用私钥进行签名
                    .compact();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * 从JWT中解析对象
     */
    public static <T> T getObjectFromToken(String token, PublicKey publicKey, Class<T> clazz) {
        try {
            if (token == null || !token.contains(".")) {
                throw new IllegalArgumentException("Invalid JWT token");
            }

            Claims claims = Jwts.parser()
                    .setSigningKey(publicKey)
                    .parseClaimsJws(token)
                    .getBody();

            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(claims.getSubject(), clazz);
        } catch (Exception e) {
            log.error("Failed to parse JWT token", e);
            return null;
        }
    }

    public static void main(String[] args) throws Exception {
        LoginUserVO user = LoginUserVO.builder()
                .userName("lyh")
                .realName("刘宇涵")
                .email("156981283@qq.com")
                .roles("admin")
                .build();
        String pubKeyPath = "/Users/liuyuhan/Desktop/StarNet/rsa_test/rsa.pub";
        String priKeyPath = "/Users/liuyuhan/Desktop/StarNet/rsa_test/rsa.pri";

        PublicKey publicKey = RSAUtil.getPublicKey(pubKeyPath);
        PrivateKey privateKey = RSAUtil.getPrivateKey(priKeyPath);
        String token = generateToken(user, 360, privateKey);
        System.out.println(token);


        System.out.println(getObjectFromToken(token, publicKey, UserDO.class));
    }
}