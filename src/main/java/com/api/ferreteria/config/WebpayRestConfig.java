package com.api.ferreteria.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebpayRestConfig {

    @Value("${transbank.webpay.commerce-code}")
    private String commerceCode;

    @Value("${transbank.webpay.api-key}")
    private String apiKey;

    @Value("${transbank.webpay.environment}")
    private String environment;

    public String getCommerceCode() {
        return commerceCode;
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getEnvironment() {
        return environment;
    }
}
