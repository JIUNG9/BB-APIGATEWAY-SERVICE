package kr.bb.apigateway.common;


import kr.bb.apigateway.systsem.filter.SystemAdminAuthorizationGatewayFilter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

//@EnableWebFluxSecurity
//@EnableReactiveMethodSecurity
//public class SecurityConfig {

//  @Bean
//  public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
//    http
//        .csrf(csrf -> csrf.disable());
//    return http.build();
//
//  }
//
//  @Order(1)
//  @Qualifier("socialAuthorizationGatewayFilter")
//  @Bean
//  public GlobalFilter socialAuthorizationGatewayFilter() {
//    return new SocialAuthorizationGatewayFilter();
//  }
//
//  @Order(0)
//  @Qualifier("systemAdminAuthorizationGatewayFilter")
//  @Bean
//  public GlobalFilter systemAdminAuthorizationGatewayFilter() {
//    return new SystemAdminAuthorizationGatewayFilter();
//  }
//
//  @Order(2)
//  @Qualifier("storeAuthorizationGatewayFilter")
//  @Bean
//  public GlobalFilter storeAuthorizationGatewayFilter() {
//    return new StoreAuthorizationGatewayFilter();
//  }
//}
