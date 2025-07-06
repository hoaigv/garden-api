
package com.example.demo.notification.repository;

import com.example.demo.notification.model.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Repository for NotificationEntity supporting CRUD and Specifications.
 */
@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, String>,
        JpaSpecificationExecutor<NotificationEntity> {
    // Bạn có thể thêm các phương thức query tuỳ chỉnh ở đây
}