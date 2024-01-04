package com.vashchenko.project.security;

import com.vashchenko.project.enums.Role;
import com.vashchenko.project.models.entities.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import java.io.IOException;
import java.util.ArrayList;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {

    @Bean
    public UserDetailsService userDetailsService() {
//        UserDetails admin = User.builder()
//                .login("user")
//                .password("user")
//                .role(Role.ADMIN)
//                .active(true)
//                .mail("bla-bla-bla")
//                .phoneNumber("+2777123")
//                .build();
//        return new InMemoryUserDetailsManager(admin);
        return new UserDetailsServerImpl();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf().disable()
                .cors().disable()
                .authorizeHttpRequests()
                /*.requestMatchers("/admin/**").hasAuthority(Role.ADMIN.getAuthority())
                .requestMatchers("/manager/**").hasAuthority(Role.MANAGER.getAuthority())
                .requestMatchers("/client/**").hasAuthority(Role.CLIENT.getAuthority())*/
                .anyRequest().permitAll()
                .and().logout().logoutUrl("/logout")
                .and().formLogin().successHandler(new CustomAuthenticationHandler()).loginPage("/login").permitAll()
                .and().logout()
                .logoutSuccessUrl("/").permitAll().and()
                .build();
    }

    private class CustomAuthenticationHandler extends SavedRequestAwareAuthenticationSuccessHandler {

        @Override
        public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                            Authentication authentication) throws IOException, ServletException {
            response.sendRedirect("acc/"+authentication.getName());

        }
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }
}
