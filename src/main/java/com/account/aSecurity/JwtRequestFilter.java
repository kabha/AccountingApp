package com.account.aSecurity;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

@Service
public class JwtRequestFilter extends OncePerRequestFilter {

	@Autowired
	private JwtUtils jwtUtils;
	@Autowired
	private MyUserDetailsService userDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
		try {
			String token = null;
			String userName = null;
			final String BearerPrefix = "Bearer ";
			final String authHeader = request.getHeader("Authorization");
			if (authHeader != null && authHeader.startsWith(BearerPrefix)) {
				token = authHeader.substring(7);
				if(token.startsWith(BearerPrefix)) {
					token = token.replaceFirst(BearerPrefix, "");
				}
				userName = jwtUtils.extractUsername(token);
			}
			if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
				if (jwtUtils.validateToken(token, userDetails)) {
					UsernamePasswordAuthenticationToken usrPwdAuthToken = new UsernamePasswordAuthenticationToken(
							userDetails, null, userDetails.getAuthorities());
					usrPwdAuthToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(usrPwdAuthToken);
				}
			}
			if(authHeader == null && !request.getRequestURI().endsWith("login") 
					&& !request.getRequestURI().endsWith("register") && !request.getRequestURI().endsWith("/save-user")) {
				throw new Exception("ERROR: Authorization-Header not fould");
			}else {
				filterChain.doFilter(request, response);
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
			response.setContentType("application/json");
			JSONObject jsonResponse = new JSONObject();
			jsonResponse.put("status", 0);
			jsonResponse.put("HttpStatus", HttpStatus.FORBIDDEN);
			try {
				response.getWriter().println(jsonResponse);
			}catch(IOException ex2) {
				ex2.printStackTrace();
			}
		}
	}

}
