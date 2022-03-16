package com.azilen.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "az_notification_attachment")
@Getter
@Setter
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Cacheable
public class NotificationAttachment extends AbstractAuditingEntity implements Serializable {
    private static final long serialVersionUID = 6736798657266184591L;

    public NotificationAttachment() {
        super.setDeleted(false);
        super.setEnabled(true);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "mime_type")
    private String mimeType;

    @Column(name = "url", length = 1024)
    private String url;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "base64")
    private String base64;

}

