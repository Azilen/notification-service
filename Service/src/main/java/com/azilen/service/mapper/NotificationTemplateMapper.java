package com.azilen.service.mapper;

import com.azilen.common.dto.NotificationTemplateDTO;
import com.azilen.common.vm.NotificationTemplateVM;
import com.azilen.domain.NotificationTemplate;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class NotificationTemplateMapper {
    public NotificationTemplate notificationTemplateVMtoNotificationTemplate(NotificationTemplateVM notificationTemplateVM) {
        if(notificationTemplateVM == null) {
            return null;
        }
        NotificationTemplate notificationTemplate = new NotificationTemplate();
        BeanUtils.copyProperties(notificationTemplateVM, notificationTemplate);
        String escapeContent = StringEscapeUtils.escapeHtml4(notificationTemplateVM.getContent());
        notificationTemplate.setContent(escapeContent);

        return notificationTemplate;
    }

    public NotificationTemplateDTO notificationTemplatetoNotificationTemplateDTO(NotificationTemplate notificationTemplate) {
        if(notificationTemplate == null) {
            return null;
        }
        NotificationTemplateDTO notificationTemplateDTO = new NotificationTemplateDTO();
        BeanUtils.copyProperties(notificationTemplate, notificationTemplateDTO);

        return notificationTemplateDTO;
    }
}
