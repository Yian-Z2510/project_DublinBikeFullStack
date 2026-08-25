package com.dublin.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/config")
public class AppConfigController {

    @Value("${google.maps.api-key:}")
    private String googleMapsApiKey;

    @GetMapping
    public Map<String, String> getPublicConfig() {
        return Collections.singletonMap("googleMapsApiKey", googleMapsApiKey);
    }
}
