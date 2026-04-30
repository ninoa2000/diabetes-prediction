package com.example.chronicdisease.repository;

import com.example.chronicdisease.model.HealthHabit;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

/**
 * 季节性健康提示 DAO
 */
public interface HealthHabitRepository
        extends MongoRepository<HealthHabit, String> {

    /** 按 order 字段升序 */
    List<HealthHabit> findAllByOrderByOrderAsc();
}
