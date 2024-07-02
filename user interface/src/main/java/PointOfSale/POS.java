/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PointOfSale;

import PromotionAndMarketing.Discount;
import PromotionAndMarketing.BrandDiscount;
import PromotionAndMarketing.CategoryDiscount;
import Authentication.Customer;
import Authentication.Employee;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
/**
 *
 * @author britt
 */
import API.APIManager;
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
    private Employee employee;
    ReceiptPrinter printer;
//update

    public POS(Employee user) {
        try {
            api = APIManager.getAPIManager();
            this.employee = user;
            inventory = new Inventory();
            pendingTransactions = new ArrayList<>();
            discounts = new ArrayList<>();
            paymentTypes = new ArrayList<>();
            setCustomer(1);
            setPaymentTypes();
            loadDiscounts();
            printer = new ReceiptPrinter();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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

    public Customer getCurrentCustomer() {
        return currentCustomer;
    }

    private void loadDiscounts() {
        try {
            discounts = new ArrayList<>();
            JSONObject response = api.getDiscounts();
            JSONArray generalArray = response.getJSONArray("general discounts");
            JSONArray productArray = response.getJSONArray("product discounts");
            JSONArray categoryArray = response.getJSONArray("category discounts");
            JSONArray brandArray = response.getJSONArray("brand discounts");

            // Load general discounts (if any)
            for (int i = 0; i < generalArray.length(); i++) {
                JSONObject discountJson = generalArray.getJSONObject(i);
                Date startDate = new SimpleDateFormat("EEE, dd MMM yyyy").parse(discountJson.getString("start_date"));
                Date endDate = new SimpleDateFormat("EEE, dd MMM yyyy").parse(discountJson.getString("end_date"));

                discounts.add(new Discount(
                        discountJson.getInt("discount_id"),
                        discountJson.getString("discount_code"),
                        discountJson.getString("discount_name"),
                        discountJson.getDouble("discount_percent"),
                        startDate,
                        endDate
                ));
            }

            // Load product discounts
            for (int i = 0; i < productArray.length(); i++) {
                JSONObject discountJson = productArray.getJSONObject(i);
                Date startDate = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz").parse(discountJson.getString("start_date"));
                Date endDate = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz").parse(discountJson.getString("end_date"));
                boolean found = false;
                for (Discount discount : discounts) {
                    if (discount.getId() == discountJson.getInt("discount_id")) {
                        found = true;
                        if (discount instanceof ProductDiscount) {
                            ((ProductDiscount) discount).addProduct(inventory.findVarantByID(discountJson.getInt("discount_id")));
                        }
                    }
                }
                if (!found) {
                    ProductDiscount productDiscount = new ProductDiscount(
                            discountJson.getInt("discount_id"),
                            discountJson.getString("discount_code"),
                            discountJson.getString("discount_name"),
                            discountJson.getDouble("discount_percent"),
                            startDate,
                            endDate);
                    productDiscount.addProduct(inventory.findVarantByID(discountJson.getInt("product_id")));
                    discounts.add(productDiscount);
                }
            }

            // Load category discounts
            for (int i = 0; i < categoryArray.length(); i++) {
                JSONObject discountJson = categoryArray.getJSONObject(i);
                Date startDate = new SimpleDateFormat("EEE, dd MMM yyyy").parse(discountJson.getString("start_date"));
                Date endDate = new SimpleDateFormat("EEE, dd MMM yyyy").parse(discountJson.getString("end_date"));
                boolean found = false;
                for (Discount discount : discounts) {
                    if (discount.getId() == discountJson.getInt("discount_id")) {
                        found = true;
                        if (discount instanceof CategoryDiscount) {
                            ((CategoryDiscount) discount).addCategory(inventory.findCategoryByID(discountJson.getInt("category_id")));
                        }
                    }
                }
                if (!found) {
                    CategoryDiscount categoryDiscount = new CategoryDiscount(
                            discountJson.getInt("discount_id"),
                            discountJson.getString("discount_code"),
                            discountJson.getString("discount_name"),
                            discountJson.getDouble("discount_percent"),
                            startDate,
                            endDate);
                    categoryDiscount.addCategory(inventory.findCategoryByID(discountJson.getInt("category_id")));
                    discounts.add(categoryDiscount);
                }
            }

            // Load brand discounts
            for (int i = 0; i < brandArray.length(); i++) {
                JSONObject discountJson = brandArray.getJSONObject(i);
                Date startDate = new SimpleDateFormat("EEE, dd MMM yyyy").parse(discountJson.getString("start_date"));
                Date endDate = new SimpleDateFormat("EEE, dd MMM yyyy").parse(discountJson.getString("end_date"));
                boolean found = false;
                for (Discount discount : discounts) {
                    if (discount.getId() == discountJson.getInt("discount_id")) {
                        found = true;
                        if (discount instanceof BrandDiscount) {
                            ((BrandDiscount) discount).addBrand(inventory.findBrandByID(discountJson.getInt("brand_id")));
                        }
                    }
                }
                if (!found) {
                    BrandDiscount brandDiscount = new BrandDiscount(
                            discountJson.getInt("discount_id"),
                            discountJson.getString("discount_code"),
                            discountJson.getString("discount_name"),
                            discountJson.getDouble("discount_percent"),
                            startDate,
                            endDate);
                    brandDiscount.addBrand(inventory.findBrandByID(discountJson.getInt("brand_id")));
                    discounts.add(brandDiscount);
                }
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

    public ArrayList<Discount> getAllDiscounts() {
        return discounts;
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
                Product product = inventory.findProductByID(productId);
                int categoryId = inventory.findCategoryByName(product.getCategory()).getId();
                CategoryDiscount categoryDiscount = (CategoryDiscount) discount;
                if (categoryDiscount.getCategoryId() == categoryId) {
                    productDiscounts.add(categoryDiscount);
                }
            } else if (discount instanceof BrandDiscount) {
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

    /*
    public ArrayList<Brand> getApplicableBrands(int discID){
        
    }
    
    public ArrayList<Products> getApplicableProducts(int discID){}
    
    public ArrayList<Discount> getApplicableCategories(int discID){}
     */
    public String applyDiscount(Transaction order, String discountCode) {
        String feedback = "Error: Invalid Code";
        for (TransactionItem item : order.getItems()) {
            ArrayList<Discount> applicableDiscounts = getDiscountsForProduct(item.getProductId());
            double highestDiscount = 0.0;
            if (item.getDiscount() != null) {
                highestDiscount = item.getDiscount().getAmount();
            }
            for (Discount discount : applicableDiscounts) {
                if (discount.getCode().equals(discountCode)) {
                    if (discount.getAmount() > highestDiscount) {
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
    
    public ArrayList<String> applyAllDiscount(Transaction order, ArrayList<String> discountCodes) {
        ArrayList<String> validCodes = new ArrayList<>();
        for (String discountCode : discountCodes) {
            String feedback = applyDiscount(order, discountCode);
            if (feedback.contains("success")) {
                validCodes.add(discountCode);
            }
        }
        return validCodes;
    }
    
    public String performSaleTransaction(int orderId, String paymentMethod) {
        String feedback = "";
        try {
            Transaction transaction = getPendingTransactionByID(orderId);
            int employeeId = transaction.getEmployeeId();
            int customerId = transaction.getCustomerId();
            double total = transaction.getTotal();
            int pointsApplied = transaction.getPointsApplied();
            ArrayList<TransactionItem> items = transaction.getItems();

            if (transaction.getChange() < 0) {
                return "Error: Insufficient Funds";
            }
            JSONArray itemsArray = new JSONArray();
            for (TransactionItem item : items) {
                JSONObject itemObj = new JSONObject();
                itemObj.put("product_id", item.getProductId());
                itemObj.put("quantity", item.getQuantity());
                itemObj.put("price", item.getPrice());
                if (item.getDiscount() != null) {
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

    public ArrayList<Transaction> getTransactionHistory() {
        ArrayList<Transaction> transactions = new ArrayList<>();
        try {
            JSONObject response = api.getTransactions();
            JSONArray transactionArray = response.getJSONArray("transactions");

            for (int i = 0; i < transactionArray.length(); i++) {
                JSONObject transactionObj = transactionArray.getJSONObject(i);
                int transactionId = transactionObj.getInt("transaction_id");
                Date transactionDate = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz").parse(transactionObj.getString("transaction_date"));
                int employeeId = transactionObj.getInt("employee_id");
                int customerId = transactionObj.getInt("customer_id");
                double total = transactionObj.getDouble("total");
                String paymentMethod = transactionObj.getString("payment_method");

                JSONArray itemsArray = transactionObj.getJSONArray("items");
                ArrayList<TransactionItem> items = new ArrayList<>();
                for (int j = 0; j < itemsArray.length(); j++) {
                    JSONObject itemObj = itemsArray.getJSONObject(j);
                    int transactionItemId = itemObj.getInt("transaction_item_id");
                    int productId = itemObj.getInt("product_id");
                    int quantity = itemObj.getInt("quantity");
                    double price = itemObj.getDouble("price");

                    items.add(new TransactionItem(transactionItemId, productId, quantity, price));
                }

                JSONArray refundItemsArray = transactionObj.getJSONArray("refund_items");
                ArrayList<RefundItem> refundItems = new ArrayList<>();
                for (int k = 0; k < refundItemsArray.length(); k++) {
                    JSONObject refundItemObj = refundItemsArray.getJSONObject(k);
                    int refundItemId = refundItemObj.getInt("refund_id");
                    int transactionItemId = refundItemObj.getInt("transaction_item_id");
                    int refundedQuantity = refundItemObj.getInt("quantity");
                    boolean restock = refundItemObj.getInt("restock") != 0;
                    double refundedAmount = refundItemObj.getDouble("amount");

                    refundItems.add(new RefundItem(refundItemId, transactionItemId, refundedQuantity, refundedAmount, restock));

                }

                transactions.add(new Transaction(transactionId, transactionDate, customerId, employeeId, total, items, paymentMethod, refundItems));
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
            if (currentCustomer == null) {
                return "No customer selected.";
            }

            if (currentCustomer.getPointsBalance() >= pointsToRedeem) {
                int newPointsBalance = currentCustomer.getPointsBalance() - pointsToRedeem;
                currentCustomer.setPointsBalance(newPointsBalance);
                Transaction transaction = getPendingTransactionByID(orderId);
                transaction.setPointsApplied(pointsToRedeem);
                return "Points redeemed successfully. New points balance: " + newPointsBalance;
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
                for (PaymentType paymentType : paymentTypes) {
                    if (paymentType.getId() == id) {
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
                for (PaymentType paymentType : paymentTypes) {
                    if (paymentType.getId() == id) {
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

    public String processRefund(int transactionId, ArrayList<RefundItem> items, int employeeId) {
        String feedback = "";
        try {
            JSONObject refundInfo = new JSONObject();
            JSONArray itemsArray = new JSONArray();
            double amount = 0;
            for (RefundItem item : items) {
                JSONObject itemObj = new JSONObject();
                itemObj.put("product_id", item.getProductId());
                itemObj.put("transaction_item_id", item.getTransactionItemId());
                itemObj.put("quantity", item.getQuantity());
                itemObj.put("price", item.getPrice());
                itemObj.put("restock", item.isRestock());
                itemsArray.put(itemObj);
                amount += item.getQuantity() * item.getPrice();
            }
            refundInfo.put("items", itemsArray);
            refundInfo.put("employee_id", employeeId);

            amount = Math.round(amount * 100.0) / 100.0;
            JSONObject response = api.processRefund(transactionId, itemsArray, employeeId, amount);

            if (response.has("error")) {
                feedback = "Error performing transaction:" + response.getString("error");
            } else {
                feedback = "Transaction successful!";
            }
        } catch (Exception e) {
            e.printStackTrace();
            feedback = "Error performing transaction: " + e.getMessage();
        }
        return feedback;
    }

    public String addDiscount(String discountCode, String discountName, double discountPercent, Date startDate, Date endDate, String discountType, ArrayList<Integer> applicable_ids) {
        String feedback = "";
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String startDateStr = dateFormat.format(startDate);
            String endDateStr = dateFormat.format(endDate);

            JSONObject response = api.addDiscount(discountCode, discountName, discountPercent, startDateStr, endDateStr, applicable_ids, discountType);

            if (response.has("error")) {
                feedback = "Error adding discount: " + response.getString("error");
            } else {
                int id = response.getInt("id");
                loadDiscounts();

                feedback = "Discount added successfully!";
            }
        } catch (Exception e) {
            e.printStackTrace();
            feedback = "Error adding discount: " + e.getMessage();
        }
        return feedback;
    }

    public Discount getDiscountByID(int id) {
        for (Discount discount : discounts) {
            if (discount.getId() == id) {
                return discount;
            }
        }
        return null;
    }

    public String updateDiscount(int discID, String discountCode, String discountName, double discountPercent, Date startDate, Date endDate, String discountType, ArrayList<Integer> applicable_ids) {
        String feedback = "";
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String startDateStr = dateFormat.format(startDate);
            String endDateStr = dateFormat.format(endDate);

            JSONObject response = api.updateDiscount(discID, discountCode, discountName, discountPercent, startDateStr, endDateStr, applicable_ids, discountType);

            if (response.has("error")) {
                feedback = "Error updating discount: " + response.getString("error");
            } else {
                feedback = "Discount updated successfully!";
                loadDiscounts(); 
            }
        } catch (Exception e) {
            e.printStackTrace();
            feedback = "Error updating discount: " + e.getMessage();
        }
        return feedback;
    }

    public String deleteDiscount(int id) {
        String feedback = "";
        try {
            JSONObject response = api.deleteDiscount(id);
            if (response.has("error")) {
                feedback = "Error deleting discount: " + response.getString("error");
            } else {
                feedback = "Discount updated successfully!";
                loadDiscounts();
            }
            System.out.println("Response from server: " + response.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return feedback;
    }

}
