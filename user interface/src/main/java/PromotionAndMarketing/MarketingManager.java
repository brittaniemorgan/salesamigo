/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PromotionAndMarketing;

/**
 *
 * @author britt
 */
import Authentication.Customer;
import Database.APIManager;
import ReportingAndAnalytics.ProductRecommendation;
import ReportingAndAnalytics.ProductRecommendationEngine;
import java.io.File;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;

public class MarketingManager {

    private APIManager api;
    private ProductRecommendationEngine productRecommendationEngine;
    public ArrayList<Customer> customers;

    public MarketingManager() {
        try {
            api = APIManager.getAPIManager();
            customers = new ArrayList<>();
            productRecommendationEngine = new ProductRecommendationEngine();
            JSONArray customersArr = api.getCustomers().getJSONArray("customers");
            for (int i = 0; i < customersArr.length(); i++) {
                JSONObject customer = customersArr.getJSONObject(i);
                customers.add(new Customer(
                        customer.getInt("customer_id"),
                        customer.getString("firstname"),
                        customer.getString("lastname"),
                        customer.getString("email"),
                        customer.getString("contact_number"),
                        customer.getString("address"),
                        customer.getInt("points_balance")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Customer> getCustomers() {
        return customers;
    }

    public void sendEmail(String[] recipients, String sender, String emailContent, String subject, ArrayList<File> attachments) {
        Email email = new Email();
        email.setRecipients(recipients);
        email.setSender(sender);
        email.setContent(emailContent);
        email.setSubject(subject);
        for (File file : attachments) {
            email.addAttachment(file);
        }
        email.send();
    }

    public void sendRecommendationEmails() {
        try {
            for (Customer customer : customers.subList(1, 4)) {
                Email email = new Email();
                String[] recipients = new String[1];
                recipients[0] = customer.getEmail();
                email.setRecipients(recipients);
                ArrayList<ProductRecommendation> recommendations = productRecommendationEngine.getRecommendationsFor(customer.getId());
                StringBuilder emailContent = new StringBuilder();
                emailContent.append("<html><body>");
                emailContent.append("<h2>Dear " + customer.getFirstname() + ",</h2>");
                emailContent.append("<p>We have some product recommendations for you:</p>");
                emailContent.append("<ul>");
                for (ProductRecommendation recommendation : recommendations) {
                    emailContent.append("<li>")
                            .append(recommendation.getBrand()).append(" ")
                            .append(recommendation.getCategory()).append(" ")
                            .append(recommendation.getColor()).append(" - $")
                            .append(recommendation.getPrice());
                    String directoryPath = "./assets";
                    File[] embeddedImageFiles;
                    File folder = new File(directoryPath);
                    if (folder.exists() && folder.isDirectory()) {
                        embeddedImageFiles = folder.listFiles((dir, name)
                                -> name.toLowerCase().endsWith(".jpg") || name.toLowerCase().endsWith(".png")
                                || name.toLowerCase().endsWith(".gif") || name.toLowerCase().endsWith(".bmp"));
                        if (embeddedImageFiles != null) {
                            for (File imageFile : embeddedImageFiles) {
                                email.addEmbeddedImage(imageFile, "embeddedImage_" + System.currentTimeMillis());
                                if (imageFile.getName().equals(recommendation.getProductId() + ".png")) {
                                    emailContent.append("<img src='cid:" + "embeddedImage_" + System.currentTimeMillis() + "' width='50px'>");
                                }
                            }
                        }
                    }
                    emailContent.append("</li>");
                    emailContent.append("</ul>");
                }
                emailContent.append("<p>SHOP AT FRESH SENSATIONS!</p>");
                emailContent.append("</body></html>");
                email.setSender("do-not-reply@salesamigo.com");
                email.setContent(emailContent.toString());
                email.setSubject("Your Product Recommendations");
                email.send();
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
}
