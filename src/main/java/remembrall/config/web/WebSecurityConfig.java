package remembrall.config.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.session.data.redis.RedisOperationsSessionRepository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import remembrall.service.UserDetailsManager;

import javax.sql.DataSource;

@EnableTransactionManagement
@EnableWebSecurity
public class WebSecurityConfig {

    @ConfigurationProperties(prefix = "spring.datasource")
    @Bean
    @Primary
    public DataSource dataSource() {
        return DataSourceBuilder
                .create()
                .build();
    }

    @Bean
    public BCryptPasswordEncoder bcrypt() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JdbcUserDetailsManager userDetailsManager() {
        JdbcUserDetailsManager manager = new UserDetailsManager(dataSource());
        manager.setUsersByUsernameQuery("select username,password,enabled from users where username=?");
        manager.setAuthoritiesByUsernameQuery("select username,authority from authorities where username = ?");
        return manager;
    }

    @Autowired
    public void initialize(AuthenticationManagerBuilder builder) throws Exception {
        builder.userDetailsService(userDetailsManager()).passwordEncoder(bcrypt());
    }

    @Bean
    @Autowired
    public RedisOperationsSessionRepository sessionRepository(RedisTemplate<Object, Object> template) {
        return new RedisOperationsSessionRepository(template);
    }
}
