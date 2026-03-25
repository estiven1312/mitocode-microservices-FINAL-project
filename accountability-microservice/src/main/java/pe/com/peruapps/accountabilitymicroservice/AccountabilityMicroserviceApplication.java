package pe.com.peruapps.accountabilitymicroservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@SpringBootApplication
@EnableMethodSecurity
public class AccountabilityMicroserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AccountabilityMicroserviceApplication.class, args);
    }

}
