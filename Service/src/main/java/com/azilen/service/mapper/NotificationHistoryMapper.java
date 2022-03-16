package com.azilen.service.mapper;

import com.azilen.common.dto.NotificationHistoryDTO;
import com.azilen.common.vm.NotificationAttachmentVM;
import com.azilen.common.vm.NotificationParamVM;
import com.azilen.common.vm.NotificationVM;
import com.azilen.domain.NotificationAttachment;
import com.azilen.domain.NotificationHistory;
import com.azilen.utils.MimeUtil;
import com.google.common.collect.Sets;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class NotificationHistoryMapper {
    public NotificationHistory notificationVMtoNotificationHistory(NotificationVM notificationVM) {
        if (notificationVM == null) {
            return null;
        }
        NotificationHistory notificationHistory = new NotificationHistory();
        notificationHistory.setNotificationId(notificationVM.getNotificationId());
        notificationHistory.setEventType(notificationVM.getNotificationEventType());
        notificationHistory.setSubEventType(notificationVM.getSubNotificationEventType());

        if (CollectionUtils.isNotEmpty(notificationVM.getExtras())) {
            StringBuilder parameters = new StringBuilder();
            for (NotificationParamVM extraVm : notificationVM.getExtras()) {
                parameters.append("__").append(extraVm.getKey()).append("__").append(extraVm.getValue());
            }

            notificationHistory.setParameters(parameters.toString());
        }

        if (CollectionUtils.isNotEmpty(notificationVM.getAttachments())) {
            Set<NotificationAttachment> notificationAttachments = Sets.newHashSet();
            for (NotificationAttachmentVM attachmentVm : notificationVM.getAttachments()) {
                NotificationAttachment notificationAttachment = new NotificationAttachment();
                notificationAttachment.setFileName(attachmentVm.getFileName());
                notificationAttachment.setUrl(attachmentVm.getUrl());
                notificationAttachment.setBase64(attachmentVm.getBase64());
                notificationAttachment.setMimeType(MimeUtil.getMimeType(attachmentVm.getFileName()));
                notificationAttachments.add(notificationAttachment);
            }

            notificationHistory.setAttachments(notificationAttachments);
        }

        return notificationHistory;
    }

    public NotificationHistoryDTO notificationHistoryToNotificationHistoryDTO(NotificationHistory notificationHistory) {
        if (notificationHistory == null) {
            return null;
        }

        NotificationHistoryDTO notificationHistoryDTO = new NotificationHistoryDTO();
        BeanUtils.copyProperties(notificationHistory, notificationHistoryDTO);

        if (CollectionUtils.isNotEmpty(notificationHistory.getAttachments())) {
            Set<NotificationAttachmentVM> notificationAttachments = Sets.newHashSet();
            for (NotificationAttachment attachment : notificationHistory.getAttachments()) {
                NotificationAttachmentVM notificationAttachmentVM = new NotificationAttachmentVM();
                notificationAttachmentVM.setFileName(attachment.getFileName());
                notificationAttachmentVM.setUrl(attachment.getUrl());
                notificationAttachmentVM.setBase64(attachment.getBase64());
                notificationAttachments.add(notificationAttachmentVM);
            }

            notificationHistoryDTO.setAttachments(notificationAttachments);
        }

        return notificationHistoryDTO;

    }
}
