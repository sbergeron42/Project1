package com.skillstorm.project1.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.skillstorm.project1.models.ActivityLog;

/**
 * This is a repository for accessing activity log records.
 * Provides methods for retrieving recent activity entries
 */
@Repository
public interface ActivityLogRepository extends JpaRepository<ActivityLog, Integer> {

    /**
     * Retrieves the 20 most recent activity log entries, ordered by timestamp descending.
     * @return a list of the most recent 20 ActivityLog records
     */
    List<ActivityLog> findTop20ByOrderByCreatedAtDesc();
}
