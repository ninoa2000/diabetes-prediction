package com.example.chronicdisease.service.impl;

import com.example.chronicdisease.model.ChronicDisease;
import com.example.chronicdisease.model.LatestResearch;
import com.example.chronicdisease.model.SeasonalTip;
import com.example.chronicdisease.repository.ChronicDiseaseRepository;
import com.example.chronicdisease.repository.LatestResearchRepository;
import com.example.chronicdisease.repository.SeasonalTipRepository;
import com.example.chronicdisease.service.EducationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EducationServiceImpl implements EducationService {
    @Autowired
    private ChronicDiseaseRepository chronicDiseaseRepository;

    @Autowired
    private LatestResearchRepository latestResearchRepository;

    @Autowired
    private SeasonalTipRepository seasonalTipRepository;

    public List<ChronicDisease> getAllChronicDiseases() {
        return chronicDiseaseRepository.findAllByOrderByOrderAsc();
    }

    public List<LatestResearch> getAllLatestResearch() {
        return latestResearchRepository.findAllByOrderByOrderAsc();
    }

    public List<SeasonalTip> getSeasonalTips(String season) {
        return seasonalTipRepository.findBySeasonOrderByOrderAsc(season);
    }

}
