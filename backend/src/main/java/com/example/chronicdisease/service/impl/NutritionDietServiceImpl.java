package com.example.chronicdisease.service.impl;

import com.example.chronicdisease.exception.ResourceNotFoundException;
import com.example.chronicdisease.model.NutritionDiet;
import com.example.chronicdisease.repository.NutritionDietRepository;
import com.example.chronicdisease.service.NutritionDietService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 健康生活指南业务实现
 */
@Service
@RequiredArgsConstructor
public class NutritionDietServiceImpl implements NutritionDietService {
    private final NutritionDietRepository repo;

    @Override
    public List<NutritionDiet> listAll() {
        return repo.findAllByOrderByOrderAsc();
    }

    @Override
    public Page<NutritionDiet> listPage(Pageable pageable) {
        return repo.findAll(pageable);
    }

    @Override
    public NutritionDiet getById(String id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("NutritionDiet", "id", id));
    }

    @Override
    public NutritionDiet create(NutritionDiet dto) {
        Date now = new Date();
        dto.setCreatedAt(now);
        dto.setUpdatedAt(now);
        return repo.save(dto);
    }

    @Override
    public NutritionDiet update(String id, NutritionDiet dto) {
        NutritionDiet db = getById(id);
        BeanUtils.copyProperties(dto, db, "id", "createdAt");
        db.setUpdatedAt(new Date());
        return repo.save(db);
    }

    @Override
    public void delete(String id) {
        repo.deleteById(id);
    }
}

