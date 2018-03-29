package site.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;


@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    // Настройки доступа
    @Override
    protected void configure(HttpSecurity config) throws Exception {
        config
                .authorizeRequests()
                .antMatchers("./").permitAll()
                .antMatchers("/h2-console/*").permitAll()
                .antMatchers("/h2*").permitAll()
                .antMatchers("/h2-console/login.do?jsessionid=f82738d4e843af89734dab7ca9e24b43").permitAll()
                .antMatchers("/editor").hasRole("EDITOR")
                .and()
                .formLogin().loginPage("/login").defaultSuccessUrl("/editor").permitAll()
                .and()
                .logout().logoutUrl("/logout").permitAll();
    }
    // Тестова реєстрація корситувача (без Passwordencoder )
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder builder) throws Exception {
        builder.inMemoryAuthentication()
                .withUser("user").password("{noop}password").roles("EDITOR");
    }
}