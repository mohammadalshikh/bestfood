package bestfood.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
            .csrf().disable()
            .authorizeRequests()

            // non-auth
            .antMatchers(
                "/login",
                "/admin/login",
                "/register",
                "/users/check-username",
                "/users/check-email",
                "/css/**",
                "/js/**",
                "/images/**")
            .permitAll()

            // admins only
            .antMatchers("/admin/**")
            .hasRole("ADMIN")

            // admins + users
            .anyRequest()
            .authenticated()

            // admin auth failure redirect
            .and()
            .exceptionHandling()
            .defaultAuthenticationEntryPointFor(
                new LoginUrlAuthenticationEntryPoint("/admin/login"),
                new AntPathRequestMatcher("/admin/**")
            )
            .defaultAuthenticationEntryPointFor(
                new LoginUrlAuthenticationEntryPoint("/login"),
                new AntPathRequestMatcher("/**")
            )
            .accessDeniedPage("/admin/login")

            // login
            .and()
            .formLogin().disable()

            // logout
            .logout()
            .logoutSuccessUrl("/login")
            .permitAll();
    }

}