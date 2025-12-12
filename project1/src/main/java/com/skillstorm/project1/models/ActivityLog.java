package com.skillstorm.project1.models;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * This is responsible for representing a log entry describing an action performed in the system.
 * Used for auditing and tracking recent activity such as inventory changes or warehouse updates.
 */
@Entity
@Table
public class ActivityLog {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "entity_type")
    private String entityType;

    @Column(name = "entity_id")
    private Integer entityId;

    @Column(name = "activity_type")
    private String activityType;

    @Column
    private String description;

    @Column
    private LocalDateTime createdAt;

    public ActivityLog() {
    }

    /**
     * This creates a new Activitylog entry.
     * @param entityType the type of entity affected
     * @param entityId the ID of the entity affected
     * @param activityType the type of activity
     * @param description descriptive message
     * @param createdAt timestamp of event
     */
    public ActivityLog(String entityType, Integer entityId, String activityType, String description,
            LocalDateTime createdAt) {
        this.entityType = entityType;
        this.entityId = entityId;
        this.activityType = activityType;
        this.description = description;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public Integer getEntityId() {
        return entityId;
    }

    public void setEntityId(Integer entityId) {
        this.entityId = entityId;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }


    

}
