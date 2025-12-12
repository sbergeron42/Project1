package com.skillstorm.project1.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skillstorm.project1.models.ActivityLog;
import com.skillstorm.project1.services.ActivityLogService;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * This controller is responsible for retrieving recent system activity logs.
 * It provides endpoints for fetching the latest recorded user and system activities
 */
@CrossOrigin
@RestController
@RequestMapping("/activity")
public class ActivityLogController {

    private ActivityLogService activityLogService;
    public ActivityLogController(ActivityLogService activityLogService) {
        this.activityLogService = activityLogService;
    }

    // Gets the most recent activities, up to 20
    @GetMapping
    public List<ActivityLog> getRecentActivity() {
        return activityLogService.getRecentActivity(20);
    }
    
}
