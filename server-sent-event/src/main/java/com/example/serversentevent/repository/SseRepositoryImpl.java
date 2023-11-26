package com.example.serversentevent.repository;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;

@Repository
public class SseRepositoryImpl implements SseRepository{
    Map<Long, String> storage;

    @PostConstruct
    public void init() {
        storage.put(1L, "close");
        storage.put(2L, "close");
        storage.put(3L, "close");
    }
    public Optional<String> find(Long num) {
        return Optional.ofNullable(storage.get(num));
    }
    public void update(Long num, String str) {
        storage.put(num, str);
    }
}
