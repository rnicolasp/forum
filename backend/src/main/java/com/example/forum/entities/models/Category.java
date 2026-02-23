package com.example.forum.entities.models;
import jakarta.persistence.*;
@Entity
@Table(name="categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false, unique = true)
    private String slug;
    @Column(nullable = false)
    private String description;
    public Category() {}
    public Category(String title, String slug, String description) {
        this.title = title;
        this.slug = slug;
        this.description = description;
    }
    public Category(String slug) {
        this.slug = slug;
    }
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
