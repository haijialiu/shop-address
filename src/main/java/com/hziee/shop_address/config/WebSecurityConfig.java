package com.hziee.shop_address.config;

import com.hziee.shop_address.service.impl.UserService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    //由于我也不知道从哪个版本开始的，以前直接继承WebSecurityConfigurerAdapter重写方法变成了现在声明SecurityFilterChain来构建一个长长的过滤链
    private UserService userService;
    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
    @Resource
    private DataSource dataSource;
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
                        .anyRequest().authenticated()
                )
                //设置自定义的登录页面
                .formLogin(
                        form->form.loginPage("/login"
                        )
                )
                .rememberMe(
                        (remember)->{
                            remember.userDetailsService(userService);
                            remember.tokenRepository(persistentTokenRepository());
                            remember.tokenValiditySeconds(60 * 60 / 2);
                        }
                )
                //跨域
                .csrf(
                        AbstractHttpConfigurer::disable
                )
        ;
        return http.build();
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository(){
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        //数据源设置
        tokenRepository.setDataSource(dataSource);
        return tokenRepository;
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