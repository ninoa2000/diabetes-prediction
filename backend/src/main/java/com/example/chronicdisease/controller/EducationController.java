package com.example.chronicdisease.controller;

import com.example.chronicdisease.model.ChronicDisease;
import com.example.chronicdisease.model.LatestResearch;
import com.example.chronicdisease.model.SeasonalTip;
import com.example.chronicdisease.service.EducationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/education")
public class EducationController {
    @Autowired
    private EducationService educationService;
    
    @GetMapping("/chronic-diseases")
    public List<ChronicDisease> getAllChronicDiseases() {
        return educationService.getAllChronicDiseases();
    }
    
    @GetMapping("/latest-research")
    public List<LatestResearch> getAllLatestResearch() {
        return educationService.getAllLatestResearch();
    }
    
    @GetMapping("/seasonal-tips/{season}")
    public List<SeasonalTip> getSeasonalTips(@PathVariable String season) {
        return educationService.getSeasonalTips(season);
    }
} 