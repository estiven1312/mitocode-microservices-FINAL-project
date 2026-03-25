package pe.com.peruapps.esignaturemicroservice.infrastructure.out.rest.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {
  @Value("${signature.service.url:http://signature-service}")
  private String baseSignatureUrl;

  @Value("${contract.service.url:http://signature-service}")
  private String baseUrl;

  private final RestClient.Builder loadBalancedRestClientBuilder;
  private final RestClient.Builder defaultRestClientBuilder;


  public RestClientConfig(
      @Qualifier("loadBalancedRestClientBuilder")
          RestClient.Builder loadBalancedRestClientBuilder,
      @Qualifier("restClientBuilderDefault") RestClient.Builder defaultRestClientBuilder) {
    this.loadBalancedRestClientBuilder = loadBalancedRestClientBuilder;
    this.defaultRestClientBuilder = defaultRestClientBuilder;
  }

  @Bean
  public RestClient eSignatureRestClient() {
    return defaultRestClientBuilder.clone().baseUrl(baseSignatureUrl).build();
  }

  @Bean
  public RestClient contractRestClient() {
    return loadBalancedRestClientBuilder.clone().baseUrl(baseUrl).build();
  }
}
