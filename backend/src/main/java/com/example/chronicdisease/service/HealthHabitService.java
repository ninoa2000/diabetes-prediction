package com.example.chronicdisease.service;

import com.example.chronicdisease.model.HealthHabit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 季节性健康提示业务接口
 */
public interface HealthHabitService {
    List<HealthHabit> listAll();
    Page<HealthHabit> listPage(Pageable pageable);
    HealthHabit getById(String id);
    HealthHabit create(HealthHabit dto);
    HealthHabit update(String id, HealthHabit dto);
    void delete(String id);
}