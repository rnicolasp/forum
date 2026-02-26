package com.example.forum.controllers;

import com.example.forum.entities.models.Category;
import com.example.forum.entities.models.User;
import com.example.forum.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    private User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @GetMapping
    public List<Category> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/{slug}")
    public Category getCategoryBySlug(@PathVariable String slug) {
        return categoryService.getCategoryBySlug(slug)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));
    }

    @PostMapping
    public Category createCategory(@RequestBody Category category) {
        return categoryService.createCategory(category);
    }

    @PutMapping("/{slug}")
    public Category updateCategory(@PathVariable String slug, @RequestBody Category categoryDetails) {
        return categoryService.updateCategory(slug, categoryDetails, getCurrentUser());
    }

    @DeleteMapping("/{slug}")
    public void deleteCategory(@PathVariable String slug) {
        categoryService.deleteCategory(slug, getCurrentUser());
    }
}
