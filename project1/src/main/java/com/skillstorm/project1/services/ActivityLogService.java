package com.skillstorm.project1.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.skillstorm.project1.models.ActivityLog;
import com.skillstorm.project1.repositories.ActivityLogRepository;

@Service
public class ActivityLogService {


    private final ActivityLogRepository activityLogRepository;
    public ActivityLogService(ActivityLogRepository activityLogRepository) {
        this.activityLogRepository = activityLogRepository;
    }

    public void log(String activityType, String entityType, Integer entityId, String description) {
        ActivityLog log = new ActivityLog();
        log.setActivityType(activityType);
        log.setEntityType(entityType);
        log.setEntityId(entityId);
        log.setDescription(description);
        log.setCreatedAt(LocalDateTime.now());
        activityLogRepository.save(log);
    }

    public List<ActivityLog> getRecentActivity(int limit) {
        return activityLogRepository.findTop20ByOrderByCreatedAtDesc();
    }

}
