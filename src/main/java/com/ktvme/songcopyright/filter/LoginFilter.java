package com.ktvme.songcopyright.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ktvme.songcopyright.config.FilterConfig;
import com.ktvme.songcopyright.config.JwtConfig;
import com.ktvme.songcopyright.model.vo.LoginUserVO;
import com.ktvme.songcopyright.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * <p>Description: 登陆过滤器</p >
 * <p>Copyright: Copyright (c)2024</p >
 * <p>Company: </p >
 * <P>Created Date: 2024年08月06日</P>
 *
 * @author LiuYuHan
 * @version 1.0
 */
@Component
public class LoginFilter extends OncePerRequestFilter {
    @Autowired
    private FilterConfig filterConfig;
    @Autowired
    private JwtConfig jwtConfig;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException {
        try {
            String path = request.getRequestURI();
            System.out.println("拦截路径：" + path);

            List<String> allowPaths = filterConfig.getAllowPaths();
            for (String allowPath : allowPaths) {
                if (path.contains(allowPath)) {
                    System.out.println("放行路径：" + path);
                    filterChain.doFilter(request, response);
                    return;
                }
            }

            String token = request.getHeader("X-Token");

            // 解析并验证 token
            LoginUserVO user = JwtUtil.getObjectFromToken(token, jwtConfig.getPublicKey(), LoginUserVO.class);

            if (user == null || !user.getRoles().contains("admin")) {
                // Token 无效或解析失败，返回 401
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.setCharacterEncoding(StandardCharsets.UTF_8.name());
                response.getWriter().write(new ObjectMapper().writeValueAsString("没有权限"));
                return;
            }
            System.out.println("拥有权限，放行：" + path);
            // Token 验证成功，放行请求
            filterChain.doFilter(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.getWriter().write(new ObjectMapper().writeValueAsString("没有权限"));
        }
    }
}