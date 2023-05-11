package com.hziee.shop_address.utils;

import com.hziee.shop_address.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    //由于我也不知道从哪个版本开始的，以前直接继承WebSecurityConfigurerAdapter重写方法变成了现在声明SecurityFilterChain来构建一个长长的过滤链
    private UserService userService;
    @Autowired

    public void setUserService(UserService userService) {
        this.userService = userService;
    }
    @Bean
    public UserDetailsService userDetailsService(){
        return userService;
    }
    @Bean
    SecurityFilterChain web(HttpSecurity http) throws Exception {
        http
                // web放行
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/layui/**", "/signup","/login").permitAll()
                        .anyRequest().permitAll()
                )
                //设置自定义的登录页面
                .formLogin(
                        form->form.loginPage("/login"
                        )
                )
        ;
        return http.build();
    }


    @Bean
    PasswordEncoder passwordEncoder(){
        //密码加密
//        return new BCryptPasswordEncoder();
        return NoOpPasswordEncoder.getInstance();
    }
    @Bean
    DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService());
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }
    @Bean
    public AuthenticationEventPublisher authenticationEventPublisher
            (ApplicationEventPublisher applicationEventPublisher) {
        return new DefaultAuthenticationEventPublisher(applicationEventPublisher);
    }
}