package com.example.chronicdisease.repository;

import com.example.chronicdisease.model.SeasonalTip;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeasonalTipRepository extends MongoRepository<SeasonalTip, String> {
    List<SeasonalTip> findBySeasonOrderByOrderAsc(String season);
} 