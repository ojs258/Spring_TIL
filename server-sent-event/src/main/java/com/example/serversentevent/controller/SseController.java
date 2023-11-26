package com.example.serversentevent.controller;

import com.example.serversentevent.dto.RequestUpdateStageOpenDto;
import com.example.serversentevent.service.SseService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.server.Encoding;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
@RequestMapping("sse")
@Slf4j
public class SseController {
    private final SseService sseService;
    @GetMapping(value = "", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter getOpen(@RequestParam Long stageNumber) throws InterruptedException {
        SseEmitter sseEmitter = new SseEmitter();

        while (true) {
            String stageOpen = sseService.findStageOpen(stageNumber);
            if (stageOpen.equals("Open")) {
                writer.write(stageOpen);
                writer.flush();
                break;
            }
            Thread.sleep(5000);
        }
    }

    private void emitterConfig(SseEmitter emitter) {
        emitter.onTimeout(() -> {
            log.info("timeout");
            emitter.complete();
        });

        emitter.onCompletion(() -> {
            log.info("timeout");
            emitter.complete();
        });

        emitter.onError(thr -> {
            log.info("timeout");
            emitter.complete();
        });
    }
    @PutMapping("")
    public ResponseEntity<?> setOpen(@RequestBody RequestUpdateStageOpenDto dto) {
        sseService.updateStageOpen(dto);
        return ResponseEntity.ok("수정 완료");
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<?> handler(Exception e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("등록된 공연 정보가 없습니다.");
    }
}
