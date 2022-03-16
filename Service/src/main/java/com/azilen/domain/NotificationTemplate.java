package com.azilen.domain;

import com.azilen.common.enums.NotificationEventType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name = "az_notification_template")
@Getter
@Setter
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Cacheable
public class NotificationTemplate extends AbstractAuditingEntity implements Serializable {
    private static final long serialVersionUID = 6736798657266184590L;

    public NotificationTemplate() {
        super.setDeleted(false);
        super.setEnabled(true);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_type", columnDefinition = "text")
    private NotificationEventType eventType;

    //    @Enumerated(EnumType.STRING)
    @Column(name = "sub_event_type", columnDefinition = "text")
    private String subEventType;

    @Size(max = 65535)
    @Column(name = "content", length = 65535, columnDefinition = "text")
    private String content;

    @Column
    private int version = 0;

    @Column(name = "is_global")
    private Boolean isGlobal = false;
}
