package com.example.serversentevent.service;

import com.example.serversentevent.dto.RequestUpdateStageOpenDto;
import com.example.serversentevent.repository.SseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SseServiceImpl implements SseService{
    private final SseRepository sseRepository;
    @Override
    public String findStageOpen(Integer num) {
        return sseRepository.find(num)
                .orElseThrow(() -> new NoSuchElementException("찾을 수 없습니다."));
    }

    @Override
    public void updateStageOpen(RequestUpdateStageOpenDto dto) {
        sseRepository.update(dto.getNum(), dto.getStr());
    }
}
