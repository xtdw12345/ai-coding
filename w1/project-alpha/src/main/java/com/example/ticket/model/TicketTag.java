package com.example.ticket.model;

import java.sql.Timestamp;

/**
 * TicketTag 关联实体类
 * 
 * @author Project Alpha Team
 */
public class TicketTag {
    private Long id;
    private Long ticketId;
    private Long tagId;
    private Timestamp createdAt;

    public TicketTag() {
    }

    public TicketTag(Long ticketId, Long tagId) {
        this.ticketId = ticketId;
        this.tagId = tagId;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTicketId() {
        return ticketId;
    }

    public void setTicketId(Long ticketId) {
        this.ticketId = ticketId;
    }

    public Long getTagId() {
        return tagId;
    }

    public void setTagId(Long tagId) {
        this.tagId = tagId;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "TicketTag{" +
                "id=" + id +
                ", ticketId=" + ticketId +
                ", tagId=" + tagId +
                ", createdAt=" + createdAt +
                '}';
    }
}

