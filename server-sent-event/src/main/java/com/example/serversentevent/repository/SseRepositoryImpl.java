package com.example.serversentevent.repository;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class SseRepositoryImpl implements SseRepository{
    Map<Integer, String> storage = new HashMap<>();

    public Optional<String> find(Integer num) {
        return Optional.ofNullable(storage.get(num));
    }
    public void update(Integer num, String str) {
        storage.put(num, str);
    }
}
