package kr.bb.apigateway.social.filter;


import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import kr.bb.apigateway.common.security.SecurityContextUtil;
import kr.bb.apigateway.common.util.ExtractAuthorizationTokenUtil;
import kr.bb.apigateway.common.valueobject.Role;
import kr.bb.apigateway.social.exception.SocialAuthException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
public class SocialAuthorizationFilter extends OncePerRequestFilter {

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
    String requestURI = request.getRequestURI();
    return !requestURI.contains("/social") || requestURI.contains("/social/login");
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    if (!Role.ROLE_SOCIAL_USER.name().equals(ExtractAuthorizationTokenUtil.extractRole(request))) {
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      throw new SocialAuthException("유저의 권한이 없습니다.");
    }
    SecurityContextUtil.setSecurityContextWithUserId(
        ExtractAuthorizationTokenUtil.extractUserId(request));
    doFilter(request, response, filterChain);
  }


}
