package kr.bb.apigateway.social.filter;


import kr.bb.apigateway.common.util.ExtractAuthorizationTokenUtil;
import kr.bb.apigateway.common.valueobject.Role;
import kr.bb.apigateway.social.exception.SocialAuthException;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class SocialAuthorizationGatewayFilterFactory extends
    AbstractGatewayFilterFactory<SocialAuthorizationGatewayFilterFactory.Config> {

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            if (!isAuthorizedUser(exchange)) {
                return handleUnauthenticatedUser(exchange);
            }
            return chain.filter(exchange);
        };
    }

    private boolean isAuthorizedUser(ServerWebExchange exchange) {
        String role = ExtractAuthorizationTokenUtil.extractRole(exchange.getRequest());
        return Role.ROLE_SOCIAL_USER.name().equals(role);
    }

    private Mono<Void> handleUnauthenticatedUser(ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        throw new SocialAuthException("소셜 유저가 아닙니다.");
    }

    public static class Config {

        private boolean shouldFilter;

        public boolean getShouldNotFilter() {
            return shouldFilter;
        }

        public void setShouldNotFilter(boolean shouldFilter) {
            this.shouldFilter = shouldFilter;
        }
    }

}
