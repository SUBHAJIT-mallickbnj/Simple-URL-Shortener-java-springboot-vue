package com.linknest;

import java.net.URI;
import java.util.concurrent.ThreadLocalRandom;
import org.springframework.stereotype.Service;

@Service
public class UrlShortenerService {
    private static final String ALPHABET = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private final UrlMappingRepository repository;

    public UrlShortenerService(UrlMappingRepository repository) {
        this.repository = repository;
    }

    public UrlMapping create(String originalUrl) {
        validateUrl(originalUrl);
        String shortCode;
        do {
            shortCode = generateCode();
        } while (repository.existsByShortCode(shortCode));
        return repository.save(new UrlMapping(shortCode, originalUrl));
    }

    public UrlMapping find(String shortCode) {
        return repository.findByShortCode(shortCode)
                .orElseThrow(() -> new UrlNotFoundException("Short link not found"));
    }

    public void registerClick(UrlMapping mapping) {
        mapping.incrementClicks();
        repository.save(mapping);
    }

    private String generateCode() {
        StringBuilder code = new StringBuilder(7);
        for (int index = 0; index < 7; index++) {
            code.append(ALPHABET.charAt(ThreadLocalRandom.current().nextInt(ALPHABET.length())));
        }
        return code.toString();
    }

    private void validateUrl(String value) {
        try {
            URI uri = URI.create(value);
            if (!("http".equalsIgnoreCase(uri.getScheme()) || "https".equalsIgnoreCase(uri.getScheme()))
                    || uri.getHost() == null) {
                throw new IllegalArgumentException("URL must use a valid http or https address");
            }
        } catch (IllegalArgumentException exception) {
            throw new InvalidUrlException("Please enter a valid URL, including https://");
        }
    }
}