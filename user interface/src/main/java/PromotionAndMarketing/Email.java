/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PromotionAndMarketing;

/**
 *
 * @author britt
 */
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.ArrayList;
import java.util.Properties;
import java.util.*;
import java.io.File;
import java.util.List;
import java.util.Map;

public class Email {
    private String sender;
    private String[] recipients;
    private String subject;
    private String content;
    private List<File> attachments;
    private Map<String, File> embeddedImages;

    public Email() {
        attachments = new ArrayList<>();
        embeddedImages = new HashMap<>();
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setRecipients(String[] recipients) {
        this.recipients = recipients;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setContent(String content) {
        this.content = content;
    }
    
    public void addAttachment(File file) {
        attachments.add(file);
    }

    public void addEmbeddedImage(File file, String cid) {
        embeddedImages.put(cid, file);
    }

    public void send() {
        String host = "smtp.mailtrap.io";

        // Getting system properties
        Properties properties = System.getProperties();

        // Setting up mail server
        properties.setProperty("mail.smtp.host", host);
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.port", "2525"); // or 587, or 465 for SSL/TLS

        // creating session object to get properties
        Session session = Session.getDefaultInstance(properties, new javax.mail.Authenticator() {
            @Override
            protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                return new javax.mail.PasswordAuthentication("8d55918a39c0e9", "f85c9f90151b2f"); // 
            }
        });

        try {
            // Create a default MimeMessage object
            Message message = new MimeMessage(session);

            // Set From: header field
            message.setFrom(new InternetAddress(sender));

            // Set To: header field
            InternetAddress[] recipientAddresses = new InternetAddress[recipients.length];
            for (int i = 0; i < recipients.length; i++) {
                recipientAddresses[i] = new InternetAddress(recipients[i]);
            }
            message.setRecipients(Message.RecipientType.TO, recipientAddresses);

            // Set Subject: header field
            message.setSubject(subject);

            // Create a multipart message
            Multipart multipart = new MimeMultipart("related");

            // Part one is the HTML content
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(content, "text/html");
            multipart.addBodyPart(messageBodyPart);

            // Part two is the embedded images
            for (Map.Entry<String, File> entry : embeddedImages.entrySet()) {
                String cid = entry.getKey();
                File imageFile = entry.getValue();

                messageBodyPart = new MimeBodyPart();
                DataSource fds = new FileDataSource(imageFile);
                messageBodyPart.setDataHandler(new DataHandler(fds));
                messageBodyPart.setHeader("Content-ID", "<" + cid + ">");
                multipart.addBodyPart(messageBodyPart);
            }

            // Part three is the attachments
            for (File attachment : attachments) {
                messageBodyPart = new MimeBodyPart();
                DataSource source = new FileDataSource(attachment);
                messageBodyPart.setDataHandler(new DataHandler(source));
                messageBodyPart.setFileName(attachment.getName());
                multipart.addBodyPart(messageBodyPart);
            }

            // Set the complete message parts
            message.setContent(multipart);

            // Send message
            Transport.send(message);

            System.out.println("Email sent successfully!");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
