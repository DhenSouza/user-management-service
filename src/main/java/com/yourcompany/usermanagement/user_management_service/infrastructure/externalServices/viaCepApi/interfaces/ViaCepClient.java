package com.yourcompany.usermanagement.user_management_service.infrastructure.externalServices.viaCepApi.interfaces;

import com.yourcompany.usermanagement.user_management_service.infrastructure.externalServices.viaCepApi.dto.ViaCepResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "viaCepClient", url = "https://viacep.com.br/ws")
public interface ViaCepClient {

    @GetMapping("/{cep}/json/")
    ViaCepResponse getAddressByCep(@PathVariable("cep") String cep);

}
