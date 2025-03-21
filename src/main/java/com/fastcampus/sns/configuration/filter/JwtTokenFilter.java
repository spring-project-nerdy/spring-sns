package com.fastcampus.sns.configuration.filter;

import com.fastcampus.sns.model.User;
import com.fastcampus.sns.repository.UserEntityRepository;
import com.fastcampus.sns.service.UserService;
import com.fastcampus.sns.util.JwtTokenUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

  private final String key;
  private final UserService userService;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    // get header
    final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
    if(header == null || !header.startsWith("Bearer ")) {
      log.error("Error occurs while getting header. header is null or invalid {}", request.getRequestURL());
      filterChain.doFilter(request, response);
      return;
    }

     try {
       final String token = header.split(" ")[1].trim();

       if(JwtTokenUtils.isExpired(token, key)) {
         log.error("key is expired");
         filterChain.doFilter(request, response);
         return;
       }

       String userName = JwtTokenUtils.getUserName(token, key);

       User user = userService.loadUserByUserName(userName);

       UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
           user, null, user.getAuthorities());

       authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
       SecurityContextHolder.getContext().setAuthentication(authenticationToken);

     } catch(RuntimeException e) {
       log.error("Error occurs while validating. {}", e.toString());
       filterChain.doFilter(request, response);
       return;
     }

     filterChain.doFilter(request, response);
  }
}
