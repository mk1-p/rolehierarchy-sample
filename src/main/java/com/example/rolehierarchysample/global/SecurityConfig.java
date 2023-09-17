package com.example.rolehierarchysample.global;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableGlobalMethodSecurity(    // Spring AOP 활용, 검증 어노테이션 사용 여부
        prePostEnabled = true,  // @PreAuthorize
        securedEnabled = true,  // @Secured
        jsr250Enabled = true)   // @RolesAllowed
public class SecurityConfig {

    private final LoginSuccessHandler loginSuccessHandler;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.disable());


        http
                .authorizeHttpRequests(authorize -> authorize       // 권한 지정
                        .requestMatchers(new AntPathRequestMatcher("/test/admin")).hasAuthority(Role.ADMIN.getKey())
                        .requestMatchers(new AntPathRequestMatcher("/test/user")).hasAuthority(Role.COMMON.getKey())
                        .requestMatchers(new AntPathRequestMatcher("/test/guest")).hasAuthority(Role.GUEST.getKey())
                        .requestMatchers(new AntPathRequestMatcher("/test/api")).authenticated()
                );

        http.formLogin(form -> form
                .loginProcessingUrl("/login")
                .successHandler(loginSuccessHandler)
        );


        return http.build();
    }


    /**
     * Role 계층화
     * @return
     */
    @Bean
    static RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl hierarchy = new RoleHierarchyImpl();
        hierarchy.setHierarchy(Role.ADMIN.getKey()+" > "+Role.API.getKey()+"\n" +
                // API 권한은 별도
                Role.ADMIN.getKey()+" > "+Role.VIP.getKey()+"\n" +
                Role.VIP.getKey()+" > "+Role.COMMON.getKey()+"\n" +
                Role.COMMON.getKey()+" > "+Role.GUEST.getKey());
        return hierarchy;
    }


    /**
     * 메모리 유저 세팅
     * @return
     */
    @Bean
    public UserDetailsService users() {

        UserDetails admin = User.builder()
                .username("test-admin")
                .password("{noop}password")
                .roles(Role.ADMIN.getSubKey())
                .build();

        UserDetails user = User.builder()
                .username("test-user")
                .password("{noop}password")
                .roles(Role.COMMON.getSubKey())
                .build();

        UserDetails guest = User.builder()
                .username("test-guest")
                .password("{noop}password")
                .roles(Role.GUEST.getSubKey())
                .build();

        return new InMemoryUserDetailsManager(user, admin, guest);
    }


}
