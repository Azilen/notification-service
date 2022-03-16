package com.azilen.web.rest;

import com.azilen.api.NotificationSubEventApiService;
import com.azilen.common.dto.NotificationSubEventDTO;
import com.azilen.common.vm.NotificationSubEventVM;
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
public class NotificationSubEventResource {
    @Autowired
    private NotificationSubEventApiService notificationSubEventApiService;

    @PostMapping("/notificationSubEvent")
    @Timed
    public ResponseEntity<NotificationSubEventDTO> create(@Valid @RequestBody NotificationSubEventVM notificationSubEventVM) throws URISyntaxException {
        log.debug("REST request to save notification SubEvent : {}", notificationSubEventVM);

        NotificationSubEventDTO notificationSubEventDTO = notificationSubEventApiService.save(notificationSubEventVM);

        return ResponseEntity.created(new URI("/api/notificationSubEvent/" + notificationSubEventDTO.getId()))
            .headers(HeaderUtil.createAlert("notificationSubEventManagement.created", notificationSubEventVM.getSubEventType()))
            .body(notificationSubEventDTO);
    }

    @PutMapping("/notificationSubEvent/{id}")
    @Timed
    public ResponseEntity<NotificationSubEventDTO> update(@PathVariable("id") Long id, @Valid @RequestBody NotificationSubEventVM notificationSubEventVM) {

        log.info("REST request to update notification SubEvent for id: {}", id);

        NotificationSubEventDTO notificationSubEventDTO = notificationSubEventApiService.update(id, notificationSubEventVM);

        return ResponseUtil.wrapOrNotFound(notificationSubEventDTO,
            HeaderUtil.createAlert("notificationSubEventManagement.updated", notificationSubEventVM.getSubEventType()));
    }

    @GetMapping("/notificationSubEvent{matrixVars}")
    @Timed
    public ResponseEntity<List<NotificationSubEventDTO>> getAll(@MatrixVariable Map<String, List<Object>> matrixVars, Pageable pageable) {
        log.debug("REST request to get all notification SubEvents {}", matrixVars);
        final Page<NotificationSubEventDTO> page = notificationSubEventApiService.getAll(matrixVars, pageable);
        List<NotificationSubEventDTO> instances = page.getContent();
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/notificationSubEvent");

        return new ResponseEntity<>(instances, headers, HttpStatus.OK);
    }

    @GetMapping("/notificationSubEvent/{id}")
    @Timed
    public ResponseEntity<NotificationSubEventDTO> get(@PathVariable("id") Long id) {
        log.info("REST request to get notification SubEvent for id: {}", id);
        return ResponseUtil.wrapOrNotFound(notificationSubEventApiService.get(id));
    }

    @DeleteMapping("/notificationSubEvent/{id}")
    @Timed
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {

        log.info("REST request to delete ats Notification SubEvent for id: {}", id);

        notificationSubEventApiService.delete(id);

        return ResponseEntity.ok().headers(HeaderUtil.createAlert("notificationSubEventManagement.deleted", null)).build();
    }
}
