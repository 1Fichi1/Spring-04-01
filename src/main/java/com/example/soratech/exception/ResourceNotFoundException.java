package com.example.soratech.exception;

/**
 * Кастомное исключение для случаев, когда запрашиваемый ресурс не найден
 * Используется в сервисах при попытке получить несуществующую сущность
 */
public class ResourceNotFoundException extends RuntimeException {
    
    /**
     * Создает исключение с произвольным сообщением
     * 
     * @param message текст сообщения об ошибке
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }
    
    /**
     * Создает исключение с форматированным сообщением
     * Например: "User не найден с id: '123'"
     * 
     * @param resourceName название ресурса (User, Product и т.д.)
     * @param fieldName название поля (id, email и т.д.)
     * @param fieldValue значение поля
     */
    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s не найден с %s: '%s'", resourceName, fieldName, fieldValue));
    }
}
