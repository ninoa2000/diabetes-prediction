package com.example.chronicdisease.repository;

import com.example.chronicdisease.model.ChronicDisease;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

/**
 * 慢性病知识的数据访问接口
 */
public interface ChronicDiseaseRepository
        extends MongoRepository<ChronicDisease, String> {

    /**
     * 按 order 字段升序返回所有记录，用于列表展示
     */
    List<ChronicDisease> findAllByOrderByOrderAsc();
}
