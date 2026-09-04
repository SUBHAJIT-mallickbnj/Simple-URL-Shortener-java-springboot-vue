package com.linknest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.net.URI;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/urls")
@CrossOrigin(originPatterns = {
    "http://localhost:5173",
    "http://127.0.0.1:5173",
    "${app.frontend-url:*}"
})
public class UrlController {
    private final UrlShortenerService service;
    private final String publicUrl;

    public UrlController(UrlShortenerService service, @Value("${app.public-url:http://localhost:8080}") String publicUrl) {
        this.service = service;
        this.publicUrl = publicUrl.replaceAll("/$", "");
    }

    @PostMapping
    public ResponseEntity<UrlResponse> shorten(@Valid @RequestBody CreateUrlRequest request, HttpServletRequest httpRequest) {
        UrlMapping mapping = service.create(request.url());
        return ResponseEntity.ok(toResponse(mapping, httpRequest));
    }

    @GetMapping("/{shortCode}")
    public ResponseEntity<UrlResponse> inspect(@PathVariable String shortCode, HttpServletRequest httpRequest) {
        return ResponseEntity.ok(toResponse(service.find(shortCode), httpRequest));
    }

    @GetMapping("/health")
    public Map<String, String> health() { return Map.of("status", "ok", "service", "linknest-api"); }

    private UrlResponse toResponse(UrlMapping mapping, HttpServletRequest httpRequest) {
        String base = publicUrl.isBlank()
                ? httpRequest.getRequestURL().toString().replace(httpRequest.getRequestURI(), "")
                : publicUrl;
        base += "/" + mapping.getShortCode();
        return new UrlResponse(mapping.getShortCode(), mapping.getOriginalUrl(), base, mapping.getCreatedAt().toString());
    }

    public record CreateUrlRequest(@NotBlank @Size(max = 2048) String url) { }
    public record UrlResponse(String shortCode, String originalUrl, String shortUrl, String createdAt) { }
}