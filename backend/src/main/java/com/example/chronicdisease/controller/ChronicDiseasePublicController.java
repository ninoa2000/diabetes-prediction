package com.example.chronicdisease.controller;

import com.example.chronicdisease.model.ChronicDisease;
import com.example.chronicdisease.service.ChronicDiseaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 公开接口 —— 用户端获取慢性病知识列表
 * <p>
 * 前端（Vite）只需 axios.get("/api/chronic") 即可。
 * 若有跨域需求，可在 @CrossOrigin 标注允许的前端地址；
 * 当前采用全局 CORS 的话可删除该注解。
 */
@RestController
@RequestMapping("/chronic")
@CrossOrigin(origins = "http://localhost:3000")  // 如已在全局配置，可删
@RequiredArgsConstructor
public class ChronicDiseasePublicController {

    private final ChronicDiseaseService service;

    /**
     * 返回所有慢性病知识（按 order 升序）。
     */
    @GetMapping
    public List<ChronicDisease> list() {
        return service.listAll();
    }
}