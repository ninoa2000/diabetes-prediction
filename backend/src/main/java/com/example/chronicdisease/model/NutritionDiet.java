package com.example.chronicdisease.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "nutrition_diets")
public class NutritionDiet {
    @Id
    private String id;
    private String title;       // 标题
    private String content;     // 内容
    private String category;    // 类别：早餐/午餐/晚餐/零食
    private String imageUrl;    // 图片URL
    private Integer order;      // 排序
    private Date createdAt;
    private Date updatedAt;
} 