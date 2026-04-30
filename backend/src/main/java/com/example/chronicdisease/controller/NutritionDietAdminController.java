package com.example.chronicdisease.controller;

import com.example.chronicdisease.model.NutritionDiet;
import com.example.chronicdisease.service.NutritionDietService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 管理端：健康生活指南管理
 */
@RestController
@RequestMapping("/admin/nutrition")
//@PreAuthorize("hasRole('ADMIN')")
public class NutritionDietAdminController {

    @Autowired
    private NutritionDietService service;

    @GetMapping
    public List<NutritionDiet> listAll() {
        return service.listAll();
    }

    @PostMapping
    public NutritionDiet create(@RequestBody NutritionDiet dto) {
        return service.create(dto);
    }

    @PutMapping("/{id}")
    public NutritionDiet update(
            @PathVariable String id,
            @RequestBody NutritionDiet dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        service.delete(id);
    }
}