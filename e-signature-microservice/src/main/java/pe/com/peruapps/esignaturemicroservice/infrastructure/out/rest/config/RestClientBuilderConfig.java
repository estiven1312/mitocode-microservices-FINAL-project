package pe.com.peruapps.esignaturemicroservice.infrastructure.out.rest.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientBuilderConfig {

  @Primary
  @Bean
  public RestClient.Builder restClientBuilderDefault() {
    return RestClient.builder();
  }

  @Bean("loadBalancedRestClientBuilder")
  @LoadBalanced
  public RestClient.Builder restClientBuilderLoadBalanced(
      JwtPropagationInterceptor jwtPropagationInterceptor) {
    return RestClient.builder().requestInterceptor(jwtPropagationInterceptor);
  }
}
