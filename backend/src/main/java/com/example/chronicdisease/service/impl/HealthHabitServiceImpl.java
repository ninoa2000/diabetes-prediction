package com.example.chronicdisease.service.impl;

import com.example.chronicdisease.exception.ResourceNotFoundException;
import com.example.chronicdisease.model.HealthHabit;
import com.example.chronicdisease.repository.HealthHabitRepository;
import com.example.chronicdisease.service.HealthHabitService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 季节性健康提示业务实现
 */
@Service
@RequiredArgsConstructor
public class HealthHabitServiceImpl implements HealthHabitService {
    private final HealthHabitRepository repo;

    @Override
    public List<HealthHabit> listAll() {
        return repo.findAllByOrderByOrderAsc();
    }

    @Override
    public Page<HealthHabit> listPage(Pageable pageable) {
        return repo.findAll(pageable);
    }

    @Override
    public HealthHabit getById(String id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("HealthHabit", "id", id));
    }

    @Override
    public HealthHabit create(HealthHabit dto) {
        Date now = new Date();
        dto.setCreatedAt(now);
        dto.setUpdatedAt(now);
        return repo.save(dto);
    }

    @Override
    public HealthHabit update(String id, HealthHabit dto) {
        HealthHabit db = getById(id);
        BeanUtils.copyProperties(dto, db, "id", "createdAt");
        db.setUpdatedAt(new Date());
        return repo.save(db);
    }

    @Override
    public void delete(String id) {
        repo.deleteById(id);
    }
}