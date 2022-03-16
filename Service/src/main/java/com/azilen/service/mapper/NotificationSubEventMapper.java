package com.azilen.service.mapper;

import com.azilen.common.dto.NotificationSubEventDTO;
import com.azilen.common.vm.NotificationSubEventVM;
import com.azilen.domain.NotificationSubEvent;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class NotificationSubEventMapper {
    public NotificationSubEvent notificationSubEventVMtoNotificationSubEvent(NotificationSubEventVM notificationSubEventVM) {
        if (notificationSubEventVM == null) {
            return null;
        }
        NotificationSubEvent notificationSubEvent = new NotificationSubEvent();
        BeanUtils.copyProperties(notificationSubEventVM, notificationSubEvent);
        notificationSubEvent.setSubEventType(notificationSubEventVM.getSubEventType().trim());

        return notificationSubEvent;
    }

    public NotificationSubEventDTO notificationSubEventToNotificationSubEventDTO(NotificationSubEvent notificationSubEvent) {
        if (notificationSubEvent == null) {
            return null;
        }
        NotificationSubEventDTO notificationSubEventDTO = new NotificationSubEventDTO();
        BeanUtils.copyProperties(notificationSubEvent, notificationSubEventDTO);

        return notificationSubEventDTO;
    }
}
