package com.azilen.repository;

import com.azilen.domain.NotificationHistory;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationHistoryRepository extends BaseRepository<NotificationHistory, Long> {
}
