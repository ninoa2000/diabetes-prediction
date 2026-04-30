package com.example.chronicdisease.service;

import com.example.chronicdisease.model.NutritionDiet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 健康生活指南业务接口
 */
public interface NutritionDietService {
    /** 查询所有（按 order 升序） */
    List<NutritionDiet> listAll();

    /** 分页查询 */
    Page<NutritionDiet> listPage(Pageable pageable);

    /** 根据 ID 查询 */
    NutritionDiet getById(String id);

    /** 创建新条目 */
    NutritionDiet create(NutritionDiet dto);

    /** 更新指定 ID 条目 */
    NutritionDiet update(String id, NutritionDiet dto);

    /** 删除指定 ID 条目 */
    void delete(String id);
}