package com.azilen.service;

import com.azilen.common.vm.NotificationSubEventVM;
import com.azilen.domain.NotificationSubEvent;
import com.azilen.repository.NotificationSubEventRepository;
import com.azilen.service.mapper.NotificationSubEventMapper;
import com.azilen.web.rest.errors.BadRequestAlertException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@Slf4j
public class NotificationSubEventService {
    @Autowired
    private NotificationSubEventRepository notificationSubEventRepository;

    @Autowired
    private NotificationSubEventMapper notificationSubEventMapper;

    public NotificationSubEvent save(NotificationSubEventVM notificationSubEventVM) {
        NotificationSubEvent notificationSubEvent = notificationSubEventMapper.notificationSubEventVMtoNotificationSubEvent(notificationSubEventVM);
        return notificationSubEventRepository.save(notificationSubEvent);
    }

    public NotificationSubEvent update(Long id, NotificationSubEventVM notificationSubEventVM) {
        Optional<NotificationSubEvent> notificationSubEventOptional = notificationSubEventRepository.findById(id);

        if (notificationSubEventOptional.isEmpty()) {
            throw new BadRequestAlertException("Notification SubEvent with given id not exists", "notificationSubEventManagement", "notExists");
        }

        NotificationSubEvent notificationSubEvent = notificationSubEventOptional.get();
        BeanUtils.copyProperties(notificationSubEventVM, notificationSubEvent, "id");
        notificationSubEvent.setSubEventType(notificationSubEventVM.getSubEventType().trim());

        return notificationSubEventRepository.save(notificationSubEvent);
    }

    @Transactional(readOnly = true)
    public NotificationSubEvent get(Long id) {
        return notificationSubEventRepository.findById(id).
            orElseThrow(() -> new BadRequestAlertException("Notification SubEvent with given id not exists", "notificationSubEventManagement", "notFound"));
    }

    @Transactional(readOnly = true)
    public NotificationSubEvent findBySubEventType(String subEventType) {
        return notificationSubEventRepository.findBySubEventType(subEventType.trim()).
            orElseThrow(() -> new BadRequestAlertException("Notification SubEvent with given type [ " + subEventType + " ] not exists", "notificationSubEventManagement", "notFound"));
    }

    @Transactional(readOnly = true)
    public Page<NotificationSubEvent> getAll(Specification<NotificationSubEvent> specification, Pageable pageable) {
        return notificationSubEventRepository.findAll(specification, pageable);
    }

    public void delete(Long id) {
        notificationSubEventRepository.deleteById(id);
    }

}
