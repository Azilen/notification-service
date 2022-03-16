package com.azilen.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;

/**
 * A DTO representing a com.azilen.common DTO.
 */
@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseDTO implements Serializable {

    private static final long serialVersionUID = 293627322722830650L;

    public BaseDTO(Long id) {
        this.id = id;
    }

    protected Long id;
    protected Boolean deleted;
    protected Boolean enabled;
    protected String createdBy;
    protected Instant createdDate;
    protected String createdDateFormatted;
    protected String lastModifiedBy;
    protected Instant lastModifiedDate;

}
