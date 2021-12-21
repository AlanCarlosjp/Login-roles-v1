package com.api.login.config;

import com.api.login.service.UsuarioService;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final BCryptPasswordEncoder encoder;
    private final UsuarioService service;

    public WebSecurityConfig(BCryptPasswordEncoder encoder, UsuarioService service) {
        this.encoder = encoder;
        this.service = service;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(service).passwordEncoder(encoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
      http.csrf().disable()
              .authorizeRequests().antMatchers("/user/create")
              .permitAll()
              .antMatchers("/login")
              .permitAll()
              .antMatchers("/user/name/**","/user/**")
              .hasRole("ADM").and().formLogin();
      //  http.csrf().disable().authorizeRequests().antMatchers("/**").permitAll();
    }
}
