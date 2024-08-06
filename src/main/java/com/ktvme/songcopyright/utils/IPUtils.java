package com.ktvme.songcopyright.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * <p>Description: IP工具类</p >
 * <p>Copyright: Copyright (c)2024</p >
 * <p>Company: </p >
 * <P>Created Date: 2024年08月05日</P>
 *
 * @author LiuYuHan
 * @version 1.0
 */
@Slf4j
@UtilityClass
public class IPUtils {
    private static final String UNKNOWN = "unknown";
    private static final String X_FORWARDED_FOR = "x-forwarded-for";
    private static final String PROXY_CLIENT_IP = "Proxy-Client-IP";
    private static final String WL_PROXY_CLIENT_IP = "WL-Proxy-Client-IP";
    private static final String LOCAL_HOST_IP = "127.0.0.1";

    public String getIpAddr(HttpServletRequest request) {
        String ipAddress;

        try {
            ipAddress = request.getHeader(X_FORWARDED_FOR);
            if (ipAddress == null || ipAddress.isEmpty() || UNKNOWN.equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader(PROXY_CLIENT_IP);
            }
            if (ipAddress == null || ipAddress.isEmpty() || UNKNOWN.equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader(WL_PROXY_CLIENT_IP);
            }
            if (ipAddress == null || ipAddress.isEmpty() || UNKNOWN.equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getRemoteAddr();
                if (LOCAL_HOST_IP.equals(ipAddress)) {
                    // 根据网卡取本机配置的IP
                    try {
                        ipAddress = InetAddress.getLocalHost().getHostAddress();
                    } catch (UnknownHostException e) {
                        log.error("[ERR] ---> 获取地址异常:", e);
                        return "";
                    }
                }
            }
            // 通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
            if (ipAddress != null) {
                if (ipAddress.contains(",")) {
                    return ipAddress.split(",")[0];
                } else {
                    return ipAddress;
                }
            } else {
                return "";
            }
        } catch (Exception e) {
            log.error("[ERR] ---> 请求异常:", e);
            return "";
        }
    }
}