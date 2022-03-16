package com.azilen.service;

import com.azilen.domain.NotificationHistory;
import com.azilen.repository.NotificationHistoryRepository;
import com.azilen.web.rest.errors.BadRequestAlertException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class NotificationHistoryService {
    @Autowired
    private NotificationHistoryRepository notificationHistoryRepository;

    public NotificationHistory save(NotificationHistory notificationHistory) {
        return notificationHistoryRepository.save(notificationHistory);
    }

    @Transactional(readOnly = true)
    public NotificationHistory get(Long id) {
        return notificationHistoryRepository.findById(id).
            orElseThrow(() -> new BadRequestAlertException("Notification History with given id not exists", "notificationHistoryManagement", "notFound"));
    }

    @Transactional(readOnly = true)
    public Page<NotificationHistory> getAll(Specification<NotificationHistory> specification, Pageable pageable) {
        return notificationHistoryRepository.findAll(specification, pageable);
    }
}
