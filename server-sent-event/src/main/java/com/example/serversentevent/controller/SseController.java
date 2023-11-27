package com.example.serversentevent.controller;

import com.example.serversentevent.dto.RequestUpdateStageOpenDto;
import com.example.serversentevent.service.SseService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.server.Encoding;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public void getOpen(@RequestParam Integer stageNumber,
                        HttpServletResponse response) throws InterruptedException, IOException {
        response.setContentType(MediaType.TEXT_EVENT_STREAM_VALUE);
        response.setCharacterEncoding(Encoding.DEFAULT_CHARSET.toString());
        PrintWriter writer = response.getWriter();
        while (true) {
            String stageOpen = sseService.findStageOpen(stageNumber);
            writer.write(stageOpen);
            writer.flush();
            if (stageOpen.equals("open")) {
                break;
            }
            log.info("대기중");
            Thread.sleep(2000);
        }
        writer.close();
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
