package com.example.chronicdisease.service.impl;

import com.example.chronicdisease.exception.ResourceNotFoundException;
import com.example.chronicdisease.model.ChronicDisease;
import com.example.chronicdisease.repository.ChronicDiseaseRepository;
import com.example.chronicdisease.service.ChronicDiseaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 慢性病知识业务逻辑实现
 */
@Service
@RequiredArgsConstructor
public class ChronicDiseaseServiceImpl implements ChronicDiseaseService {

    private final ChronicDiseaseRepository repo;

    @Override
    public List<ChronicDisease> listAll() {
        return repo.findAllByOrderByOrderAsc();
    }

    @Override
    public Page<ChronicDisease> listPage(Pageable pageable) {
        return repo.findAll(pageable);
    }

    @Override
    public ChronicDisease getById(String id) {
        return repo.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("ChronicDisease", id));
    }

    @Override
    public ChronicDisease create(ChronicDisease dto) {
        Date now = new Date();
        dto.setCreatedAt(now);
        dto.setUpdatedAt(now);
        return repo.save(dto);
    }

    @Override
    public ChronicDisease update(String id, ChronicDisease dto) {
        ChronicDisease db = getById(id);
        // 只覆盖可修改字段，保留 id 与创建时间
        BeanUtils.copyProperties(dto, db,
                "id", "createdAt");
        db.setUpdatedAt(new Date());
        return repo.save(db);
    }

    @Override
    public void delete(String id) {
        repo.deleteById(id);
    }
}
