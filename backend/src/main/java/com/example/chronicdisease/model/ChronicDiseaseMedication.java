package com.example.chronicdisease.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "chronic_disease_medications")
public class ChronicDiseaseMedication {
    @Id
    private String id;
    private String name;        // 药品名称
    private String disease;     // 适用疾病
    private String usage;       // 用法用量
    private String sideEffects; // 副作用
    private String precautions; // 注意事项
    private String imageUrl;    // 图片URL
    private Integer order;      // 排序
} 