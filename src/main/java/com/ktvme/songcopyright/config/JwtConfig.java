package com.ktvme.songcopyright.config;

import com.ktvme.songcopyright.utils.RSAUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.File;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * <p>Description: JWT配置</p >
 * <p>Copyright: Copyright (c)2024</p >
 * <p>Company: </p >
 * <P>Created Date: 2024年08月06日</P>
 *
 * @author LiuYuHan
 * @version 1.0
 */
@Slf4j
@Configuration
@Data
@ConfigurationProperties(prefix = "jwt")
public class JwtConfig {
    private String secret;
    private String pubKeyPath;
    private String priKeyPath;
    private Integer expire;
    private PublicKey publicKey;
    private PrivateKey privateKey;

    @PostConstruct
    public void init() {
        try {
            File pubFile = new File(pubKeyPath);
            File priFile = new File(priKeyPath);
            if (!pubFile.exists() || !priFile.exists()) {
                RSAUtil.generateKey(pubKeyPath, priKeyPath, secret);
            }
            this.publicKey = RSAUtil.getPublicKey(pubKeyPath);
            this.privateKey = RSAUtil.getPrivateKey(priKeyPath);
        } catch (Exception e) {
            log.error("Failed to initialize JWT properties", e);
        }
    }
}