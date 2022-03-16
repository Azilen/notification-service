package com.azilen.repository;

import com.azilen.common.enums.NotificationEventType;
import com.azilen.domain.NotificationTemplate;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationTemplateRepository extends BaseRepository<NotificationTemplate, Long> {
    NotificationTemplate findOneByEventTypeAndSubEventTypeAndIsGlobal(NotificationEventType eventType, String subEventType, Boolean isGlobal);
}
