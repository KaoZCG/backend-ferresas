package com.api.ferreteria.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class WebpayRestService {

    @Value("${transbank.webpay.commerce-code}")
    private String commerceCode;

    @Value("${transbank.webpay.api-key}")
    private String apiKey;

    @Value("${transbank.webpay.environment}")
    private String environment;

    private String getBaseUrl() {
        if ("TEST".equalsIgnoreCase(environment)) {
            return "https://webpay3gint.transbank.cl/rswebpaytransaction/api/webpay/v1.3";
        } else {
            return "https://webpay3g.transbank.cl/rswebpaytransaction/api/webpay/v1.3";
        }
    }

    public Map<String, Object> createTransaction(int amount, String returnUrl) {
        RestTemplate restTemplate = new RestTemplate();

        Map<String, Object> body = new HashMap<>();
        body.put("buy_order", "orden-" + System.currentTimeMillis());
        body.put("session_id", "session-" + System.currentTimeMillis());
        body.put("amount", amount);
        body.put("return_url", returnUrl);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Tbk-Api-Key-Id", commerceCode);
        headers.set("Tbk-Api-Key-Secret", apiKey);

        // Debug
        System.out.println("Tbk-Api-Key-Id: [" + commerceCode + "]");
        System.out.println("Tbk-Api-Key-Secret: [" + apiKey + "]");

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(
                getBaseUrl() + "/transactions",
                request,
                Map.class
        );

        return response.getBody();
    }

    public Map<String, Object> commitTransaction(String token) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Tbk-Api-Key-Id", commerceCode);
        headers.set("Tbk-Api-Key-Secret", apiKey);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                getBaseUrl() + "/transactions/" + token,
                HttpMethod.PUT,
                request,
                Map.class
        );

        return response.getBody();
    }
}