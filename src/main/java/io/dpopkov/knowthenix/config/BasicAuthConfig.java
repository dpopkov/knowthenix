package io.dpopkov.knowthenix.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import static io.dpopkov.knowthenix.config.AppConstants.BASIC_AUTH_URL;

@Configuration
@EnableWebSecurity
public class BasicAuthConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("james")
                .password("{noop}secret12")
                .authorities("ROLE_ADMIN");
    }

    /*
    To test Basic Authentication use curl:
    1. Get base64 encoded string "james:secret12": echo -n james:secret12 | base64
    2. Run GET request:
        curl -H "Authorization: Basic amFtZXM6c2VjcmV0MTI=" http://localhost:8080/api/basicAuth/validate
     */

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers(BASIC_AUTH_URL + "/**").hasRole("ADMIN")
                .and()
                .httpBasic();
    }
}
