package com.example.chronicdisease.service;

import com.example.chronicdisease.model.ChronicDisease;
import com.example.chronicdisease.model.LatestResearch;
import com.example.chronicdisease.model.SeasonalTip;
import com.example.chronicdisease.repository.ChronicDiseaseRepository;
import com.example.chronicdisease.repository.LatestResearchRepository;
import com.example.chronicdisease.repository.SeasonalTipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

public interface EducationService {

    public List<ChronicDisease> getAllChronicDiseases();;

    public List<LatestResearch> getAllLatestResearch();

    public List<SeasonalTip> getSeasonalTips(String season);

}