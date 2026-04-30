package com.example.chronicdisease.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "chronic_diseases")
public class ChronicDisease {
    @Id
    private String id;
    private String name;        // 疾病名称
    private String description; // 疾病描述
    private String symptoms;    // 症状
    private String causes;      // 病因
    private String prevention;  // 预防措施
    private String treatment;   // 治疗方法
    private List<String> riskFactors; // 风险因素
    private String imageUrl;    // 图片URL
    private Integer order;      // 排序
    private Date createdAt;
    private Date updatedAt;
} 