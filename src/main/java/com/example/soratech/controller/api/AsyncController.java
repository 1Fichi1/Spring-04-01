package com.example.soratech.controller.api;

import com.example.soratech.service.AsyncService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Tag(name = "Async API", description = "Демонстрация асинхронных операций для улучшения производительности")
@RestController
@RequestMapping("/api/async")
public class AsyncController {

    @Autowired
    private AsyncService asyncService;

    @Operation(
        summary = "Отправить email асинхронно",
        description = "Запускает отправку email в фоновом режиме. " +
                     "Метод возвращает ответ немедленно, не дожидаясь завершения отправки."
    )
    @PostMapping("/send-email")
    public ResponseEntity<?> sendEmail(@RequestBody Map<String, String> emailData) {
        
        String to = emailData.get("to");
        String subject = emailData.get("subject");
        String body = emailData.get("body");
        
        // Запускаем асинхронную операцию (результат не ждем)
        asyncService.sendEmailAsync(to, subject, body);
        
        // Сразу возвращаем ответ (не ждем завершения)
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Email отправляется в фоновом режиме");
        response.put("status", "processing");
        response.put("to", to);
        
        return ResponseEntity.accepted().body(response);
    }
    
    @Operation(
        summary = "Обработать заказ асинхронно",
        description = "Запускает обработку заказа в фоновом режиме. " +
                     "Включает проверку остатков, резервирование товаров и создание накладной."
    )
    @PostMapping("/process-order/{orderId}")
    public ResponseEntity<?> processOrder(@PathVariable Long orderId) {
        
        // Запускаем асинхронную обработку (результат не ждем)
        asyncService.processOrderAsync(orderId);
        
        // Сразу возвращаем ответ
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Заказ принят в обработку");
        response.put("orderId", orderId);
        response.put("status", "processing");
        
        return ResponseEntity.accepted().body(response);
    }
    
    @Operation(
        summary = "Сгенерировать отчет асинхронно",
        description = "Запускает генерацию отчета в фоновом режиме. " +
                     "Метод возвращает ответ немедленно, генерация происходит в отдельном потоке."
    )
    @GetMapping("/generate-report")
    public ResponseEntity<?> generateReport(@RequestParam String reportType) {
        
        // Запускаем асинхронную генерацию (результат не ждем)
        asyncService.generateReportAsync(reportType);
        
        // Сразу возвращаем ответ
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Отчет генерируется в фоновом режиме");
        response.put("reportType", reportType);
        response.put("status", "processing");
        
        return ResponseEntity.accepted().body(response);
    }
    
    @Operation(
        summary = "Проверить статус асинхронной операции",
        description = "В реальном проекте здесь была бы проверка статуса из Redis или БД"
    )
    @GetMapping("/status/{taskId}")
    public ResponseEntity<?> checkStatus(@PathVariable String taskId) {
        // В реальном проекте здесь была бы проверка статуса
        // из Redis, БД или другого хранилища
        
        Map<String, Object> response = new HashMap<>();
        response.put("taskId", taskId);
        response.put("status", "completed");
        response.put("result", "Операция успешно завершена");
        
        return ResponseEntity.ok(response);
    }
}

