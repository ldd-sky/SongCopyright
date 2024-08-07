package com.ktvme.songcopyright.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ktvme.songcopyright.config.FilterConfig;
import com.ktvme.songcopyright.config.JwtConfig;
import com.ktvme.songcopyright.model.entity.UserDO;
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
            // 1. 获取请求路径
            String path = request.getRequestURI();
            System.out.println("拦截路径：" + path);

            // 2. 白名单校验
            List<String> allowPaths = filterConfig.getAllowPaths();
            for (String allowPath : allowPaths) {
                if (path.contains(allowPath)) {
                    System.out.println("放行路径：" + path);
                    filterChain.doFilter(request, response);
                    return;
                }
            }

            // 3. 获取 token
            String token = request.getHeader("X-Token");

            // 4. 校验 token
            JwtUtil.getObjectFromToken(token, jwtConfig.getPublicKey(), LoginUserVO.class);

            // 5. 校验成功，放行
            filterChain.doFilter(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            // 5.2 失败，返回提示`token失效`
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());

            // 响应“没有权限”提示
            response.getWriter().write(new ObjectMapper().writeValueAsString("没有权限"));
        }
    }
}