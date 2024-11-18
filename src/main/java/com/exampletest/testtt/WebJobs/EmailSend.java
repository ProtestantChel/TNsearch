package com.exampletest.testtt.WebJobs;

import com.exampletest.testtt.Configurations.VariablesProperties;
import com.sun.mail.smtp.SMTPTransport;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import javax.mail.*;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.*;
import java.io.IOException;
import java.util.*;
@Slf4j
public class EmailSend {
    public void mailSend(String send, Logger logger, VariablesProperties mcfg, String title) {
        try {
            logger.info((new Date()) + "---- Email-send ---");
            logger.info((new Date()) + " " + mcfg.toString());
            Properties props = System.getProperties();
            props.put("mail.smtps.host", mcfg.getMailHost().trim());
            props.put("mail.smtps.port", mcfg.getMailPort().trim());
            props.put("mail.smtps.auth", mcfg.getMailSmtpAuth().trim());
            Session session = Session.getInstance(props, null);
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(mcfg.getMailFromEmail().trim()));
            msg.setSubject(title);
            msg.setText(send);
            msg.setHeader("Content-Transfer-Encoding", "base64");
            msg.setSentDate(new Date());
            SMTPTransport t =
                    (SMTPTransport) session.getTransport("smtps");
            try {
                t.connect(mcfg.getMailHost().trim(), mcfg.getMailUser().trim(), mcfg.getMailPassword().trim());
            } catch (AuthenticationFailedException authenticationFailedException) {
                logger.info("Не удалось отправить почту. Ошибка авторизации. Проверьте правильность введенных данных: email отправителя, логин и пароль.");
            }
            String[] mailList = mcfg.getMailToEmail().trim().split(",");
            for (String email : mailList) {
                try {
                    msg.setRecipients(Message.RecipientType.CC,
                            InternetAddress.parse(email.trim(), false));
                    t.sendMessage(msg, msg.getAllRecipients());
                    logger.info("Response " + email + ": " + t.getLastServerResponse());
                } catch (SendFailedException sendFailedException) {
                    logger.info("Не удалось отправить почту. Не верно указан Email " + email + "получателя.");
                } catch (IllegalStateException illegalStateException) {
                    logger.info("Не удалось отправить почту. Not connected");
                }
            }
            t.close();

        } catch (MessagingException | NullPointerException e) {
            log.error(e.getMessage(),e);
        }
    }
    public String getEmailCode(VariablesProperties mcfg) {

        try {
            log.info((new Date()) + "---- Email-getting code ---");
            Properties props = System.getProperties();
            props.put("mail.pop3.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.put("mail.pop3.socketFactory.fallback", "false");
            props.put("mail.pop3.socketFactory.port", mcfg.getCode_email_pop3_port());
            props.put("mail.pop3.port", mcfg.getCode_email_pop3_port());
            props.put("mail.pop3.host", mcfg.getCode_email_pop3_host());
            props.put("mail.pop3.user", mcfg.getCode_email_user());
            props.put("mail.store.protocol", "pop3");
            props.put("mail.pop3.ssl.protocols", mcfg.getCode_email_pop3_ssl_protocols());
            Authenticator auth = new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(mcfg.getCode_email_user(), mcfg.getCode_email_password());
                }
            };
            Session session = Session.getInstance(props, auth);
            Store store = session.getStore("pop3");
            store.connect(mcfg.getCode_email_pop3_host(), mcfg.getCode_email_user(), mcfg.getCode_email_password());
            Date time = new Date();
            long start = time.getTime();
            long end = time.getTime();
            boolean exists = false;
            while (end - start < 60 * 1000L && !exists) {

                Folder inbox = store.getFolder("INBOX");
                inbox.open(Folder.READ_ONLY);
                Message[] messages = inbox.getMessages();

                Message maxMessage = Arrays.stream(messages)
                        .filter(f -> {
                            try {
                                return InternetAddress.toString(f.getReplyTo()).contains("tn.ru");
                            } catch (MessagingException e) {
                                return false;
                            }
                        })
                        .max(Comparator.comparingLong(m -> {
                            try {

                                return m.getSentDate().getTime();
                            } catch (MessagingException e) {
                                throw new RuntimeException(e);
                            }
                        })).orElse(null);

                if (maxMessage != null && (start - 30000L) < maxMessage.getSentDate().getTime()) {

                    if (maxMessage.isMimeType("text/html")) {
                        String html = maxMessage.getContent().toString();
                        Document document = Jsoup.parse(html);
                        Elements code = document.selectXpath("//td/p[contains(@style,'#E72430')]");
                        log.info(code.get(0).text());
                        return code.get(0).text();
                    }
                }
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ignored) {
                }
                inbox.close(false);
                end = new Date().getTime();
            }
            store.close();
        } catch (IOException | MessagingException | NullPointerException e) {
            log.error(e.getMessage(),e);
        }
        return "";
    }
}
