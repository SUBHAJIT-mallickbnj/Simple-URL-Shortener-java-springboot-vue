package com.linknest;

import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UrlMappingRepository extends MongoRepository<UrlMapping, String> {
    Optional<UrlMapping> findByShortCode(String shortCode);
    boolean existsByShortCode(String shortCode);
}