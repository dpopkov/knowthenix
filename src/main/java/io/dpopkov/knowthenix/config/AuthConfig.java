package io.dpopkov.knowthenix.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import static io.dpopkov.knowthenix.config.AppConstants.*;

@Configuration
@EnableWebSecurity
public class AuthConfig extends WebSecurityConfigurerAdapter {

    private static final String ADMIN = "ADMIN";
    private static final String EDITOR = "EDITOR";
    private static final String USER = "USER";
    private static final String API_ANY = API + "/**";
    private static final String QUESTIONS_CREATED_ON_ANY = QUESTIONS_URL + "/created/**";

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("james")
                .password("{noop}secret12")
                .authorities(roleOf(ADMIN))
            .and()
                .withUser("jack")
                .password("{noop}secret56")
                .authorities(roleOf(EDITOR))
            .and()
                .withUser("alice")
                .password("{noop}secret34")
                .authorities(roleOf(USER));
    }

    private String roleOf(String roleName) {
        return "ROLE_" + roleName;
    }

    /*
    To test Basic Authentication use curl:
    1. Get base64 encoded string "james:secret12": echo -n james:secret12 | base64
    2. Run GET request:
        curl -H "Authorization: Basic amFtZXM6c2VjcmV0MTI=" http://localhost:8080/api/basicAuth/validate
     */

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        /* Configure Basic Authentication */
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS, BASIC_AUTH_URL + "/**").permitAll()
                .antMatchers(BASIC_AUTH_URL + "/**").hasAnyRole(ADMIN, EDITOR, USER)
                .and()
                .httpBasic();

        /* Configure JWT Authentication */
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS, API_ANY).permitAll()
                .antMatchers(HttpMethod.GET, QUESTIONS_CREATED_ON_ANY).permitAll()
                .antMatchers(HttpMethod.GET, API_ANY).hasAnyRole(ADMIN, EDITOR, USER)
                .antMatchers(HttpMethod.POST, API_ANY).hasRole(EDITOR)
                .antMatchers(HttpMethod.PUT, API_ANY).hasRole(EDITOR)
                .antMatchers(HttpMethod.PATCH, API_ANY).hasRole(EDITOR)
                .antMatchers(API_ANY).hasRole(ADMIN)
                .and()
                .addFilter(new JwtAuthorizationFilter(authenticationManager()));

        // For h2 console
        http.headers().frameOptions().disable();
    }
}
