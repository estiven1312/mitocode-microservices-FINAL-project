package pe.com.peruapps.contractsmicroservice.infrastructure.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class AccountabilityRestClientConfig {

  @Value("${accountability.service.url:http://accountability-service}")
  private String accountabilityServiceBaseUrl;

  @Bean
  @Primary
  public RestClient.Builder builder() {
    return RestClient.builder();
  }

  @Bean
  @LoadBalanced
  public RestClient.Builder loadBalancedBuilder(JwtPropagationInterceptor jwtPropagationInterceptor) {
    return RestClient.builder().requestInterceptor(jwtPropagationInterceptor);
  }

  @Bean
  public RestClient accountabilityRestClientBuilder(@LoadBalanced RestClient.Builder loadBalancedBuilder) {
    return loadBalancedBuilder.clone().baseUrl(accountabilityServiceBaseUrl).build();
  }

  @Bean
  public AccountabilityHttpExchange accountabilityHttpExchangeClientFactory(
      RestClient accountabilityRestClientBuilder) {
    return HttpServiceProxyFactory.builderFor(
            RestClientAdapter.create(accountabilityRestClientBuilder))
        .build()
        .createClient(AccountabilityHttpExchange.class);
  }
}
