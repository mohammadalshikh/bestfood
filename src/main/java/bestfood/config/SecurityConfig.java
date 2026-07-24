package bestfood.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration @EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
            .csrf().disable()
            .formLogin().disable()
            .httpBasic().disable()

            .authorizeRequests()

            .antMatchers(
                "/",
                "/login",
                "/logout",
                "/register",
                "/shop",
                "/contact",
                "/search",
                "/users/check-username",
                "/users/check-email",
                "/css/**",
                "/js/**",
                "/images/**")
            .permitAll()

            .antMatchers("/admin/**").authenticated()

            .anyRequest().permitAll();
    }
}