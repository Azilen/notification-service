package com.azilen.web.rest;

import com.azilen.api.NotificationTemplateApiService;
import com.azilen.common.dto.NotificationTemplateDTO;
import com.azilen.common.vm.NotificationTemplateVM;
import com.azilen.web.rest.util.HeaderUtil;
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

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@Slf4j
public class NotificationTemplateResource {
    @Autowired
    private NotificationTemplateApiService notificationTemplateApiService;

    @PostMapping("/notificationTemplate")
    @Timed
    public ResponseEntity<NotificationTemplateDTO> create(@Valid @RequestBody NotificationTemplateVM notificationTemplateVM) throws URISyntaxException {
        log.debug("REST request to save notification template : {}", notificationTemplateVM);

        NotificationTemplateDTO notificationTemplateDTO = notificationTemplateApiService.save(notificationTemplateVM);

        return ResponseEntity.created(new URI("/api/notificationTemplate/" + notificationTemplateDTO.getId()))
            .headers(HeaderUtil.createAlert("notificationTemplateManagement.created", notificationTemplateVM.getSubEventType()))
            .body(notificationTemplateDTO);
    }

    @PutMapping("/notificationTemplate/{id}")
    @Timed
    public ResponseEntity<NotificationTemplateDTO> update(@PathVariable("id") Long id, @Valid @RequestBody NotificationTemplateVM notificationTemplateVM) {

        log.info("REST request to update notification template for id: {}", id);

        NotificationTemplateDTO notificationTemplateDTO = notificationTemplateApiService.update(id, notificationTemplateVM);

        return ResponseUtil.wrapOrNotFound(notificationTemplateDTO,
            HeaderUtil.createAlert("notificationTemplateManagement.updated", notificationTemplateVM.getSubEventType()));
    }

    @GetMapping("/notificationTemplate{matrixVars}")
    @Timed
    public ResponseEntity<List<NotificationTemplateDTO>> getAll(@MatrixVariable Map<String, List<Object>> matrixVars, Pageable pageable) {
        log.debug("REST request to get all notification templates {}", matrixVars);
        final Page<NotificationTemplateDTO> page = notificationTemplateApiService.getAll(matrixVars, pageable);
        List<NotificationTemplateDTO> instances = page.getContent();
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/notificationTemplate");

        return new ResponseEntity<>(instances, headers, HttpStatus.OK);
    }

    @GetMapping("/notificationTemplate/{id}")
    @Timed
    public ResponseEntity<NotificationTemplateDTO> get(@PathVariable("id") Long id) {
        log.info("REST request to get notification template for id: {}", id);
        return ResponseUtil.wrapOrNotFound(notificationTemplateApiService.get(id));
    }
}
