package com.fgsqw.web.config;

import cn.hutool.core.util.ObjectUtil;
import com.fgsqw.beans.user.LoginUser;
import com.fgsqw.beans.user.MvUser;
import com.fgsqw.iservice.user.IMvUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    IMvUserService userService;
    @Autowired
    RestAuthorizationEntryPoint restAuthorizationEntryPoint;
    @Autowired
    RestfulAccessDeniedHandler restfulAccessDeniedHandler;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 密码匹配使用 BCryptPasswordEncoder()
        auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
    }

    // 放行路径,不走拦截链
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(
                "/common/captcha",
                "/common/captcha2",
                "/user/login",
                "/user/logout",
                "index.html",
                "favicon.ico",
                "/doc.html",
                "/swagger-resources/**",
                "/v2/api-docs/**",
                "/css/**",
                "/js/**",
                "/webjars/**"
        );
    }

    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        // 重写 UserDetailsService下的 loadUserByUsername 使其从数据库中查询用户数据进行比对
        return username->{
            MvUser mvUser = userService.getMvUserByUserName(username);
            if(ObjectUtil.isEmpty(mvUser)){
                return null;
            }
            return new LoginUser(mvUser);
        };
    }

    //HttpSecurity配置
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // 关闭 scrf 使用jwt
                .csrf().disable()
                // 不使用session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .anyRequest()
                .authenticated()
                .and().headers().cacheControl();
        // 添加jwt 登录授权过滤器
        http.addFilterBefore(jwtAuthenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        // 自定义未授权,未登录接口返回
        http.exceptionHandling()
                .accessDeniedHandler(restfulAccessDeniedHandler)
                .authenticationEntryPoint(restAuthorizationEntryPoint);
    }

    // JWt登录授权过滤器
    @Bean
    public JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter(){
        return new JwtAuthenticationTokenFilter();
    }
}
