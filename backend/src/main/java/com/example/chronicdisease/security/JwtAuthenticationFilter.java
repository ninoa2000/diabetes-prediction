package com.example.chronicdisease.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Collections;

@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider tokenProvider;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();
    // 不需要带/api前缀，因为Spring Security已经处理了context-path
    private final List<String> excludePaths = Arrays.asList(
        "/auth/**", 
        "/auth/login", 
        "/auth/register",
        "/test/public"
    );

    public JwtAuthenticationFilter(JwtTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        String contextPath = request.getContextPath();
        
        // 如果有context path，需要去除
        if (StringUtils.hasText(contextPath) && path.startsWith(contextPath)) {
            path = path.substring(contextPath.length());
        }
        
        // 创建final变量用于lambda表达式
        final String finalPath = path;
        
        log.debug("当前请求路径: {}, 处理后路径: {}", request.getRequestURI(), finalPath);
        
        boolean shouldExclude = excludePaths.stream()
            .anyMatch(p -> pathMatcher.match(p, finalPath));
            
        log.debug("该路径{}被过滤器处理", shouldExclude ? "不会" : "将会");
        return shouldExclude;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            // 从请求头获取用户ID
            String userId = request.getHeader("X-User-ID");
            if (userId != null && !userId.isEmpty()) {
                // 如果有用户ID，创建一个认证对象
                Authentication auth = new UsernamePasswordAuthenticationToken(userId, null, Collections.emptyList());
                SecurityContextHolder.getContext().setAuthentication(auth);
                log.debug("已设置用户认证信息到安全上下文: {}", userId);
            } else {
                // 如果没有用户ID，设置匿名认证
                Authentication auth = new UsernamePasswordAuthenticationToken("anonymous", null, Collections.emptyList());
                SecurityContextHolder.getContext().setAuthentication(auth);
                log.debug("已设置默认认证信息到安全上下文");
            }
        } catch (Exception ex) {
            log.error("设置用户认证时发生错误: {}", ex.getMessage());
            // 在发生错误时设置匿名认证
            Authentication auth = new UsernamePasswordAuthenticationToken("anonymous", null, Collections.emptyList());
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
} 