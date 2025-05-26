package com.yourcompany.usermanagement.user_management_service.infrastructure.externalServices.viaCepApi.dto;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ViaCepResponse {

    @JsonProperty("cep")
    private String postalCode;

    @JsonProperty("logradouro")
    private String street;

    @JsonProperty("complemento")
    private String complement;

    @JsonProperty("bairro")
    private String neighborhood;

    @JsonProperty("localidade")
    private String city;

    @JsonProperty("uf")
    private String state;

    @JsonProperty("ibge")
    private String ibgeCode;

    @JsonProperty("gia")
    private String giaCode;

    @JsonProperty("ddd")
    private String ddd;

    @JsonProperty("siafi")
    private String siafiCode;

}
