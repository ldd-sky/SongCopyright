package com.ktvme.songcopyright.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * <p>Description: 过滤器配置</p >
 * <p>Copyright: Copyright (c)2024</p >
 * <p>Company: </p >
 * <P>Created Date: 2024年08月06日</P>
 *
 * @author LiuYuHan
 * @version 1.0
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "filter")
public class FilterConfig {

    /**
     * 允许访问的路径（白名单）
     */
    private List<String> allowPaths;
}