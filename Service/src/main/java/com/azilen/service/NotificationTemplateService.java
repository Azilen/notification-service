package com.azilen.service;

import com.azilen.common.enums.NotificationEventType;
import com.azilen.common.vm.NotificationTemplateVM;
import com.azilen.domain.NotificationTemplate;
import com.azilen.repository.NotificationTemplateRepository;
import com.azilen.service.mapper.NotificationTemplateMapper;
import com.azilen.web.rest.errors.BadRequestAlertException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.StringEscapeUtils;
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
public class NotificationTemplateService {
    @Autowired
    private NotificationTemplateRepository notificationTemplateRepository;

    @Autowired
    private NotificationTemplateMapper notificationTemplateMapper;

    public NotificationTemplate save(NotificationTemplateVM notificationTemplateVM) {
        NotificationTemplate notificationTemplate = notificationTemplateMapper.notificationTemplateVMtoNotificationTemplate(notificationTemplateVM);
        return notificationTemplateRepository.save(notificationTemplate);
    }

    public NotificationTemplate update(Long id, NotificationTemplateVM notificationTemplateVM) {
        Optional<NotificationTemplate> notificationTemplateOptional = notificationTemplateRepository.findById(id);

        if (notificationTemplateOptional.isEmpty()) {
            throw new BadRequestAlertException("Notification Template with given id not exists", "notificationTemplateManagement", "notExists");
        }

        NotificationTemplate notificationTemplate = notificationTemplateOptional.get();
        BeanUtils.copyProperties(notificationTemplateVM, notificationTemplate, "id");
        String escapeContent = StringEscapeUtils.escapeHtml4(notificationTemplateVM.getContent());
        notificationTemplate.setContent(escapeContent);

        notificationTemplate.setVersion(notificationTemplate.getVersion() + 1);

        return notificationTemplateRepository.save(notificationTemplate);
    }

    @Transactional(readOnly = true)
    public NotificationTemplate get(Long id) {
        return notificationTemplateRepository.findById(id).
            orElseThrow(() -> new BadRequestAlertException("Notification Template with given id not exists", "notificationTemplateManagement", "notFound"));
    }

    @Transactional(readOnly = true)
    public Page<NotificationTemplate> getAll(Specification<NotificationTemplate> specification, Pageable pageable) {
        return notificationTemplateRepository.findAll(specification, pageable);
    }

    public NotificationTemplate findOneByEventTypeAndSubEventTypeAndIsGlobal(NotificationEventType eventType, String subEventType, Boolean isGlobal) {
        return notificationTemplateRepository.findOneByEventTypeAndSubEventTypeAndIsGlobal(eventType, subEventType, isGlobal);
    }
}
