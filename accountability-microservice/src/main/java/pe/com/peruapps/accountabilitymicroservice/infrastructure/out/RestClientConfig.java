package pe.com.peruapps.accountabilitymicroservice.infrastructure.out;

import io.micrometer.observation.ObservationRegistry;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

  @Bean
  RestClient.Builder jwtPropagationRestClientCustomizer(
      ObservationRegistry observationRegistry,
      ObjectProvider<JwtPropagationInterceptor> jwtInterceptorProvider) {
    RestClient.Builder builder = RestClient.builder().observationRegistry(observationRegistry);

    jwtInterceptorProvider.ifAvailable(builder::requestInterceptor);

    return builder;
  }
}
