package com.azilen.api;

import com.azilen.common.dto.NotificationHistoryDTO;
import com.azilen.domain.NotificationHistory;
import com.azilen.service.NotificationHistoryService;
import com.azilen.service.mapper.NotificationHistoryMapper;
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
public class NotificationHistoryApiService {
    @Autowired
    private NotificationHistoryService notificationHistoryService;

    @Autowired
    private NotificationHistoryMapper notificationHistoryMapper;

    @Transactional(readOnly = true)
    public NotificationHistoryDTO get(Long id) {
        return notificationHistoryMapper.notificationHistoryToNotificationHistoryDTO(notificationHistoryService.get(id));
    }

    @Transactional(readOnly = true)
    public Page<NotificationHistoryDTO> getAll(Map<String, List<Object>> matrixVars, Pageable pageable) {
        Map<String, List<Object>> matrix = Maps.newHashMap(matrixVars);

        Page<NotificationHistory> all;

        if (MatrixParamUtil.isFalse(matrix, IgnoreMatrixParam.pagination.name())) {
            all = notificationHistoryService.getAll(new MatrixAzilenSpecificationsBuilder<NotificationHistory>(matrix, NotificationHistory.class).build(), Pageable.unpaged());
        } else {
            all = notificationHistoryService.getAll(new MatrixAzilenSpecificationsBuilder<NotificationHistory>(matrix, NotificationHistory.class).build(), pageable);
        }

        return all.map(notificationHistoryMapper::notificationHistoryToNotificationHistoryDTO);
    }
}
