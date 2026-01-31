package com.courierwala.server.jwtutils;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.courierwala.server.security.CustomUserDetailsService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
//
//@Component
//@RequiredArgsConstructor
//public class JwtFilter extends OncePerRequestFilter {
//
//	private final JwtUtil jwtUtil;
//	private final CustomUserDetailsService userDetailsService;
//
//	@Override
//	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
//			throws ServletException, IOException {
//
//		try {
//
//			//  1. Skip JWT filter for auth endpoints
//			String path = request.getServletPath();
//			if (path.startsWith("/api/auth")) {
//				chain.doFilter(request, response);
//				return;
//			}
//
//			String token = getTokenFromCookie(request);
//
//			if (token != null) {
//
//				String username = jwtUtil.extractUsername(token);
//
//				if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//
//					UserDetails userDetails = userDetailsService.loadUserByUsername(username);
//
//					if (jwtUtil.isTokenValid(token, userDetails)) {
//
//						UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
//								userDetails, null, userDetails.getAuthorities());
//
//						SecurityContextHolder.getContext().setAuthentication(authToken);
//					}
//				}
//			}
//
//			chain.doFilter(request, response);
//
//		} catch (io.jsonwebtoken.ExpiredJwtException e) {
//
//			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//			response.setContentType("application/json");
//			response.getWriter().write("{\"error\": \"Token expired\"}");
//
//		} catch (io.jsonwebtoken.JwtException e) {
//
//			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//			response.setContentType("application/json");
//			response.getWriter().write("{\"error\": \"Invalid token\"}");
//		}
//
//	}
//
//	private String getTokenFromCookie(HttpServletRequest request) {
//		if (request.getCookies() != null) {
//			for (Cookie cookie : request.getCookies()) {
//				if ("JWT_TOKEN".equals(cookie.getName())) {
//					return cookie.getValue();
//				}
//			}
//		}
//		return null;
//	}
//
//}


@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain
    ) throws ServletException, IOException {

        // ✅ 1. Skip OPTIONS (CORS preflight)
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            chain.doFilter(request, response);
            return;
        }

        // ✅ 2. Skip auth endpoints
        String path = request.getServletPath();
        if (path.startsWith("/api/auth")) {
            chain.doFilter(request, response);
            return;
        }

        try {
            String token = getTokenFromCookie(request);

            if (token != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                String username = jwtUtil.extractUsername(token);

                if (username != null) {

                    UserDetails userDetails =
                            userDetailsService.loadUserByUsername(username);

                    if (jwtUtil.isTokenValid(token, userDetails)) {

                        UsernamePasswordAuthenticationToken authToken =
                                new UsernamePasswordAuthenticationToken(
                                        userDetails,
                                        null,
                                        userDetails.getAuthorities()
                                );

                        authToken.setDetails(
                                new WebAuthenticationDetailsSource()
                                        .buildDetails(request)
                        );

                        SecurityContextHolder.getContext()
                                .setAuthentication(authToken);
                    }
                }
            }

        } catch (Exception e) {
            // ❌ DO NOT write response
            // ❌ DO NOT redirect
            // Let Spring Security handle it
            SecurityContextHolder.clearContext();
        }

        chain.doFilter(request, response);
    }

    private String getTokenFromCookie(HttpServletRequest request) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("JWT_TOKEN".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}

