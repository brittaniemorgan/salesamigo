/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PromotionAndMarketing;

/**
 *
 * @author britt
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class EmailUI {

    private JFrame frame;
    private JTextField senderField;
    private JTextField recipientField;
    private JTextField subjectField;
    private JTextArea contentArea;
    private JButton attachButton;
    private JTextArea attachmentArea;
    private JComboBox<String> templateComboBox;
    private JButton embedImageButton;
    private File[] embeddedImageFiles;

    private Email email;
    private Map<String, String> templates;

    public EmailUI() {
        initialize();
        initializeTemplates();
        this.email = new Email();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout(0, 0));

        JPanel panel = new JPanel();
        frame.getContentPane().add(panel, BorderLayout.CENTER);
        panel.setLayout(new BorderLayout(0, 0));

        JPanel composePanel = new JPanel();
        panel.add(composePanel, BorderLayout.CENTER);
        composePanel.setLayout(new BorderLayout(0, 0));

        JPanel fieldsPanel = new JPanel();
        composePanel.add(fieldsPanel, BorderLayout.CENTER);
        fieldsPanel.setLayout(new GridLayout(7, 1, 0, 0)); // Updated to 7 rows

        JPanel senderPanel = new JPanel();
        fieldsPanel.add(senderPanel);
        senderPanel.setLayout(new BorderLayout(0, 0));

        JLabel lblSender = new JLabel("From:");
        senderPanel.add(lblSender, BorderLayout.WEST);

        senderField = new JTextField();
        senderField.setText("your-email@example.com"); // Replace with your email
        senderPanel.add(senderField, BorderLayout.CENTER);
        senderField.setColumns(10);

        JPanel recipientPanel = new JPanel();
        fieldsPanel.add(recipientPanel);
        recipientPanel.setLayout(new BorderLayout(0, 0));

        JLabel lblRecipient = new JLabel("To:");
        recipientPanel.add(lblRecipient, BorderLayout.WEST);

        recipientField = new JTextField();
        recipientPanel.add(recipientField, BorderLayout.CENTER);
        recipientField.setColumns(10);

        JPanel subjectPanel = new JPanel();
        fieldsPanel.add(subjectPanel);
        subjectPanel.setLayout(new BorderLayout(0, 0));

        JLabel lblSubject = new JLabel("Subject:");
        subjectPanel.add(lblSubject, BorderLayout.WEST);

        subjectField = new JTextField();
        subjectPanel.add(subjectField, BorderLayout.CENTER);
        subjectField.setColumns(10);

        JPanel templatePanel = new JPanel();
        fieldsPanel.add(templatePanel);
        templatePanel.setLayout(new BorderLayout(0, 0));

        JLabel lblTemplate = new JLabel("Template:");
        templatePanel.add(lblTemplate, BorderLayout.WEST);

        templateComboBox = new JComboBox<>();
        templatePanel.add(templateComboBox, BorderLayout.CENTER);
        templateComboBox.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                String selectedTemplate = (String) templateComboBox.getSelectedItem();
                if (selectedTemplate != null) {
                    String templateContent = templates.get(selectedTemplate);
                    contentArea.setText(templateContent);
                }
            }
        });

        JPanel contentPanel = new JPanel();
        fieldsPanel.add(contentPanel);
        contentPanel.setLayout(new BorderLayout(0, 0));

        JLabel lblContent = new JLabel("Content:");
        contentPanel.add(lblContent, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane();
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        contentArea = new JTextArea();
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        scrollPane.setViewportView(contentArea);

        JPanel attachmentPanel = new JPanel();
        fieldsPanel.add(attachmentPanel);
        attachmentPanel.setLayout(new BorderLayout(0, 0));

        JPanel attachButtonPanel = new JPanel();
        attachmentPanel.add(attachButtonPanel, BorderLayout.NORTH);

        attachButton = new JButton("Attach File");
        attachButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fileChooser.setMultiSelectionEnabled(true);
            int option = fileChooser.showOpenDialog(frame);
            if (option == JFileChooser.APPROVE_OPTION) {
                File[] selectedFiles = fileChooser.getSelectedFiles();
                for (File file : selectedFiles) {
                    if (email != null) {
                        email.addAttachment(file);
                        attachmentArea.append(file.getName() + "\n");
                    } else {
                        JOptionPane.showMessageDialog(frame, "Please enter email details and click 'Send' before attaching files.");
                    }
                }
            }
        });
        attachButtonPanel.add(attachButton);

        embedImageButton = new JButton("Embed Images from Folder");
        embedImageButton.addActionListener(e -> {
            String directoryPath = "./assets";

            File folder = new File(directoryPath);
            if (folder.exists() && folder.isDirectory()) {
                embeddedImageFiles = folder.listFiles((dir, name)
                        -> name.toLowerCase().endsWith(".jpg") || name.toLowerCase().endsWith(".png")
                        || name.toLowerCase().endsWith(".gif") || name.toLowerCase().endsWith(".bmp"));
                if (embeddedImageFiles != null) {
                    for (File imageFile : embeddedImageFiles) {
                        email.addEmbeddedImage(imageFile, "embeddedImage_" + System.currentTimeMillis());
                        contentArea.append("<img src='cid:" + "embeddedImage_" + System.currentTimeMillis() + "'>");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid directory or directory does not exist.");
            }
        });
        attachButtonPanel.add(embedImageButton);

        JPanel attachmentAreaPanel = new JPanel();
        attachmentPanel.add(attachmentAreaPanel, BorderLayout.CENTER);
        attachmentAreaPanel.setLayout(new BorderLayout(0, 0));

        JLabel lblAttachments = new JLabel("Attachments:");
        attachmentAreaPanel.add(lblAttachments, BorderLayout.NORTH);

        JScrollPane attachmentScrollPane = new JScrollPane();
        attachmentAreaPanel.add(attachmentScrollPane, BorderLayout.CENTER);

        attachmentArea = new JTextArea();
        attachmentArea.setEditable(false);
        attachmentScrollPane.setViewportView(attachmentArea);

        JPanel buttonPanel = new JPanel();
        panel.add(buttonPanel, BorderLayout.SOUTH);

        JButton btnSend = new JButton("Send");
        btnSend.addActionListener(e -> {
            String sender = senderField.getText().trim();
            String recipient = recipientField.getText().trim();
            String[] recipients = recipient.split(",");
            String subject = subjectField.getText().trim();
            String content = contentArea.getText().trim();

            email.setSender(sender);
            email.setRecipients(recipients);
            email.setSubject(subject);
            email.setContent(content);

            if (embeddedImageFiles != null) {
                for (File imageFile : embeddedImageFiles) {
                    email.addEmbeddedImage(imageFile, "embeddedImage_" + System.currentTimeMillis());
                }
            }

            email.send();

            JOptionPane.showMessageDialog(frame, "Email sent successfully!");
        });
        buttonPanel.add(btnSend);

        frame.setVisible(true);
    }

    private void initializeTemplates() {
        // Initialize templates
        templates = new HashMap<>();
        templates.put("Standard Template", "<html><body><h2>Dear Customer,</h2><p>Please find our latest product recommendations below:</p><ul><li><img src='https://example.com/productA.jpg' width='100' height='100'> Product A</li><li><img src='https://example.com/productB.jpg' width='100' height='100'> Product B</li><li><img src='https://example.com/productC.jpg' width='100' height='100'> Product C</li></ul><p>Best regards,<br>Your Team</body></html>");
        templates.put("Promotional Template", "<html><body><h2>Hi there,</h2><p>Check out these exciting new products!</p><ul><li><img src='cid:embeddedImage' width='100' height='100' alt='No image found'> Product X</li><li><img src='https://example.com/productY.jpg' width='100' height='100'> Product Y</li><li><img src='https://example.com/productZ.jpg' width='100' height='100'> Product Z</li></ul><p>Regards,<br>Your Team</body></html>");
        templates.put("Custom Template", "<html><body><h2>Customize this template as per your need.</h2></body></html>");

        // Populate template combo box
        for (String templateName : templates.keySet()) {
            templateComboBox.addItem(templateName);
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                EmailUI window = new EmailUI();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
