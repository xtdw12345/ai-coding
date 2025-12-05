package com.example.ticket.model;

import java.sql.Timestamp;

/**
 * Tag 实体类
 * 
 * @author Project Alpha Team
 */
public class Tag {
    private Long id;
    private String name;
    private String color;
    private Timestamp createdAt;

    public Tag() {
    }

    public Tag(String name) {
        this.name = name;
        this.color = "#86868b"; // 默认灰色
    }

    public Tag(String name, String color) {
        this.name = name;
        this.color = color != null ? color : "#86868b";
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color != null ? color : "#86868b";
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Tag{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", color='" + color + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}

