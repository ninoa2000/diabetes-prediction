package com.example.chronicdisease.controller;

import com.example.chronicdisease.model.LatestResearch;
import com.example.chronicdisease.service.LatestResearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 管理端：最新研究资讯管理
 */
@RestController
@RequestMapping("/admin/research")
//@PreAuthorize("hasRole('ADMIN')")
public class LatestResearchAdminController {

    @Autowired
    private LatestResearchService service;

    @GetMapping
    public List<LatestResearch> listAll() {
        return service.listAll();
    }

    @PostMapping
    public LatestResearch create(@RequestBody LatestResearch dto) {
        return service.create(dto);
    }

    @PutMapping("/{id}")
    public LatestResearch update(
            @PathVariable String id,
            @RequestBody LatestResearch dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        service.delete(id);
    }
}