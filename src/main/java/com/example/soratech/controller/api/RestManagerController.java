package com.example.soratech.controller.api;

import com.example.soratech.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@Tag(name = "Manager API", description = "REST API для панели менеджера (требуется роль Менеджер или Администратор)")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/manager")
@PreAuthorize("hasAnyRole('Администратор', 'Менеджер')")
public class RestManagerController {

    private final ProductDetailsService productDetailsService;
    private final ProductService productService;
    private final OrderService orderService;
    private final ReviewService reviewService;
    private final CategoryService categoryService;
    private final ManufacturerService manufacturerService;
    private final UserService userService;

    public RestManagerController(ProductDetailsService productDetailsService,
                                 ProductService productService,
                                 OrderService orderService,
                                 ReviewService reviewService,
                                 CategoryService categoryService,
                                 ManufacturerService manufacturerService,
                                 UserService userService) {
        this.productDetailsService = productDetailsService;
        this.productService = productService;
        this.orderService = orderService;
        this.reviewService = reviewService;
        this.categoryService = categoryService;
        this.manufacturerService = manufacturerService;
        this.userService = userService;
    }

    // Get single item by ID
    @GetMapping("/{entity}/{id}")
    public ResponseEntity<?> getById(@PathVariable String entity, @PathVariable Long id) {
        try {
            Object result = switch (entity) {
                case "productdetails" -> productDetailsService.findById(id);
                case "products" -> productService.findById(id);
                case "orders" -> orderService.findById(id);
                case "reviews" -> reviewService.findById(id);
                default -> throw new IllegalArgumentException("Unknown entity: " + entity);
            };
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    // ProductDetails
    @GetMapping("/productdetails")
    public ResponseEntity<?> getProductDetails(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String query,
            @RequestParam(required = false) Long productId,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(defaultValue = "false") boolean deleted) {
        
        try {
            Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                Sort.by(sortBy).descending() : 
                Sort.by(sortBy).ascending();
            Pageable pageable = PageRequest.of(page, size, sort);
            
            Page<?> result;
            if (deleted) {
                result = productDetailsService.findAllDeleted(pageable);
            } else if (productId != null) {
                result = productDetailsService.findByProductId(productId, pageable);
            } else if (query != null && !query.isEmpty()) {
                result = productDetailsService.searchByCharacteristicName(query, pageable);
            } else {
                result = productDetailsService.findAllActive(pageable);
            }
            
            return ResponseEntity.ok(ApiResponse.success(
                result.getContent(),
                result.getNumber(),
                result.getTotalPages(),
                result.getTotalElements()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    // Products
    @GetMapping("/products")
    public ResponseEntity<?> getProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String query,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long manufacturerId,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(defaultValue = "false") boolean deleted) {
        
        try {
            Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                Sort.by(sortBy).descending() : 
                Sort.by(sortBy).ascending();
            Pageable pageable = PageRequest.of(page, size, sort);
            
            Page<?> result;
            if (deleted) {
                result = productService.findAllDeleted(pageable);
            } else if (query != null && !query.isEmpty()) {
                result = productService.searchByName(query, pageable);
            } else {
                result = productService.findAllActive(pageable);
            }
            
            return ResponseEntity.ok(ApiResponse.success(
                result.getContent(),
                result.getNumber(),
                result.getTotalPages(),
                result.getTotalElements()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    // Orders
    @GetMapping("/orders")
    public ResponseEntity<?> getOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(defaultValue = "false") boolean deleted) {
        
        try {
            Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                Sort.by(sortBy).descending() : 
                Sort.by(sortBy).ascending();
            Pageable pageable = PageRequest.of(page, size, sort);
            
            Page<?> result;
            if (deleted) {
                result = orderService.findAllDeleted(pageable);
            } else if (userId != null) {
                result = orderService.findByUserId(userId, pageable);
            } else if (status != null && !status.isEmpty()) {
                result = orderService.findByStatus(status, pageable);
            } else {
                result = orderService.findAllActive(pageable);
            }
            
            return ResponseEntity.ok(ApiResponse.success(
                result.getContent(),
                result.getNumber(),
                result.getTotalPages(),
                result.getTotalElements()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    // Reviews
    @GetMapping("/reviews")
    public ResponseEntity<?> getReviews(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Long productId,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Integer rating,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(defaultValue = "false") boolean deleted) {
        
        try {
            Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                Sort.by(sortBy).descending() : 
                Sort.by(sortBy).ascending();
            Pageable pageable = PageRequest.of(page, size, sort);
            
            Page<?> result;
            if (deleted) {
                result = reviewService.findAllDeleted(pageable);
            } else if (productId != null) {
                result = reviewService.findByProductId(productId, pageable);
            } else if (userId != null) {
                result = reviewService.findByUserId(userId, pageable);
            } else if (rating != null) {
                result = reviewService.findByRating(rating, pageable);
            } else {
                result = reviewService.findAllActive(pageable);
            }
            
            return ResponseEntity.ok(ApiResponse.success(
                result.getContent(),
                result.getNumber(),
                result.getTotalPages(),
                result.getTotalElements()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    // Helper endpoints for select options
    @GetMapping("/categories")
    public ResponseEntity<?> getCategories(@RequestParam(defaultValue = "1000") int size) {
        try {
            Pageable pageable = PageRequest.of(0, size);
            Page<?> result = categoryService.findAllActive(pageable);
            return ResponseEntity.ok(ApiResponse.success(
                result.getContent(),
                result.getNumber(),
                result.getTotalPages(),
                result.getTotalElements()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/manufacturers")
    public ResponseEntity<?> getManufacturers(@RequestParam(defaultValue = "1000") int size) {
        try {
            Pageable pageable = PageRequest.of(0, size);
            Page<?> result = manufacturerService.findAllActive(pageable);
            return ResponseEntity.ok(ApiResponse.success(
                result.getContent(),
                result.getNumber(),
                result.getTotalPages(),
                result.getTotalElements()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/users")
    public ResponseEntity<?> getUsers(@RequestParam(defaultValue = "1000") int size) {
        try {
            Pageable pageable = PageRequest.of(0, size);
            Page<?> result = userService.findAllActive(pageable);
            return ResponseEntity.ok(ApiResponse.success(
                result.getContent(),
                result.getNumber(),
                result.getTotalPages(),
                result.getTotalElements()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    // ==================== CREATE (POST) ====================
    
    @Operation(summary = "Создать новую запись",
               description = "Менеджер может создавать товары, детали, заказы и отзывы")
    @PostMapping("/{entity}")
    public ResponseEntity<?> create(
            @PathVariable String entity,
            @RequestBody java.util.Map<String, Object> data) {
        try {
            Object result = switch (entity) {
                case "products" -> createProduct(data);
                case "productdetails" -> createProductDetails(data);
                case "orders" -> createOrder(data);
                case "reviews" -> createReview(data);
                default -> throw new IllegalArgumentException("Менеджер не может создавать: " + entity);
            };
            return ResponseEntity.ok(ApiResponse.success(result));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    // ==================== UPDATE (PUT) ====================
    
    @Operation(summary = "Обновить запись",
               description = "Менеджер может обновлять товары, детали, заказы и отзывы")
    @PutMapping("/{entity}/{id}")
    public ResponseEntity<?> update(
            @PathVariable String entity,
            @PathVariable Long id,
            @RequestBody java.util.Map<String, Object> data) {
        try {
            Object result = switch (entity) {
                case "products" -> updateProduct(id, data);
                case "productdetails" -> updateProductDetails(id, data);
                case "orders" -> updateOrder(id, data);
                case "reviews" -> updateReview(id, data);
                default -> throw new IllegalArgumentException("Менеджер не может обновлять: " + entity);
            };
            return ResponseEntity.ok(ApiResponse.success(result));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    // ==================== DELETE (POST logic-delete) ====================
    
    @Operation(summary = "Логическое удаление",
               description = "Менеджер может логически удалять товары, детали, заказы и отзывы")
    @PostMapping("/{entity}/logic-delete/{id}")
    public ResponseEntity<?> logicDelete(
            @PathVariable String entity,
            @PathVariable Long id) {
        try {
            switch (entity) {
                case "products" -> productService.logicDelete(id);
                case "productdetails" -> productDetailsService.logicDelete(id);
                case "orders" -> orderService.logicDelete(id);
                case "reviews" -> reviewService.logicDelete(id);
                default -> throw new IllegalArgumentException("Менеджер не может удалять: " + entity);
            }
            return ResponseEntity.ok(ApiResponse.success("Запись помечена как удаленная"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    // ==================== DELETE (Physical) ====================
    
    @Operation(summary = "Физическое удаление",
               description = "Менеджер может физически удалять товары, детали, заказы и отзывы")
    @DeleteMapping("/{entity}/{id}")
    public ResponseEntity<?> delete(
            @PathVariable String entity,
            @PathVariable Long id) {
        try {
            switch (entity) {
                case "products" -> productService.delete(id);
                case "productdetails" -> productDetailsService.delete(id);
                case "orders" -> orderService.delete(id);
                case "reviews" -> reviewService.delete(id);
                default -> throw new IllegalArgumentException("Менеджер не может удалять: " + entity);
            }
            return ResponseEntity.ok(ApiResponse.success("Запись удалена"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    // ==================== RESTORE ====================
    
    @Operation(summary = "Восстановить удаленную запись",
               description = "Менеджер может восстанавливать логически удаленные записи")
    @PostMapping("/{entity}/restore/{id}")
    public ResponseEntity<?> restore(
            @PathVariable String entity,
            @PathVariable Long id) {
        try {
            switch (entity) {
                case "products" -> productService.restore(id);
                case "productdetails" -> productDetailsService.restore(id);
                case "orders" -> orderService.restore(id);
                case "reviews" -> reviewService.restore(id);
                default -> throw new IllegalArgumentException("Менеджер не может восстанавливать: " + entity);
            }
            return ResponseEntity.ok(ApiResponse.success("Запись восстановлена"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    // ==================== Helper methods for CREATE ====================
    
    private Object createProduct(java.util.Map<String, Object> data) {
        com.example.soratech.model.Product product = new com.example.soratech.model.Product();
        product.setName((String) data.get("name"));
        product.setPrice(new BigDecimal(data.get("price").toString()));
        product.setStockQuantity(Integer.valueOf(data.get("stockQuantity").toString()));
        
        Long categoryId = Long.valueOf(data.get("categoryId").toString());
        Long manufacturerId = Long.valueOf(data.get("manufacturerId").toString());
        java.util.List<Long> supplierIds = data.get("supplierIds") != null ? 
            (java.util.List<Long>) data.get("supplierIds") : java.util.Collections.emptyList();
        
        productService.save(product, manufacturerId, categoryId, supplierIds);
        return product;
    }
    
    private Object createProductDetails(java.util.Map<String, Object> data) {
        com.example.soratech.model.ProductDetails details = new com.example.soratech.model.ProductDetails();
        details.setCharacteristicName((String) data.get("characteristicName"));
        details.setCharacteristicValue((String) data.get("characteristicValue"));
        
        Long productId = Long.valueOf(data.get("productId").toString());
        details.setProduct(productService.findById(productId));
        
        productDetailsService.save(details);
        return details;
    }
    
    private Object createOrder(java.util.Map<String, Object> data) {
        com.example.soratech.model.Order order = new com.example.soratech.model.Order();
        order.setStatus((String) data.get("status"));
        order.setTotalAmount(new BigDecimal(data.get("totalAmount").toString()));
        order.setDeliveryAddress((String) data.get("deliveryAddress"));
        
        Long userId = Long.valueOf(data.get("userId").toString());
        
        orderService.save(order, userId);
        return order;
    }
    
    private Object createReview(java.util.Map<String, Object> data) {
        com.example.soratech.model.Review review = new com.example.soratech.model.Review();
        review.setRating(Integer.valueOf(data.get("rating").toString()));
        review.setComment((String) data.get("comment"));
        
        Long productId = Long.valueOf(data.get("productId").toString());
        Long userId = Long.valueOf(data.get("userId").toString());
        
        reviewService.save(review, productId, userId);
        return review;
    }
    
    // ==================== Helper methods for UPDATE ====================
    
    private Object updateProduct(Long id, java.util.Map<String, Object> data) {
        com.example.soratech.model.Product product = productService.findById(id);
        product.setName((String) data.get("name"));
        product.setPrice(new BigDecimal(data.get("price").toString()));
        product.setStockQuantity(Integer.valueOf(data.get("stockQuantity").toString()));
        
        Long categoryId = Long.valueOf(data.get("categoryId").toString());
        Long manufacturerId = Long.valueOf(data.get("manufacturerId").toString());
        java.util.List<Long> supplierIds = data.get("supplierIds") != null ? 
            (java.util.List<Long>) data.get("supplierIds") : java.util.Collections.emptyList();
        
        productService.update(id, product, manufacturerId, categoryId, supplierIds);
        return product;
    }
    
    private Object updateProductDetails(Long id, java.util.Map<String, Object> data) {
        com.example.soratech.model.ProductDetails details = productDetailsService.findById(id);
        details.setCharacteristicName((String) data.get("characteristicName"));
        details.setCharacteristicValue((String) data.get("characteristicValue"));
        
        Long productId = Long.valueOf(data.get("productId").toString());
        details.setProduct(productService.findById(productId));
        
        productDetailsService.update(id, details);
        return details;
    }
    
    private Object updateOrder(Long id, java.util.Map<String, Object> data) {
        com.example.soratech.model.Order order = orderService.findById(id);
        order.setStatus((String) data.get("status"));
        order.setTotalAmount(new BigDecimal(data.get("totalAmount").toString()));
        order.setDeliveryAddress((String) data.get("deliveryAddress"));
        
        Long userId = Long.valueOf(data.get("userId").toString());
        order.setUser(userService.findById(userId));
        
        orderService.update(id, order);
        return order;
    }
    
    private Object updateReview(Long id, java.util.Map<String, Object> data) {
        com.example.soratech.model.Review review = reviewService.findById(id);
        review.setRating(Integer.valueOf(data.get("rating").toString()));
        review.setComment((String) data.get("comment"));
        
        Long productId = Long.valueOf(data.get("productId").toString());
        Long userId = Long.valueOf(data.get("userId").toString());
        
        review.setProduct(productService.findById(productId));
        review.setUser(userService.findById(userId));
        
        reviewService.update(id, review);
        return review;
    }
}

