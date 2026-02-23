package com.example.forum.controllers;
import com.example.forum.entities.models.Category;
import com.example.forum.entities.models.User;
import com.example.forum.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    @Autowired
    private CategoryRepository categoryRepository;
    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories() {
        return ResponseEntity.ok(categoryRepository.findAll());
    }
    @GetMapping("/{slug}")
    public ResponseEntity<?> getCategoryBySlug(@PathVariable String slug) {
        Optional<Category> cat = categoryRepository.findBySlug(slug);
        if (cat.isPresent()) {
            return ResponseEntity.ok(cat.get());
        }
        return ResponseEntity.notFound().build();
    }
    @PostMapping
    public ResponseEntity<?> createCategory(@RequestBody Category category) {
        if (category.getSlug() == null || category.getSlug().isEmpty()) {
            category.setSlug(category.getTitle().toLowerCase().replace(" ", "-"));
        }
        try {
            Category savedCategory = categoryRepository.save(category);
            return ResponseEntity.ok(savedCategory);
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Error: Es posible que esta categoría ya exista.");
        }
    }
    @PutMapping("/{slug}")
    public ResponseEntity<?> updateCategory(@PathVariable String slug, @RequestBody Category categoryDetails) {
        
        Optional<Category> categoryOpt = categoryRepository.findBySlug(slug);
        if (categoryOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Category category = categoryOpt.get();
        if (categoryDetails.getTitle() != null) category.setTitle(categoryDetails.getTitle());
        if (categoryDetails.getDescription() != null) category.setDescription(categoryDetails.getDescription());
        Category updatedCategory = categoryRepository.save(category);
        return ResponseEntity.ok(updatedCategory);
    }
    @DeleteMapping("/{slug}")
    public ResponseEntity<?> deleteCategory(@PathVariable String slug) {        
        Optional<Category> categoryOpt = categoryRepository.findBySlug(slug);
        if (categoryOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        categoryRepository.delete(categoryOpt.get());
        return ResponseEntity.ok().build();
    }
}
