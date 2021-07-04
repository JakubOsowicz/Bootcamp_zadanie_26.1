package pl.osowicz.task_manager.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                    .antMatchers("/").permitAll()
                    .antMatchers("/register").permitAll()
                    .antMatchers("/style/**").permitAll()
                    .antMatchers("/scripts/**").permitAll()
                    .antMatchers("/error/**").permitAll()
                    .antMatchers("/forgotPassword").permitAll()
                    .antMatchers("/mailSendSuccess").permitAll()
                    .antMatchers("/resetPassword").permitAll()
                    .antMatchers("/task/add").hasAnyRole("ADMIN", "SUPPORT", "USER")
                    .antMatchers("/task/**").hasAnyRole("ADMIN", "SUPPORT")
                    .antMatchers("/user/**").hasRole("ADMIN")
                    .antMatchers("/admin/**").hasRole("ADMIN")
                    .anyRequest().authenticated()
                .and()
                    .formLogin()
                        .usernameParameter("email")
                        .loginPage("/login").permitAll()
                .and()
                    .logout()
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .permitAll();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers("/h2-console/**");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
