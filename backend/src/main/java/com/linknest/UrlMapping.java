package com.linknest;

import java.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("url_mappings")
public class UrlMapping {
    @Id
    private String id;
    @Indexed(unique = true)
    private String shortCode;
    private String originalUrl;
    private Instant createdAt;
    private long clicks;

    protected UrlMapping() { }

    public UrlMapping(String shortCode, String originalUrl) {
        this.shortCode = shortCode;
        this.originalUrl = originalUrl;
        this.createdAt = Instant.now();
    }

    public String getShortCode() { return shortCode; }
    public String getOriginalUrl() { return originalUrl; }
    public Instant getCreatedAt() { return createdAt; }
    public long getClicks() { return clicks; }
    public void incrementClicks() { clicks++; }
}