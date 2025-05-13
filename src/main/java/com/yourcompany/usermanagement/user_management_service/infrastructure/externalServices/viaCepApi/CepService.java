package com.yourcompany.usermanagement.user_management_service.infrastructure.externalServices.viaCepApi;

import com.yourcompany.usermanagement.user_management_service.application.web.errorHandler.ExternalServiceException;
import com.yourcompany.usermanagement.user_management_service.infrastructure.externalServices.viaCepApi.dto.ViaCepResponse;
import com.yourcompany.usermanagement.user_management_service.infrastructure.externalServices.viaCepApi.interfaces.ICepService;
import com.yourcompany.usermanagement.user_management_service.application.web.address.dto.AddressCepResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class CepService implements ICepService {

    private final WebClient webClient = WebClient.builder()
            .baseUrl("https://viacep.com.br/ws")
            .build();

    public ViaCepResponse getCodePostal(String cep) {
        try {
            return webClient.get()
                    .uri("/{cep}/json/", cep)
                    .retrieve()
                    .bodyToMono(ViaCepResponse.class)
                    .block();
        }catch (Exception e){
            throw new ExternalServiceException("Error consult service ViaCep", e);
        }
    }


}
