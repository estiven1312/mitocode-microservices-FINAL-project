package pe.com.peruapps.esignaturemicroservice.infrastructure.out.rest.client.contract;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import pe.com.peruapps.esignaturemicroservice.application.out.ContractProviderPort;
import pe.com.peruapps.esignaturemicroservice.domain.model.Contract;
import pe.com.peruapps.esignaturemicroservice.domain.model.Id;

import java.util.UUID;

@Service
public class ContractProviderClient implements ContractProviderPort {

  private final RestClient contractRestClient;

  public ContractProviderClient(@Qualifier("contractRestClient") RestClient contractRestClient) {
    this.contractRestClient = contractRestClient;
  }

  public ContractResponse getContractById(Id contractId) {
    return contractRestClient
        .get()
        .uri(uri -> uri.path("api/v1/contracts/{contractId}").build(contractId.value().toString()))
        .retrieve()
        .body(ContractResponse.class);
  }

  @Override
  @CircuitBreaker(name = "contractsServiceCircuitBreaker", fallbackMethod = "findByIdFallback")
  @Retry(name = "contractsServiceRetry", fallbackMethod = "retryFindByIdFallback")
  public Contract findById(Id contractId) {
    ContractResponse response = getContractById(contractId);
    return new Contract(
        toId(response.contractId()),
        response.name(),
        response.description(),
        Contract.Type.valueOf(response.type()),
        Contract.ServiceType.valueOf(response.serviceType()),
        toId(response.relatedContractId()),
        response.startDate(),
        response.endDate(),
        toId(response.thirdPartyId()),
        toId(response.createdBy()),
        response.createdAt(),
        toId(response.requestedArea()),
        toId(response.requestedCompany()),
        response.amount(),
        Contract.Status.valueOf(response.status()));
  }

  private Id toId(UUID value) {
    return value == null ? null : new Id(value);
  }
}
