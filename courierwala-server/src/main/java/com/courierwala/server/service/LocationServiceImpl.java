package com.courierwala.server.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.beans.factory.annotation.Value;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {

    @Value("${olamaps.api.key:}")
    private String apiKey;

    @Value("${olamaps.base-url:}")
    private String baseUrl;


    private final RestClient restClient;

    public Object autocomplete(String input) {

        if (input == null || input.trim().isEmpty()) {
            throw new IllegalArgumentException("Query param 'input' is required");
        }

        String url = baseUrl + "/autocomplete?input=" + input;

        return restClient.get()
                .uri(url)   // ✅ FULL URL
                .header("X-API-Key", apiKey)
                .header("Accept", "application/json")
                .retrieve()
                .body(Object.class);
    }
}
