package com.courierwala.server.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.courierwala.server.service.LocationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/customer/location")
@RequiredArgsConstructor
//@CrossOrigin(origins = "http://3.7.13.235:80")
public class CustomerLocationController {

    private final LocationService locationService;

    @GetMapping("/autocomplete")
    public ResponseEntity<?> autocomplete(@RequestParam String input) {

        try {
        	
        	System.out.println("in auto controller !");
        	System.out.println(locationService.autocomplete(input));
            return ResponseEntity.ok(
                    locationService.autocomplete(input)
            );
        } catch (IllegalArgumentException ex) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("error", ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to call Ola Maps API"));
        }
    }
}
