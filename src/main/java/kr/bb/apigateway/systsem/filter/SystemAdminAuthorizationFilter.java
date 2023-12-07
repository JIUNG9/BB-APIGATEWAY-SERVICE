package kr.bb.apigateway.systsem.filter;


import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import kr.bb.apigateway.common.security.SecurityContextUtil;
import kr.bb.apigateway.common.util.ExtractAuthorizationTokenUtil;
import kr.bb.apigateway.common.valueobject.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
public class SystemAdminAuthorizationFilter extends OncePerRequestFilter {

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
    String requestURI = request.getRequestURI();
    return !requestURI.contains("/admin") || requestURI.contains("/admin/login");
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    if (!Role.ROLE_SYSTEM_ADMIN.name().equals(ExtractAuthorizationTokenUtil.extractRole(request))) {
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      throw new InsufficientAuthenticationException("시스템 관리자의 권한이 없습니다. 시스템 관리자로 로그인해주세요");
    }
    SecurityContextUtil.setSecurityContextWithUserId(
        ExtractAuthorizationTokenUtil.extractUserId(request));
    doFilter(request, response, filterChain);
  }
}
