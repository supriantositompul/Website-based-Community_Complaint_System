package com.puas.serverapp.services.impl;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.puas.serverapp.models.entities.Category;
import com.puas.serverapp.repositories.CategoryRepository;
import com.puas.serverapp.services.GenericService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class CategoryServiceImpl implements GenericService<Category, Integer> {

    private CategoryRepository categoryRepository;

    @Override
    public List<Category> getAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Category getById(Integer id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "category not found"));
    }

    @Override
    public Category create(Category category) {
        if (categoryRepository.findByName(category.getName()).isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "category name is already!");
        }
        return categoryRepository.save(category);
    }

    @Override
    public Category update(Integer id, Category category) {
        Category existingCategory = getById(id);
        existingCategory.setId(id);

        if (categoryRepository.existsByNameAndId(category.getName(), id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "category is already exists!");
        }

        existingCategory.setName(category.getName());
        return categoryRepository.save(existingCategory);
    }

    @Override
    public Category delete(Integer id) {
        Category category = getById(id);
        categoryRepository.delete(category);
        return category;
    }

}
