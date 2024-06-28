package com.puas.serverapp.controllers.impl;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.puas.serverapp.controllers.GenericController;
import com.puas.serverapp.models.entities.Category;
import com.puas.serverapp.services.impl.CategoryServiceImpl;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/category")
@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
public class CategoryControllerImpl implements GenericController<Category, Integer> {

    private CategoryServiceImpl categoryServiceImpl;

    @GetMapping
    @Override
    public List<Category> getAll() {
        return categoryServiceImpl.getAll();
    }

    @Override
    @GetMapping("/{id}")
    public Category getById(@PathVariable Integer id) {
        return categoryServiceImpl.getById(id);
    }

    @PostMapping
    @Override
    public Category create(@RequestBody Category category) {
        return categoryServiceImpl.create(category);
    }

    @PutMapping("/{id}")
    @Override
    public Category update(@PathVariable Integer id, @RequestBody Category category) {
        return categoryServiceImpl.update(id, category);
    }

    @Override
    @DeleteMapping("/{id}")
    public Category delete(@PathVariable Integer id) {
        return categoryServiceImpl.delete(id);
    }

}
