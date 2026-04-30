package com.example.chronicdisease.controller;

import com.example.chronicdisease.model.HealthHabit;
import com.example.chronicdisease.service.HealthHabitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 管理端：季节性健康提示管理
 */
@RestController
@RequestMapping("/admin/habits")
//@PreAuthorize("hasRole('ADMIN')")
public class HealthHabitAdminController {

    @Autowired
    private HealthHabitService service;

    @GetMapping
    public List<HealthHabit> listAll() {
        return service.listAll();
    }

    @PostMapping
    public HealthHabit create(@RequestBody HealthHabit dto) {
        return service.create(dto);
    }

    @PutMapping("/{id}")
    public HealthHabit update(
            @PathVariable String id,
            @RequestBody HealthHabit dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        service.delete(id);
    }
}