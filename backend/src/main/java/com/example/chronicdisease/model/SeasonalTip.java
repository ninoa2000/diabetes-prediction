package com.example.chronicdisease.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "seasonal_tips")
public class SeasonalTip {
    @Id
    private String id;
    private String season;      // 季节：spring/summer/autumn/winter
    private String title;       // 标题
    private String content;     // 内容
    private String advice;      // 专家建议
    private Integer order;      // 排序
} 