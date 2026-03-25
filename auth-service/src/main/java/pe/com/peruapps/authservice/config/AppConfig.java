package pe.com.peruapps.authservice.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({AuthJwtProperties.class, AdminBootstrapProperties.class})
public class AppConfig {
}
