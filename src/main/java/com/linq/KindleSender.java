package com.linq;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.internet.MimeUtility;
import java.io.File;
import java.io.UnsupportedEncodingException;

/**
 * 发送邮件
 *
 * @author LinQ
 * @version 2014-06-17
 */
public class KindleSender {
    private static final Logger logger = LoggerFactory.getLogger(KindleSender.class);

    public void sendMobi(AppConfig config, String path) throws EmailException, UnsupportedEncodingException {
        if (!config.isSendToKindle())
            return;

        if (StringUtils.isBlank(path) || !new File(path).exists()) {
            logger.error("{} not exist", path);
            return;
        }

        EmailAttachment attachment = new EmailAttachment();
        attachment.setPath(path);
        attachment.setDisposition(EmailAttachment.ATTACHMENT);
        String name = FilenameUtils.getBaseName(path);
        attachment.setName(MimeUtility.encodeText(String.format("%s.mobi", name)));

        // Create the email message
        MultiPartEmail email = new MultiPartEmail();
        email.setHostName(config.getSmtp());
        email.setSmtpPort(config.getSmtpPort());
        email.setSSLOnConnect(config.isSsl());
        email.setAuthentication(config.getSendMail(), config.getSendPassword());
        email.addTo(config.getKindleAddress(), "kindle");
        email.setFrom(config.getSendMail(), config.getSendMail());
        email.setSubject("nice");
        email.setCharset("utf-8");
        email.setMsg("mobi");

        // add the attachment
        email.attach(attachment);

        logger.info("start to send mail");

        // send the email
        email.send();

        logger.info("send {} to kindle success", name);
    }
}
