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
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@Tag(name = "Admin API", description = "REST API для панели администратора (требуется роль Администратор)")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/admin")
public class RestAdminController {

    private final UserService userService;
    private final RoleService roleService;
    private final CategoryService categoryService;
    private final ManufacturerService manufacturerService;
    private final SupplierService supplierService;
    private final ProductDetailsService productDetailsService;
    private final ProductService productService;
    private final OrderService orderService;
    private final ReviewService reviewService;

    public RestAdminController(UserService userService,
                               RoleService roleService,
                               CategoryService categoryService,
                               ManufacturerService manufacturerService,
                               SupplierService supplierService,
                               ProductDetailsService productDetailsService,
                               ProductService productService,
                               OrderService orderService,
                               ReviewService reviewService) {
        this.userService = userService;
        this.roleService = roleService;
        this.categoryService = categoryService;
        this.manufacturerService = manufacturerService;
        this.supplierService = supplierService;
        this.productDetailsService = productDetailsService;
        this.productService = productService;
        this.orderService = orderService;
        this.reviewService = reviewService;
    }

    // Get single item by ID
    @GetMapping("/{entity}/{id}")
    public ResponseEntity<?> getById(@PathVariable String entity, @PathVariable Long id) {
        try {
            Object result = switch (entity) {
                case "users" -> userService.findById(id);
                case "roles" -> roleService.findById(id);
                case "categories" -> categoryService.findById(id);
                case "manufacturers" -> manufacturerService.findById(id);
                case "suppliers" -> supplierService.findById(id);
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

    // Users
    @GetMapping("/users")
    public ResponseEntity<?> getUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String searchType,
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
                result = userService.findAllDeleted(pageable);
            } else if (query != null && !query.isEmpty()) {
                result = "email".equals(searchType) ? 
                    userService.searchByEmail(query, pageable) : 
                    userService.searchByName(query, pageable);
            } else {
                result = userService.findAllActive(pageable);
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

    // Roles
    @GetMapping("/roles")
    public ResponseEntity<?> getRoles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String query,
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
                result = roleService.findAllDeleted(pageable);
            } else if (query != null && !query.isEmpty()) {
                result = roleService.searchByName(query, pageable);
            } else {
                result = roleService.findAllActive(pageable);
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

    // Categories
    @GetMapping("/categories")
    public ResponseEntity<?> getCategories(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String query,
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
                result = categoryService.findAllDeleted(pageable);
            } else if (query != null && !query.isEmpty()) {
                result = categoryService.searchByName(query, pageable);
            } else {
                result = categoryService.findAllActive(pageable);
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

    // Manufacturers
    @GetMapping("/manufacturers")
    public ResponseEntity<?> getManufacturers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String country,
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
                result = manufacturerService.findAllDeleted(pageable);
            } else if (query != null && !query.isEmpty()) {
                result = manufacturerService.searchByName(query, pageable);
            } else if (country != null && !country.isEmpty()) {
                result = manufacturerService.filterByCountry(country, pageable);
            } else {
                result = manufacturerService.findAllActive(pageable);
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

    // Suppliers
    @GetMapping("/suppliers")
    public ResponseEntity<?> getSuppliers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String query,
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
                result = supplierService.findAllDeleted(pageable);
            } else if (query != null && !query.isEmpty()) {
                result = supplierService.searchByName(query, pageable);
            } else {
                result = supplierService.findAllActive(pageable);
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
            } else if (categoryId != null) {
                result = productService.filterByCategory(categoryId, pageable);
            } else if (manufacturerId != null) {
                result = productService.filterByManufacturer(manufacturerId, pageable);
            } else if (minPrice != null || maxPrice != null) {
                result = productService.filterByPriceRange(minPrice, maxPrice, pageable);
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
    
    // ==================== CREATE (POST) ====================
    
    @Operation(summary = "Создать новую сущность", 
               description = "Универсальный метод для создания любой сущности")
    @PostMapping("/{entity}")
    public ResponseEntity<?> create(
            @PathVariable String entity,
            @RequestBody java.util.Map<String, Object> data) {
        try {
            Object result = switch (entity) {
                case "users" -> createUser(data);
                case "roles" -> createRole(data);
                case "categories" -> createCategory(data);
                case "manufacturers" -> createManufacturer(data);
                case "suppliers" -> createSupplier(data);
                case "products" -> createProduct(data);
                case "productdetails" -> createProductDetails(data);
                case "orders" -> createOrder(data);
                case "reviews" -> createReview(data);
                default -> throw new IllegalArgumentException("Unknown entity: " + entity);
            };
            return ResponseEntity.ok(ApiResponse.success(result));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    // ==================== UPDATE (PUT) ====================
    
    @Operation(summary = "Обновить существующую сущность",
               description = "Универсальный метод для обновления любой сущности по ID")
    @PutMapping("/{entity}/{id}")
    public ResponseEntity<?> update(
            @PathVariable String entity,
            @PathVariable Long id,
            @RequestBody java.util.Map<String, Object> data) {
        try {
            Object result = switch (entity) {
                case "users" -> updateUser(id, data);
                case "roles" -> updateRole(id, data);
                case "categories" -> updateCategory(id, data);
                case "manufacturers" -> updateManufacturer(id, data);
                case "suppliers" -> updateSupplier(id, data);
                case "products" -> updateProduct(id, data);
                case "productdetails" -> updateProductDetails(id, data);
                case "orders" -> updateOrder(id, data);
                case "reviews" -> updateReview(id, data);
                default -> throw new IllegalArgumentException("Unknown entity: " + entity);
            };
            return ResponseEntity.ok(ApiResponse.success(result));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    // ==================== DELETE (POST logic-delete) ====================
    
    @Operation(summary = "Логическое удаление сущности",
               description = "Помечает сущность как удаленную без физического удаления из БД")
    @PostMapping("/{entity}/logic-delete/{id}")
    public ResponseEntity<?> logicDelete(
            @PathVariable String entity,
            @PathVariable Long id) {
        try {
            switch (entity) {
                case "users" -> userService.logicDelete(id);
                case "roles" -> roleService.logicDelete(id);
                case "categories" -> categoryService.logicDelete(id);
                case "manufacturers" -> manufacturerService.logicDelete(id);
                case "suppliers" -> supplierService.logicDelete(id);
                case "products" -> productService.logicDelete(id);
                case "productdetails" -> productDetailsService.logicDelete(id);
                case "orders" -> orderService.logicDelete(id);
                case "reviews" -> reviewService.logicDelete(id);
                default -> throw new IllegalArgumentException("Unknown entity: " + entity);
            }
            return ResponseEntity.ok(ApiResponse.success("Запись помечена как удаленная"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    // ==================== DELETE (Physical) ====================
    
    @Operation(summary = "Физическое удаление сущности",
               description = "Безвозвратно удаляет сущность из базы данных")
    @DeleteMapping("/{entity}/{id}")
    public ResponseEntity<?> delete(
            @PathVariable String entity,
            @PathVariable Long id) {
        try {
            switch (entity) {
                case "users" -> userService.delete(id);
                case "roles" -> roleService.delete(id);
                case "categories" -> categoryService.delete(id);
                case "manufacturers" -> manufacturerService.delete(id);
                case "suppliers" -> supplierService.delete(id);
                case "products" -> productService.delete(id);
                case "productdetails" -> productDetailsService.delete(id);
                case "orders" -> orderService.delete(id);
                case "reviews" -> reviewService.delete(id);
                default -> throw new IllegalArgumentException("Unknown entity: " + entity);
            }
            return ResponseEntity.ok(ApiResponse.success("Запись удалена"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    // ==================== RESTORE ====================
    
    @Operation(summary = "Восстановить удаленную сущность",
               description = "Восстанавливает логически удаленную сущность")
    @PostMapping("/{entity}/restore/{id}")
    public ResponseEntity<?> restore(
            @PathVariable String entity,
            @PathVariable Long id) {
        try {
            switch (entity) {
                case "users" -> userService.restore(id);
                case "roles" -> roleService.restore(id);
                case "categories" -> categoryService.restore(id);
                case "manufacturers" -> manufacturerService.restore(id);
                case "suppliers" -> supplierService.restore(id);
                case "products" -> productService.restore(id);
                case "productdetails" -> productDetailsService.restore(id);
                case "orders" -> orderService.restore(id);
                case "reviews" -> reviewService.restore(id);
                default -> throw new IllegalArgumentException("Unknown entity: " + entity);
            }
            return ResponseEntity.ok(ApiResponse.success("Запись восстановлена"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    // ==================== Helper methods for CREATE ====================
    
    private Object createUser(java.util.Map<String, Object> data) {
        com.example.soratech.model.User user = new com.example.soratech.model.User();
        user.setName((String) data.get("name"));
        user.setEmail((String) data.get("email"));
        user.setPhone((String) data.get("phone"));
        
        org.springframework.security.crypto.password.PasswordEncoder encoder = 
            new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder();
        user.setPassword(encoder.encode((String) data.get("password")));
        
        user.setAddress((String) data.get("address"));
        user.setActive(data.get("active") != null ? (Boolean) data.get("active") : true);
        
        Long roleId = Long.valueOf(data.get("roleId").toString());
        userService.save(user, roleId);
        return user;
    }
    
    private Object createRole(java.util.Map<String, Object> data) {
        com.example.soratech.model.Role role = new com.example.soratech.model.Role();
        role.setName((String) data.get("name"));
        role.setDescription((String) data.get("description"));
        roleService.save(role);
        return role;
    }
    
    private Object createCategory(java.util.Map<String, Object> data) {
        com.example.soratech.model.Category category = new com.example.soratech.model.Category();
        category.setName((String) data.get("name"));
        category.setDescription((String) data.get("description"));
        categoryService.save(category);
        return category;
    }
    
    private Object createManufacturer(java.util.Map<String, Object> data) {
        com.example.soratech.model.Manufacturer manufacturer = new com.example.soratech.model.Manufacturer();
        manufacturer.setName((String) data.get("name"));
        manufacturer.setCountry((String) data.get("country"));
        manufacturer.setEmail((String) data.get("email"));
        manufacturer.setPhone((String) data.get("phone"));
        manufacturer.setAddress((String) data.get("address"));
        manufacturerService.save(manufacturer);
        return manufacturer;
    }
    
    private Object createSupplier(java.util.Map<String, Object> data) {
        com.example.soratech.model.Supplier supplier = new com.example.soratech.model.Supplier();
        supplier.setName((String) data.get("name"));
        supplier.setContactPerson((String) data.get("contactPerson"));
        supplier.setPhone((String) data.get("phone"));
        supplier.setEmail((String) data.get("email"));
        supplier.setAddress((String) data.get("address"));
        supplierService.save(supplier);
        return supplier;
    }
    
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
    
    private Object updateUser(Long id, java.util.Map<String, Object> data) {
        com.example.soratech.model.User user = userService.findById(id);
        user.setName((String) data.get("name"));
        user.setEmail((String) data.get("email"));
        user.setPhone((String) data.get("phone"));
        user.setAddress((String) data.get("address"));
        
        if (data.get("password") != null && !((String) data.get("password")).isEmpty()) {
            org.springframework.security.crypto.password.PasswordEncoder encoder = 
                new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder();
            user.setPassword(encoder.encode((String) data.get("password")));
        }
        
        if (data.get("active") != null) {
            user.setActive((Boolean) data.get("active"));
        }
        
        Long roleId = Long.valueOf(data.get("roleId").toString());
        userService.update(id, user, roleId);
        return user;
    }
    
    private Object updateRole(Long id, java.util.Map<String, Object> data) {
        com.example.soratech.model.Role role = roleService.findById(id);
        role.setName((String) data.get("name"));
        role.setDescription((String) data.get("description"));
        roleService.update(id, role);
        return role;
    }
    
    private Object updateCategory(Long id, java.util.Map<String, Object> data) {
        com.example.soratech.model.Category category = categoryService.findById(id);
        category.setName((String) data.get("name"));
        category.setDescription((String) data.get("description"));
        categoryService.update(id, category);
        return category;
    }
    
    private Object updateManufacturer(Long id, java.util.Map<String, Object> data) {
        com.example.soratech.model.Manufacturer manufacturer = manufacturerService.findById(id);
        manufacturer.setName((String) data.get("name"));
        manufacturer.setCountry((String) data.get("country"));
        manufacturer.setEmail((String) data.get("email"));
        manufacturer.setPhone((String) data.get("phone"));
        manufacturer.setAddress((String) data.get("address"));
        manufacturerService.update(id, manufacturer);
        return manufacturer;
    }
    
    private Object updateSupplier(Long id, java.util.Map<String, Object> data) {
        com.example.soratech.model.Supplier supplier = supplierService.findById(id);
        supplier.setName((String) data.get("name"));
        supplier.setContactPerson((String) data.get("contactPerson"));
        supplier.setPhone((String) data.get("phone"));
        supplier.setEmail((String) data.get("email"));
        supplier.setAddress((String) data.get("address"));
        supplierService.update(id, supplier);
        return supplier;
    }
    
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

