package com.example.soratech.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class AsyncService {

    /**
     * Асинхронная отправка email
     * Симулирует отправку письма с задержкой 2 секунды
     */
    @Async("taskExecutor")
    public CompletableFuture<String> sendEmailAsync(String to, String subject, String body) {
        try {
            System.out.println("=== НАЧАЛО АСИНХРОННОЙ ОПЕРАЦИИ ===");
            System.out.println("Отправка email на: " + to);
            System.out.println("Тема: " + subject);
            System.out.println("Поток: " + Thread.currentThread().getName());
            
            // Симуляция длительной операции
            Thread.sleep(2000);
            
            System.out.println("✓ Email успешно отправлен");
            System.out.println("=== КОНЕЦ АСИНХРОННОЙ ОПЕРАЦИИ ===");
            
            return CompletableFuture.completedFuture("Email успешно отправлен");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return CompletableFuture.completedFuture("Ошибка отправки email");
        }
    }
    
    /**
     * Асинхронная обработка заказа
     * Симулирует обработку заказа с задержкой 3 секунды
     */
    @Async("taskExecutor")
    public CompletableFuture<String> processOrderAsync(Long orderId) {
        try {
            System.out.println("=== НАЧАЛО ОБРАБОТКИ ЗАКАЗА ===");
            System.out.println("ID заказа: " + orderId);
            System.out.println("Поток: " + Thread.currentThread().getName());
            
            // Симуляция обработки:
            // - Проверка остатков на складе
            Thread.sleep(1000);
            System.out.println("  1/3 Проверка остатков...");
            
            // - Резервирование товаров
            Thread.sleep(1000);
            System.out.println("  2/3 Резервирование товаров...");
            
            // - Создание накладной
            Thread.sleep(1000);
            System.out.println("  3/3 Создание накладной...");
            
            System.out.println("✓ Заказ #" + orderId + " успешно обработан");
            System.out.println("=== КОНЕЦ ОБРАБОТКИ ЗАКАЗА ===");
            
            return CompletableFuture.completedFuture("Заказ обработан успешно");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return CompletableFuture.completedFuture("Ошибка обработки заказа");
        }
    }
    
    /**
     * Асинхронная генерация отчета
     * Симулирует генерацию отчета с задержкой 5 секунд
     */
    @Async("taskExecutor")
    public CompletableFuture<String> generateReportAsync(String reportType) {
        try {
            System.out.println("=== НАЧАЛО ГЕНЕРАЦИИ ОТЧЕТА ===");
            System.out.println("Тип отчета: " + reportType);
            System.out.println("Поток: " + Thread.currentThread().getName());
            
            // Симуляция генерации отчета
            for (int i = 1; i <= 5; i++) {
                Thread.sleep(1000);
                System.out.println("  Прогресс: " + (i * 20) + "%");
            }
            
            System.out.println("✓ Отчет '" + reportType + "' успешно сгенерирован");
            System.out.println("=== КОНЕЦ ГЕНЕРАЦИИ ОТЧЕТА ===");
            
            return CompletableFuture.completedFuture("Отчет готов");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return CompletableFuture.completedFuture("Ошибка генерации отчета");
        }
    }
}

