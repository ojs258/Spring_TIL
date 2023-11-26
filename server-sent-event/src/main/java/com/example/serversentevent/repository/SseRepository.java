package com.example.serversentevent.repository;

import java.util.Optional;

public interface SseRepository {
    Optional<String> find(Long num);
    void update(Long num, String str);
}
