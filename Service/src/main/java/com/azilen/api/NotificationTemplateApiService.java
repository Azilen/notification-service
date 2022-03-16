package com.azilen.api;

import com.azilen.common.dto.NotificationTemplateDTO;
import com.azilen.common.vm.NotificationTemplateVM;
import com.azilen.domain.NotificationTemplate;
import com.azilen.service.NotificationTemplateService;
import com.azilen.service.mapper.NotificationTemplateMapper;
import com.azilen.specification.IgnoreMatrixParam;
import com.azilen.specification.MatrixAzilenSpecificationsBuilder;
import com.azilen.specification.MatrixParamUtil;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
@Slf4j
public class NotificationTemplateApiService {
    @Autowired
    private NotificationTemplateService notificationTemplateService;

    @Autowired
    private NotificationTemplateMapper notificationTemplateMapper;

    public NotificationTemplateDTO save(NotificationTemplateVM notificationTemplateVM) {
        return notificationTemplateMapper.notificationTemplatetoNotificationTemplateDTO(notificationTemplateService.save(notificationTemplateVM));
    }

    public NotificationTemplateDTO update(Long id, NotificationTemplateVM notificationTemplateVM) {
        return notificationTemplateMapper.notificationTemplatetoNotificationTemplateDTO(notificationTemplateService.update(id, notificationTemplateVM));
    }

    @Transactional(readOnly = true)
    public NotificationTemplateDTO get(Long id) {
        return notificationTemplateMapper.notificationTemplatetoNotificationTemplateDTO(notificationTemplateService.get(id));
    }

    @Transactional(readOnly = true)
    public Page<NotificationTemplateDTO> getAll(Map<String, List<Object>> matrixVars, Pageable pageable) {
        Map<String, List<Object>> matrix = Maps.newHashMap(matrixVars);

        Page<NotificationTemplate> all;

        if (MatrixParamUtil.isFalse(matrix, IgnoreMatrixParam.pagination.name())) {
            all = notificationTemplateService.getAll(new MatrixAzilenSpecificationsBuilder<NotificationTemplate>(matrix, NotificationTemplate.class).build(), Pageable.unpaged());
        } else {
            all = notificationTemplateService.getAll(new MatrixAzilenSpecificationsBuilder<NotificationTemplate>(matrix, NotificationTemplate.class).build(), pageable);
        }

        return all.map(notificationTemplateMapper::notificationTemplatetoNotificationTemplateDTO);
    }
}
