package com.example.chronicdisease.repository;

import com.example.chronicdisease.model.LatestResearch;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

/**
 * 最新研究资讯 DAO
 */
public interface LatestResearchRepository
        extends MongoRepository<LatestResearch, String> {

    /** 按 publishDate 字段降序（最新排前） */
    List<LatestResearch> findAllByOrderByPublishDateDesc();
    // 旧接口里报错的调用：按 order 升序
    List<LatestResearch> findAllByOrderByOrderAsc();
}
