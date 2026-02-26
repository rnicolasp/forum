package com.example.forum.services;

import com.example.forum.entities.models.Category;
import com.example.forum.entities.models.User;
import com.example.forum.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired private CategoryRepository categoryRepository;

    public List<Category> getAllCategories() { return categoryRepository.findAll(); }
    
    public Optional<Category> getCategoryBySlug(String slug) { return categoryRepository.findBySlug(slug); }

    public Category createCategory(Category category) {
        if (category.getSlug() == null || category.getSlug().isEmpty()) {
            category.setSlug(category.getTitle().toLowerCase().replace(" ", "-"));
        }
        try {
            return categoryRepository.save(category);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error: Es posible que esta categoría ya exista.");
        }
    }

    public Category updateCategory(String slug, Category categoryDetails, User currentUser) {
        if (currentUser == null || !"admin".equalsIgnoreCase(currentUser.getRole())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Forbidden: Solo los administradores pueden editar categorías");
        }
        Category category = categoryRepository.findBySlug(slug)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found"));
        if (categoryDetails.getTitle() != null) category.setTitle(categoryDetails.getTitle());
        if (categoryDetails.getDescription() != null) category.setDescription(categoryDetails.getDescription());
        return categoryRepository.save(category);
    }

        public void deleteCategory(String slug, User currentUser) {
            Category category = categoryRepository.findBySlug(slug)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found"));
            categoryRepository.delete(category);
        }
}
