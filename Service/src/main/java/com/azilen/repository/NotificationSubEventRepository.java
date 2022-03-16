package com.azilen.repository;

import com.azilen.domain.NotificationSubEvent;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NotificationSubEventRepository extends BaseRepository<NotificationSubEvent, Long> {
    Optional<NotificationSubEvent> findBySubEventType(String subEventType);
}
