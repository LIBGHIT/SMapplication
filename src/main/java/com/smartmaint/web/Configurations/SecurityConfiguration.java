package com.smartmaint.web.Configurations;

import com.smartmaint.web.Models.Role;
import com.smartmaint.web.security.AccessTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final AccessTokenFilter accessTokenFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeHttpRequests().requestMatchers("/","","/home",
                        //login_register_auth
                        "/login",
                        "/register",
                        "/validation",
                        "/regenaratetoken",
                        "/newToken",
                        "/registerUser",
                        "/registrationConfirm",
                        "/authenticate",
                        "/logout",
                        "/forgotPass",
                        "/newPassword",
                        "/changePass",
                        "/ChangePassword",
                        //public_functionalities
                        "/assets/**",
                        //messages
                        "/sendMessage",
                        "/contact",
                        "/forgotPasswordRecovery",
                        //HEADER_NORMAL_USER
                        "/about",
                        "/services",
                        "/blogs",
                        "/detailblog",
                        "/testform",
                        "/comingsoon").permitAll()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                .requestMatchers(
                        //blogs
                        "/blogadd",
                        "/blogedit",
                        "/blogmanage",
                        "/blogposts",
                        "/community",
                        "/coursesdashboard",
                        "/coursesdetails",
                        "/coursestopsell",
                        "/dashboard",
                        "/users",
                        "/messages/**",
                        "/analytics",
                        "/files/**",
                        "/maintenance").hasRole("ADMIN")
                .requestMatchers("/logout").authenticated()
                .anyRequest().authenticated()
                .and()
                .formLogin().loginPage("/login")
                .and()
                .sessionManagement()
                .and()
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(accessTokenFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
