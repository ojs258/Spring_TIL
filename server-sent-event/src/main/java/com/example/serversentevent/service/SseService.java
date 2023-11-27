package com.example.serversentevent.service;

import com.example.serversentevent.dto.RequestUpdateStageOpenDto;

public interface SseService {
    String findStageOpen(Integer num);
    void updateStageOpen(RequestUpdateStageOpenDto dto);
}
