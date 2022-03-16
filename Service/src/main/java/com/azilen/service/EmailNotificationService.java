package com.azilen.service;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.pinpointemail.AmazonPinpointEmail;
import com.amazonaws.services.pinpointemail.AmazonPinpointEmailClientBuilder;
import com.amazonaws.services.pinpointemail.model.Destination;
import com.amazonaws.services.pinpointemail.model.EmailContent;
import com.amazonaws.services.pinpointemail.model.RawMessage;
import com.amazonaws.services.pinpointemail.model.SendEmailRequest;
import com.azilen.common.vm.EmailNotificationVM;
import com.azilen.common.vm.NotificationAttachmentVM;
import com.azilen.common.vm.NotificationParamVM;
import com.azilen.common.vm.NotificationVM;
import com.azilen.config.ApplicationProperties;
import com.azilen.domain.NotificationAttachment;
import com.azilen.domain.NotificationHistory;
import com.azilen.domain.NotificationSubEvent;
import com.azilen.domain.NotificationTemplate;
import com.azilen.service.mapper.NotificationHistoryMapper;
import com.azilen.service.mapper.NotificationTemplateProcessor;
import com.azilen.utils.MimeUtil;
import com.azilen.web.rest.errors.BadRequestAlertException;
import com.azilen.web.rest.errors.SendNotificationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.pinpoint.PinpointClient;
import software.amazon.awssdk.services.pinpoint.model.*;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.URLDataSource;
import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

@Service
@Slf4j
public class EmailNotificationService {
    @Autowired
    ApplicationProperties applicationProperties;

    @Autowired
    NotificationHistoryMapper notificationHistoryMapper;

    @Autowired
    NotificationHistoryService notificationHistoryService;

    @Autowired
    NotificationTemplateService notificationTemplateService;

    @Autowired
    NotificationSubEventService notificationSubEventService;

    protected static PinpointClient pinpointClient;

    private static String charset = "UTF-8";
    private static AddressConfiguration configuration = AddressConfiguration.builder().channelType(ChannelType.EMAIL)
        .build();

    public void sendEmailNotifiation(NotificationVM notificationVM) throws SendNotificationException {
        try {

            NotificationSubEvent notificationSubEvent = notificationSubEventService.findBySubEventType(notificationVM.getSubNotificationEventType());

            ObjectMapper oMapper = new ObjectMapper();
            EmailNotificationVM emailNotificationVM = oMapper.convertValue(notificationVM.getNotificationVM(), EmailNotificationVM.class);
            for (String to : emailNotificationVM.getTo()) {
                Map<String, AddressConfiguration> toAddressMap = new HashMap<>();
                Map<String, AddressConfiguration> ccAddressMap = new HashMap<>();
                Map<String, AddressConfiguration> bccAddressMap = new HashMap<>();
                Map<String, Map<String, AddressConfiguration>> finalAddressMap = new HashMap<>();

                toAddressMap.put(to, configuration);

                if (CollectionUtils.isNotEmpty(emailNotificationVM.getCc())) {
                    for (String cc : emailNotificationVM.getCc()) {
                        ccAddressMap.put(cc, configuration);
                    }
                }

                if (CollectionUtils.isNotEmpty(emailNotificationVM.getBcc())) {
                    for (String bcc : emailNotificationVM.getBcc()) {
                        bccAddressMap.put(bcc, configuration);
                    }
                }

                finalAddressMap.put("to", toAddressMap);
                finalAddressMap.put("cc", ccAddressMap);
                finalAddressMap.put("bcc", bccAddressMap);

                String emailBody = null;
                int version = 0;

                NotificationTemplate notificationTemplate;
                if (notificationVM.getTemplateData() != null && !notificationVM.getTemplateData().isEmpty()) {
                    emailBody = notificationVM.getTemplateData();
                    version = (notificationVM.getVersion() != null ? notificationVM.getVersion().intValue() : 1);
                }

                if (emailBody == null) {
                    notificationTemplate = notificationTemplateService.findOneByEventTypeAndSubEventTypeAndIsGlobal(
                        notificationVM.getNotificationEventType(),
                        notificationVM.getSubNotificationEventType(),
                        true
                    );
                    if (notificationTemplate != null) {
                        emailBody = notificationTemplate.getContent();
                        version = notificationTemplate.getVersion();
                    }
                }

                if (emailBody == null) {
                    throw new BadRequestAlertException("No template data found to sent email.", "emailManagement", "notFound");
                }

                String emailBodyContent = NotificationTemplateProcessor.getInstance().processTemplate(emailBody, version, notificationSubEvent.getDisplayName(), notificationVM.getHtmlVaraiblesData(), notificationVM.getNotificationEventType().getDisplayName());

                //sendEmail(emailNotificationVM.getSubject(), emailBodyContent, toAddressMap);

                sendMimeMessage(notificationVM.getNotificationId(), emailNotificationVM.getSubject(), emailBodyContent, finalAddressMap, notificationVM.getAttachments());

                NotificationHistory notificationHistory = notificationHistoryMapper.notificationVMtoNotificationHistory(notificationVM);
                notificationHistory.setContent(emailBodyContent);
                notificationHistory.setFromAddress(StringUtils.isNotBlank(emailNotificationVM.getFrom()) ? emailNotificationVM.getFrom() : applicationProperties.getPinPointConfig().getEmailSender());
                notificationHistory.setToAddress(to);

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

                notificationHistoryService.save(notificationHistory);
            }

        } catch (PinpointException | IOException | TemplateException e) {
            log.error("email sent error email::{} ", notificationVM.getNotificationId(), e);
            throw new SendNotificationException("could not send notification", 801);
        }
    }

