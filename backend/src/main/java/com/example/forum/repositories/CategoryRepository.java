package com.example.forum.repositories;
import com.example.forum.entities.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    Optional<Category> findBySlug(String slug); 
}
