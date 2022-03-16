package com.azilen.domain;

import com.azilen.common.enums.NotificationEventType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "az_notification_history")
@Getter
@Setter
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Cacheable
public class NotificationHistory extends AbstractAuditingEntity implements Serializable {
    private static final long serialVersionUID = 6736798657266184590L;

    public NotificationHistory() {
        super.setDeleted(false);
        super.setEnabled(true);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Size(max = 100)
    @Column(name = "notification_id", length = 100, columnDefinition = "text")
    private String notificationId;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_type", columnDefinition = "text")
    private NotificationEventType eventType;

    //    @Enumerated(EnumType.STRING)
    @Column(name = "sub_event_type", columnDefinition = "text")
    private String subEventType;

    @Size(max = 65535)
    @Column(name = "content", length = 65535, columnDefinition = "text")
    private String content;

    @Size(max = 50)
    @Column(name = "from_address", length = 50, columnDefinition = "text")
    private String fromAddress;

    @Size(max = 50)
    @Column(name = "to_address", length = 50, columnDefinition = "text")
    private String toAddress;

    @OneToMany(cascade = CascadeType.ALL)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "az_notification_history_attachment", joinColumns = @JoinColumn(name = "notification_history_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "attachment_id", referencedColumnName = "id"))
    private Set<NotificationAttachment> attachments;

    @Column(name = "parameters", length = 4000, columnDefinition = "text")
    private String parameters;

}

