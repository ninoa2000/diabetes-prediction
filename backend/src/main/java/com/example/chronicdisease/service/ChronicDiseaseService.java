package com.example.chronicdisease.service;

import com.example.chronicdisease.model.ChronicDisease;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 慢性病知识业务逻辑顶层接口
 */
public interface ChronicDiseaseService {

    /** 按排序字段升序返回全部记录 */
    List<ChronicDisease> listAll();

    /** 分页查询 */
    Page<ChronicDisease> listPage(Pageable pageable);

    /** 根据主键查询 */
    ChronicDisease getById(String id);

    /** 新建记录 */
    ChronicDisease create(ChronicDisease dto);

    /** 更新记录 */
    ChronicDisease update(String id, ChronicDisease dto);

    /** 删除记录 */
    void delete(String id);
}
