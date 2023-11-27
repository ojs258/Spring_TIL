package com.example.serversentevent.repository;

import java.util.Optional;

public interface SseRepository {
    Optional<String> find(Integer num);
    void update(Integer num, String str);
}
