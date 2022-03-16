package com.azilen.web.rest;

import com.azilen.api.NotificationHistoryApiService;
import com.azilen.common.dto.NotificationHistoryDTO;
import com.azilen.web.rest.util.PaginationUtil;
import com.azilen.web.rest.util.ResponseUtil;
import io.micrometer.core.annotation.Timed;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@Slf4j
public class NotificationHistoryResource {
    @Autowired
    private NotificationHistoryApiService notificationHistoryApiService;

    @GetMapping("/notificationHistory{matrixVars}")
    @Timed
    public ResponseEntity<List<NotificationHistoryDTO>> getAll(@MatrixVariable Map<String, List<Object>> matrixVars, Pageable pageable) {
        log.debug("REST request to get all notification history {}", matrixVars);
        final Page<NotificationHistoryDTO> page = notificationHistoryApiService.getAll(matrixVars, pageable);
        List<NotificationHistoryDTO> instances = page.getContent();
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/notificationHistory");

        return new ResponseEntity<>(instances, headers, HttpStatus.OK);
    }

    @GetMapping("/notificationHistory/{id}")
    @Timed
    public ResponseEntity<NotificationHistoryDTO> get(@PathVariable("id") Long id) {
        log.info("REST request to get notification history for id: {}", id);
        return ResponseUtil.wrapOrNotFound(notificationHistoryApiService.get(id));
    }
}
