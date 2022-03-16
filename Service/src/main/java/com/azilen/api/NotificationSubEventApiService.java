package com.azilen.api;

import com.azilen.common.dto.NotificationSubEventDTO;
import com.azilen.common.vm.NotificationSubEventVM;
import com.azilen.domain.NotificationSubEvent;
import com.azilen.service.NotificationSubEventService;
import com.azilen.service.mapper.NotificationSubEventMapper;
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
public class NotificationSubEventApiService {
    @Autowired
    private NotificationSubEventService notificationSubEventService;

    @Autowired
    private NotificationSubEventMapper notificationSubEventMapper;

    public NotificationSubEventDTO save(NotificationSubEventVM notificationSubEventVM) {
        return notificationSubEventMapper.notificationSubEventToNotificationSubEventDTO(notificationSubEventService.save(notificationSubEventVM));
    }

    public NotificationSubEventDTO update(Long id, NotificationSubEventVM notificationSubEventVM) {
        return notificationSubEventMapper.notificationSubEventToNotificationSubEventDTO(notificationSubEventService.update(id, notificationSubEventVM));
    }

    @Transactional(readOnly = true)
    public NotificationSubEventDTO get(Long id) {
        return notificationSubEventMapper.notificationSubEventToNotificationSubEventDTO(notificationSubEventService.get(id));
    }

    @Transactional(readOnly = true)
    public Page<NotificationSubEventDTO> getAll(Map<String, List<Object>> matrixVars, Pageable pageable) {
        Map<String, List<Object>> matrix = Maps.newHashMap(matrixVars);

        Page<NotificationSubEvent> all;

        if (MatrixParamUtil.isFalse(matrix, IgnoreMatrixParam.pagination.name())) {
            all = notificationSubEventService.getAll(new MatrixAzilenSpecificationsBuilder<NotificationSubEvent>(matrix, NotificationSubEvent.class).build(), Pageable.unpaged());
        } else {
            all = notificationSubEventService.getAll(new MatrixAzilenSpecificationsBuilder<NotificationSubEvent>(matrix, NotificationSubEvent.class).build(), pageable);
        }

        return all.map(notificationSubEventMapper::notificationSubEventToNotificationSubEventDTO);
    }

    public void delete(Long id) {
        notificationSubEventService.delete(id);
    }
}
