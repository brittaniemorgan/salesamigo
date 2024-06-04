/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PointOfSale;

import Authentication.Customer;
import Authentication.User;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
/**
 *
 * @author britt
 */
import Database.APIManager;
import Inventory.Inventory;
import Inventory.Product;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Date;

public class POS {

    private APIManager api;
    private Inventory inventory;
    private ArrayList<Transaction> pendingTransactions;
    private ArrayList<Discount> discounts;
    private ArrayList<PaymentType> paymentTypes;
    private Customer currentCustomer;
    private User employee;
    ReceiptPrinter printer;
//update
        
    public POS(User user) {
        try {
            api = APIManager.getAPIManager();
            this.employee = user;
            inventory = new Inventory();
            pendingTransactions = new ArrayList<>();
            discounts = new ArrayList<>();
            paymentTypes = new ArrayList<>();
            //Updated
            setCustomer(1);
            setPaymentTypes();

            // Load discounts from API
            loadDiscounts();
            printer = new ReceiptPrinter();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    // Method to set the current customer
    public void setCustomer(int customerId) {
        try {
            JSONObject response = api.getCustomerByID(customerId);
            JSONArray customerArray = response.getJSONArray("customers");
            for (int i = 0; i < customerArray.length(); i++) {
                JSONObject customer = customerArray.getJSONObject(i);
                currentCustomer = new Customer(
                    customer.getInt("customer_id"),
                    customer.getString("firstname"),
                    customer.getString("lastname"),  
                    customer.getString("email"),
                    customer.getString("contact_number"),
                    customer.getString("address"),
                    customer.getInt("points_balance"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Getters for customer and other attributes
    public Customer getCurrentCustomer() {
        return currentCustomer;
    }

    private void loadDiscounts() {
        try {
            // Get all discounts from the API
            JSONObject response = api.getDiscounts();
            JSONArray generalArray = response.getJSONArray("general discounts");
            JSONArray productArray = response.getJSONArray("product discounts");
            JSONArray categoryArray = response.getJSONArray("category discounts");
            JSONArray brandArray = response.getJSONArray("brand discounts");

            // Load general discounts (if any)
            for (int i = 0; i < generalArray.length(); i++) {
                JSONObject discountJson = generalArray.getJSONObject(i);
                discounts.add(new Discount(
                        discountJson.getInt("discount_id"),
                        discountJson.getString("discount_name"),
                        discountJson.getString("discount_code"),
                        discountJson.getDouble("discount_percent")
                ));
            }

            // Load product discounts
            for (int i = 0; i < productArray.length(); i++) {
                JSONObject discountJson = productArray.getJSONObject(i);
                discounts.add(new ProductDiscount(
                        discountJson.getInt("discount_id"),
                        discountJson.getString("discount_name"),
                        discountJson.getString("discount_code"),
                        discountJson.getDouble("discount_percent"),
                        discountJson.getInt("product_id")
                ));
            }

            // Load category discounts
            for (int i = 0; i < categoryArray.length(); i++) {
                JSONObject discountJson = categoryArray.getJSONObject(i);
                discounts.add(new CategoryDiscount(
                        discountJson.getInt("discount_id"),
                        discountJson.getString("discount_name"),
                        discountJson.getString("discount_code"),
                        discountJson.getDouble("discount_percent"),
                        discountJson.getInt("category_id")
                ));
            }

            // Load brand discounts
            for (int i = 0; i < brandArray.length(); i++) {
                JSONObject discountJson = brandArray.getJSONObject(i);
                discounts.add(new BrandDiscount(
                        discountJson.getInt("discount_id"),
                        discountJson.getString("discount_name"),
                        discountJson.getString("discount_code"),
                        discountJson.getDouble("discount_percent"),
                        discountJson.getInt("brand_id")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Inventory getInventory() {
        return inventory;
    }

    public ArrayList<Transaction> getPendingTransactions() {
        return pendingTransactions;
    }

    public void addPendingTransaction(Transaction transaction) {
        pendingTransactions.add(transaction);
    }

    public void deletePendingTransaction(Transaction transaction) {
        pendingTransactions.remove(transaction);
    }

    public void createSaleTransaction(int employeeId, int customerId) {
        int tempID = pendingTransactions.size() + 1;
        pendingTransactions.add(new Transaction(tempID, new Date(), employeeId, customerId));
    }

    public Transaction getPendingTransactionByID(int id) {
        for (Transaction transaction : pendingTransactions) {
            if (transaction.getTransactionId() == id) {
                return transaction;
            }
        }
        return null;
    }

    public ArrayList<Discount> getDiscountsForProduct(int productId) {
        ArrayList<Discount> productDiscounts = new ArrayList<>();

        for (Discount discount : discounts) {
            if (discount instanceof ProductDiscount) {
                ProductDiscount productDiscount = (ProductDiscount) discount;
                if (productDiscount.getProductId() == productId) {
                    productDiscounts.add(productDiscount);
                }
            } else if (discount instanceof CategoryDiscount) {
                // Assuming you have a method to get category ID for a product
                Product product = inventory.findProductByID(productId);
                int categoryId = inventory.findCategoryByName(product.getCategory()).getId();
                CategoryDiscount categoryDiscount = (CategoryDiscount) discount;
                if (categoryDiscount.getCategoryId() == categoryId) {
                    productDiscounts.add(categoryDiscount);
                }
            } else if (discount instanceof BrandDiscount) {
                // Assuming you have a method to get brand ID for a product
                Product product = inventory.findProductByID(productId);
                int brandId = inventory.findBrandByName(product.getBrand()).getId();
                BrandDiscount brandDiscount = (BrandDiscount) discount;
                if (brandDiscount.getBrandId() == brandId) {
                    productDiscounts.add(brandDiscount);
                }
            } else {
                productDiscounts.add(discount);
            }
        }

        return productDiscounts;
    }

    public String applyDiscount(Transaction order, String discountCode) {
        String feedback = "Error: Invalid Code";
        for(TransactionItem item : order.getItems()){
            ArrayList<Discount> applicableDiscounts = getDiscountsForProduct(item.getProductId());
            double highestDiscount = 0.0;
            if (item.getDiscount() != null){
                highestDiscount = item.getDiscount().getAmount();
            }
            for (Discount discount : applicableDiscounts) {
                if (discount.getCode().equals(discountCode)) {
                    if (discount.getAmount() > highestDiscount){
                        highestDiscount = discount.getAmount();
                        item.setDiscount(discount);
                        order.calculateTotal();
                        feedback = "Code applied successfully";
                    }                    
                }              
            }
        }
  
        return feedback;
    }
    
    //Update
    public ArrayList<String> applyAllDiscount(Transaction order, ArrayList<String> discountCodes) {
        ArrayList<String> validCodes = new ArrayList<>();
        for(String discountCode : discountCodes){
            String feedback = applyDiscount(order, discountCode);
            if (feedback.contains("success")){
                validCodes.add(discountCode);
            }
        }
        return validCodes;
    }
    
    
    /* Apply sale when item is added
    if (discount.getCode().equals("") && discount.getAmount() > highestDiscount) {
                    highestDiscount = discount.getAmount();
                    discountedPrice = calculateDiscountedPrice(inventory.findVarantByID(item.getProductId()).getPrice(), discount.getAmount());
                    // Update the item's price with the discounted price
                    item.setDiscount(discount);
                    order.calculateTotal(); 
                    feedback += "Sale Discounts Applied";
                }
    */
//Updated
    public String performSaleTransaction(int orderId, String paymentMethod) {
        String feedback = "";
        try {
            Transaction transaction = getPendingTransactionByID(orderId);
            int employeeId = transaction.getEmployeeId();
            int customerId = transaction.getCustomerId();
            double total = transaction.getTotal();
            int pointsApplied = transaction.getPointsApplied();
            ArrayList<TransactionItem> items = transaction.getItems();
            
            
            JSONArray itemsArray = new JSONArray();
            for (TransactionItem item : items) {
                JSONObject itemObj = new JSONObject();
                itemObj.put("product_id", item.getProductId());
                itemObj.put("quantity", item.getQuantity());
                itemObj.put("price", item.getTotal());
                if (item.getDiscount() != null){
                    itemObj.put("discount_id", item.getDiscount().getId());
                }
                itemsArray.put(itemObj);
            }

            JSONObject response = api.addTransaction(employeeId, customerId, total, paymentMethod, itemsArray, pointsApplied);

            if (response.has("error")) {
                feedback = "Error performing transaction:" + response.getString("error");
            } else {
                int transactionId = response.getInt("transaction_id");
                feedback = "Transaction successful!";
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

    public void printReceipt(int transactionId) {
        Transaction transaction = getPendingTransactionByID(transactionId);
        printer.printReceipt(transaction);
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


    public String redeemPoints(int orderId, int pointsToRedeem) {
        try {
            // Check if current customer is set
            if (currentCustomer == null) {
                return "No customer selected.";
            }

            // Check if customer has enough points to redeem
            if (currentCustomer.getPointsBalance() >= pointsToRedeem) {
                // Calculate new points balance after redemption
                int newPointsBalance = currentCustomer.getPointsBalance() - pointsToRedeem;
                currentCustomer.setPointsBalance(newPointsBalance);
                Transaction transaction = getPendingTransactionByID(orderId);
                transaction.setPointsApplied(pointsToRedeem);
                return "Points redeemed successfully. New points balance: " + newPointsBalance;
                

                // Update the customer's points balance
                //JSONObject response = api.updateCustomerPoints(currentCustomer.getId(), newPointsBalance);
               /* if (response != null && response.has("success")) {
                    currentCustomer.setPointsBalance(newPointsBalance);
                    return "Points redeemed successfully. New points balance: " + newPointsBalance;
                } else {
                    return "Failed to update points balance.";
                }*/
            } else {
                return "Insufficient points to redeem.";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error redeeming points: " + e.getMessage();
        }
    }
    
    public ArrayList<PaymentType> getPaymentTypes() {
        return paymentTypes;
    }
    
    public String createPaymentType(String name) {
        String feedback = "";
        try {            
            JSONObject response = api.addPaymentType(name);
            
            if (response.has("error")) {
                feedback = "Error adding payment type: " + response.getString("error");                
            } else {
                int typeId = response.getInt("id");
                paymentTypes.add(new PaymentType(typeId, name));
                feedback = "Payment type added successfully!";
            }            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return feedback;
    }
    
    public String updatePaymentType(int id, String name) {
        String feedback = "";
        try {
            JSONObject response = api.updatePaymentType(id, name);
            
            if (response.has("error")) {
                feedback = "Error updating payment type: " + response.getString("error");                
            } else {
                for (PaymentType paymentType : paymentTypes){
                    if (paymentType.getId() == id){
                        paymentType.setName(name);
                        break;
                    }
                }
                feedback = "Payment type updated successfully!";
            } 
            System.out.println("Response from server: " + response.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return feedback;
    }

    public String deletePaymentType(int id) {
        String feedback = "";
        try {
            JSONObject response = api.deletePaymentType(id);
            if (response.has("error")) {
                feedback = "Error deleting payment type: " + response.getString("error");                
            } else {
                for (PaymentType paymentType : paymentTypes){
                    if (paymentType.getId() == id){
                        paymentTypes.remove(paymentType);
                        feedback = "Payment type deleted successfully!";
                        break;
                    }
                }
            } 
            System.out.println("Response from server: " + response.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return feedback;
    }

    public PaymentType findPaymentTypeByName(String name) {
        for (PaymentType paymentType : paymentTypes) {
            if (paymentType.getName().equals(name)) {
                return paymentType;
            }
        }
        return null; 
    }
    
    private void setPaymentTypes() {
        try {
            JSONObject response = api.getPaymentTypes();
            JSONArray paymentTypesArray = response.getJSONArray("payment_types");

            for (int i = 0; i < paymentTypesArray.length(); i++) {
                JSONObject paymentTypeObject = paymentTypesArray.getJSONObject(i);
                int id = paymentTypeObject.getInt("payment_id");
                String name = paymentTypeObject.getString("payment");
                paymentTypes.add(new PaymentType(id, name));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
