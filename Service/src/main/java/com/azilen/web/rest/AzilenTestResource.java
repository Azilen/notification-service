package com.azilen.web.rest;

import com.azilen.web.rest.util.HeaderUtil;
import io.micrometer.core.annotation.Timed;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Slf4j
public class AzilenTestResource {

    @GetMapping("/test")
    @Timed
    public ResponseEntity<?> test() {
        log.info("REST test request....");
        return ResponseEntity.ok().headers(HeaderUtil.createAlert("test", null)).build();
    }
}
