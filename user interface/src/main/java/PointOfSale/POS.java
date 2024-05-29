/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PointOfSale;

import Database.APIManager;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author britt
 */

public class POS {
    private APIManager api;

    public POS() {
        try{
            api = APIManager.getAPIManager();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // to be tested
    
    public String performSaleTransaction(int employeeId, int customerId, double total, String paymentMethod, ArrayList<TransactionItem> items) {
        String feedback = "";
        try {
            JSONObject response = api.addTransaction(employeeId, customerId, total, paymentMethod, items);

            if (response != null && response.has("error")) {                
                feedback = "Error performing transaction.";
            } else {
                int transactionId = response.getInt("transaction_id");
                feedback = "Transaction successful! Transaction ID: " + transactionId;
            }
        } catch (Exception e) {
            e.printStackTrace();
            feedback = "Error performing transaction: " + e.getMessage();
        }
        return feedback;
    }

    // Method to handle refunds
    public String processRefund(int transactionId, int itemId) {
        String feedback = "";
        try {
            JSONObject response = api.refundTransactionItem(transactionId, itemId);

            if (response != null && response.has("error")) {
                feedback = "Error processing refund.";
            } else {                
                feedback = "Refund processed successfully!";
            }
        } catch (Exception e) {
            e.printStackTrace();
            feedback = "Error processing refund: " + e.getMessage();
        }
        return feedback;
    }
    
    public ArrayList<Transaction> viewTransactionHistory() {
        ArrayList<Transaction> transactions = new ArrayList<>();
        try {
            JSONObject response = api.getTransactions();
            JSONArray transactionArray = response.getJSONArray("transactions");

            for (int i = 0; i < transactionArray.length(); i++) {
                JSONObject transactionObj = transactionArray.getJSONObject(i);
                int transactionId = transactionObj.getInt("transaction_id");
                Date transactionDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(transactionObj.getString("transaction_date"));
                int employeeId = transactionObj.getInt("employee_id");
                int customerId = transactionObj.getInt("customer_id");
                double total = transactionObj.getDouble("total");
                String paymentMethod = transactionObj.getString("payment_method");

                // Fetch items for the transaction
                JSONArray itemsArray = transactionObj.getJSONArray("items");
                ArrayList<TransactionItem> items = new ArrayList<>();
                for (int j = 0; j < itemsArray.length(); j++) {
                    JSONObject itemObj = itemsArray.getJSONObject(j);
                    int productId = itemObj.getInt("product_id");
                    int quantity = itemObj.getInt("quantity");
                    double price = itemObj.getDouble("price");

                    items.add(new TransactionItem(productId, quantity, price));
                }

                transactions.add(new Transaction(transactionId, transactionDate, customerId, employeeId, total, items, paymentMethod));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return transactions;
    }
    
    public void printReceipt(Transaction transaction) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            DecimalFormat decimalFormat = new DecimalFormat("#.##");

            System.out.println("Receipt");
            System.out.println("----------------------------------");
            System.out.println("Transaction ID: " + transaction.getTransactionId());
            System.out.println("Date: " + dateFormat.format(transaction.getTransactionDate()));
            System.out.println("Employee ID: " + transaction.getEmployeeId());
            System.out.println("Customer ID: " + transaction.getCustomerId());
            System.out.println("Total: $" + decimalFormat.format(transaction.getTotal()));
            System.out.println("Payment Method: " + transaction.getPaymentMethod());
            System.out.println("----------------------------------");
            System.out.println("Items:");

            for (TransactionItem item : transaction.getItems()) {
                System.out.println("Product ID: " + item.getProductId() + ", Quantity: " + item.getQuantity() + ", Price: $" + decimalFormat.format(item.getPrice()));
            }
            System.out.println("----------------------------------");
            System.out.println("Thank you for your purchase!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    //not sure how to implement
    //need to account for discounts
    public void addDiscount(){}
    
    public void applyPoints(){}
    
    public void addPoint(){}
}
    
