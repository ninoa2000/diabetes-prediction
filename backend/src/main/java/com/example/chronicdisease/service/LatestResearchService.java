package com.example.chronicdisease.service;

import com.example.chronicdisease.model.LatestResearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 最新研究资讯业务接口
 */
public interface LatestResearchService {
    List<LatestResearch> listAll();
    Page<LatestResearch> listPage(Pageable pageable);
    LatestResearch getById(String id);
    LatestResearch create(LatestResearch dto);
    LatestResearch update(String id, LatestResearch dto);
    void delete(String id);
}
