package com.yourcompany.usermanagement.user_management_service.infrastructure.externalServices.viaCepApi;

import com.yourcompany.usermanagement.user_management_service.application.web.errorHandler.ExternalServiceException;
import com.yourcompany.usermanagement.user_management_service.infrastructure.externalServices.viaCepApi.dto.ViaCepResponse;
import com.yourcompany.usermanagement.user_management_service.infrastructure.externalServices.viaCepApi.interfaces.ICepService;
import com.yourcompany.usermanagement.user_management_service.infrastructure.externalServices.viaCepApi.interfaces.ViaCepClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CepService implements ICepService {

    private final ViaCepClient viaCepClient;

    @Override
    public ViaCepResponse getCodePostal(String cep) {
        try {
            return viaCepClient.getAddressByCep(cep);
        } catch (Exception e) {
            throw new ExternalServiceException("Error consulting service ViaCep", e);
        }
    }
}
