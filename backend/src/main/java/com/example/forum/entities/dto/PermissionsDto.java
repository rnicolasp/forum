package com.example.forum.entities.dto;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
public class PermissionsDto {
    private List<String> root = Arrays.asList(
            "own_topics:write",
            "own_topics:delete",
            "own_replies:write",
            "own_replies:delete"
    );
    private List<String> categories = Collections.emptyList();
    public List<String> getRoot() { return root; }
    public List<String> getCategories() { return categories; }
}