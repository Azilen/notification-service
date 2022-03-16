package com.azilen.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "az_notification_sub_event_type")
@Getter
@Setter
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Cacheable
public class NotificationSubEvent extends AbstractAuditingEntity implements Serializable {
    private static final long serialVersionUID = 6736798657266184592L;

    public NotificationSubEvent() {
        super.setDeleted(false);
        super.setEnabled(true);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "sub_event_type")
    private String subEventType;

    @Column(name = "display_name")
    private String displayName;

}

