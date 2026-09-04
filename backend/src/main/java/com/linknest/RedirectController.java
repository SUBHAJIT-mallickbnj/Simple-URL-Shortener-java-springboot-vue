package com.linknest;

import java.net.URI;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class RedirectController {
    private final UrlShortenerService service;

    public RedirectController(UrlShortenerService service) { this.service = service; }

    @GetMapping("/{shortCode:[A-Za-z0-9]{7}}")
    public ResponseEntity<Void> redirect(@PathVariable String shortCode) {
        UrlMapping mapping = service.find(shortCode);
        service.registerClick(mapping);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(mapping.getOriginalUrl()));
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }
}