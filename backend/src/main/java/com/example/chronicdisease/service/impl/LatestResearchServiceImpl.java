package com.example.chronicdisease.service.impl;

import com.example.chronicdisease.exception.ResourceNotFoundException;
import com.example.chronicdisease.model.LatestResearch;
import com.example.chronicdisease.repository.LatestResearchRepository;
import com.example.chronicdisease.service.LatestResearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 最新研究资讯业务实现
 */
@Service
@RequiredArgsConstructor
public class LatestResearchServiceImpl implements LatestResearchService {
    private final LatestResearchRepository repo;

    @Override
    public List<LatestResearch> listAll() {
        return repo.findAllByOrderByPublishDateDesc();
    }

    @Override
    public Page<LatestResearch> listPage(Pageable pageable) {
        return repo.findAll(pageable);
    }

    @Override
    public LatestResearch getById(String id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("LatestResearch", "id", id));
    }

    @Override
    public LatestResearch create(LatestResearch dto) {
        Date now = new Date();
        dto.setCreatedAt(now);
        dto.setUpdatedAt(now);
        return repo.save(dto);
    }

    @Override
    public LatestResearch update(String id, LatestResearch dto) {
        LatestResearch db = getById(id);
        BeanUtils.copyProperties(dto, db, "id", "createdAt");
        db.setUpdatedAt(new Date());
        return repo.save(db);
    }

    @Override
    public void delete(String id) {
        repo.deleteById(id);
    }
}