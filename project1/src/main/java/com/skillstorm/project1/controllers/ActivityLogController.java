package com.skillstorm.project1.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skillstorm.project1.models.ActivityLog;
import com.skillstorm.project1.services.ActivityLogService;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@CrossOrigin
@RestController
@RequestMapping("/activity")
public class ActivityLogController {

    private ActivityLogService activityLogService;
    public ActivityLogController(ActivityLogService activityLogService) {
        this.activityLogService = activityLogService;
    }

    @GetMapping
    public List<ActivityLog> getRecentActivity() {
        return activityLogService.getRecentActivity(20);
    }
    
}
