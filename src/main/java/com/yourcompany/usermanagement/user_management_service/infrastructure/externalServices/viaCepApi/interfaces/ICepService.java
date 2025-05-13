package com.yourcompany.usermanagement.user_management_service.infrastructure.externalServices.viaCepApi.interfaces;

import com.yourcompany.usermanagement.user_management_service.application.web.address.dto.AddressCepResponse;
import com.yourcompany.usermanagement.user_management_service.infrastructure.externalServices.viaCepApi.dto.ViaCepResponse;

public interface ICepService {
    ViaCepResponse getCodePostal(String cep);
}