    public void getPinPointClient() {
        AwsCredentialsProvider credentialsProvider = StaticCredentialsProvider
            .create(AwsBasicCredentials.create(applicationProperties.getPinPointConfig().getAwsAccessKey(),
                applicationProperties.getPinPointConfig().getAwsSecretAccessKey()));

        pinpointClient = PinpointClient.builder().credentialsProvider(credentialsProvider)
            .region(Region.of(applicationProperties.getPinPointConfig().getAwsRegion())).build();
    }

    private void sendEmail(String subject, String content, Map<String, AddressConfiguration> addressMap) {
        SimpleEmailPart emailPart = SimpleEmailPart.builder().data(content)
            .charset(charset).build();

        SimpleEmailPart subjectPart = SimpleEmailPart.builder().data(subject)
            .charset(charset).build();

        SimpleEmail simpleEmail = SimpleEmail.builder().htmlPart(emailPart).subject(subjectPart).build();

        EmailMessage emailMessage = EmailMessage.builder().fromAddress(applicationProperties.getPinPointConfig().getEmailSender())
            .simpleEmail(simpleEmail).build();

        DirectMessageConfiguration directMessageConfiguration = DirectMessageConfiguration.builder()
            .emailMessage(emailMessage).build();

        MessageRequest messageRequest = MessageRequest.builder().addresses(addressMap)
            .messageConfiguration(directMessageConfiguration).build();

        SendMessagesRequest messagesRequest = SendMessagesRequest.builder()
            .applicationId(applicationProperties.getPinPointConfig().getProjectId()).messageRequest(messageRequest).build();

        getPinPointClient();

        pinpointClient.sendMessages(messagesRequest);
    }

    public void sendMimeMessage(String notificationId, String subject, String content, Map<String, Map<String, AddressConfiguration>> finalAddressMap, Set<NotificationAttachmentVM> attachments) {

        try {

            Map<String, AddressConfiguration> toAddressMap = finalAddressMap.get("to");
            Map<String, AddressConfiguration> ccAddressMap = finalAddressMap.get("cc");
            Map<String, AddressConfiguration> bccAddressMap = finalAddressMap.get("bcc");

            javax.mail.Session session = javax.mail.Session.getDefaultInstance(new Properties());
            MimeMessage message = new MimeMessage(session);

            message.setSubject(subject, charset);
            message.setFrom(new InternetAddress(applicationProperties.getPinPointConfig().getEmailSender()));

            for (String address : toAddressMap.keySet()) {
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(address));
            }

            for (String address : ccAddressMap.keySet()) {
                message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(address));
            }

            for (String address : bccAddressMap.keySet()) {
                message.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(address));
            }

            MimeMultipart msgBody = new MimeMultipart("alternative");
            MimeBodyPart wrap = new MimeBodyPart();

            MimeBodyPart htmlPart = new MimeBodyPart();
            htmlPart.setContent(content, "text/html; charset=UTF-8");
            msgBody.addBodyPart(htmlPart);
            wrap.setContent(msgBody);
            MimeMultipart msg = new MimeMultipart("mixed");
            message.setContent(msg);
            msg.addBodyPart(wrap);

            if (CollectionUtils.isNotEmpty(attachments)) {
                MimeBodyPart attachment;
                DataSource dataSource = null;

                for (NotificationAttachmentVM attachmentVM : attachments) {

                    if (StringUtils.isNotBlank(attachmentVM.getBase64()) && StringUtils.isNotBlank(attachmentVM.getFileName())) {
                        dataSource = new ByteArrayDataSource(Base64.decodeBase64(attachmentVM.getBase64()), MimeUtil.getMimeType(attachmentVM.getFileName()));
                    } else if (StringUtils.isNotBlank(attachmentVM.getUrl())) {
                        dataSource = new URLDataSource(new URL(attachmentVM.getUrl()));
                    }

                    if (dataSource != null) {
                        attachment = new MimeBodyPart();
                        attachment.setDataHandler(new DataHandler(dataSource));
                        attachment.setFileName(StringUtils.isNotBlank(attachmentVM.getFileName()) ? attachmentVM.getFileName() : dataSource.getName());
                        msg.addBodyPart(attachment);
                    }
                }
            }

            AWSCredentialsProvider awsCredentialsProvider = new AWSStaticCredentialsProvider(new BasicAWSCredentials(
                applicationProperties.getPinPointConfig().getAwsAccessKey(),
                applicationProperties.getPinPointConfig().getAwsSecretAccessKey()));

            AmazonPinpointEmail client = AmazonPinpointEmailClientBuilder.standard().withCredentials(awsCredentialsProvider)
                .withRegion(Regions.fromName(applicationProperties.getPinPointConfig().getAwsRegion())).build();

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            message.writeTo(outputStream);

            SendEmailRequest rawEmailRequest = new SendEmailRequest()
                .withFromEmailAddress(applicationProperties.getPinPointConfig().getEmailSender())
                .withDestination(new Destination()
                    .withToAddresses(toAddressMap.keySet())
                    .withCcAddresses(ccAddressMap.keySet())
                    .withBccAddresses(bccAddressMap.keySet())
                )
                .withContent(new EmailContent()
                    .withRaw(new RawMessage().withData(ByteBuffer.wrap(outputStream.toByteArray())))
                );

            client.sendEmail(rawEmailRequest);
        } catch (Exception ex) {
            log.error("Failed to send email with notificationId {} due to {}", notificationId, ex.getMessage(), ex);
        }
    }
}
