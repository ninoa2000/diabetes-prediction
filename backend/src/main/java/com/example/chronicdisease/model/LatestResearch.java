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
@Document(collection = "latest_research")
public class LatestResearch {
    @Id
    private String id;
    private String title;       // 标题
    private String content;     // 内容
    private String source;      // 来源
    private String author;      // 作者
    private Date publishDate;   // 发布日期
    private String imageUrl;    // 图片URL
    private String link;        // 原文链接
    private Integer order;      // 排序
    private Date createdAt;
    private Date updatedAt;
} 