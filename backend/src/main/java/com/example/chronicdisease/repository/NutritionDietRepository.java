package com.example.chronicdisease.repository;

import com.example.chronicdisease.model.NutritionDiet;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

/**
 * 健康生活指南 DAO
 */
public interface NutritionDietRepository
        extends MongoRepository<NutritionDiet, String> {

    /** 按 order 字段升序 */
    List<NutritionDiet> findAllByOrderByOrderAsc();
}
