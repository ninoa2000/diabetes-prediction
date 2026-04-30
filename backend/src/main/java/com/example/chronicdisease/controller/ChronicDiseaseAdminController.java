package com.example.chronicdisease.controller;

import com.example.chronicdisease.model.ChronicDisease;
import com.example.chronicdisease.service.ChronicDiseaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 管理端：慢性病知识管理
 */
@RestController
@RequestMapping("/admin/chronic")
//@PreAuthorize("hasRole('ADMIN')")
public class ChronicDiseaseAdminController {

    @Autowired
    private ChronicDiseaseService service;

    /** 查询所有慢性病知识 */
    @GetMapping
    public List<ChronicDisease> listAll() {
        return service.listAll();
    }

    /** 创建新的慢性病知识 */
    @PostMapping
    public ChronicDisease create(@RequestBody ChronicDisease dto) {
        return service.create(dto);
    }

    /** 更新指定 ID 的慢性病知识 */
    @PutMapping("/{id}")
    public ChronicDisease update(
            @PathVariable String id,
            @RequestBody ChronicDisease dto) {
        return service.update(id, dto);
    }

    /** 删除指定 ID 的慢性病知识 */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        service.delete(id);
    }
}

