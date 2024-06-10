/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PointOfSale;

/**
 *
 * @author britt
 */

public class TransactionItem {
    private int productId;
    private Transaction transaction;
    private int quantity;
    private int transactionItemId;
    private double price;
    private double total;
    private Discount discount;

    public TransactionItem(int productId, int quantity, double price) {
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
        this.total = quantity * price;          
    }
    
    public TransactionItem(int transactionItemId, int productId, int quantity, double price) {
        this.transactionItemId = transactionItemId;
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
        this.total = quantity * price;
    }

    // Getters and Setters

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }
    
    public int getTransactionItemId() {
        return transactionItemId;
    }

    public void setTransactionItemId(int transactionItemId) {
        this.transactionItemId = transactionItemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        updateTotal();
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
        updateTotal();
    }

    public double getTotal() {       
        return total;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public Discount getDiscount() {
        return discount;
    }

    public void setDiscount(Discount discount) {
        this.discount = discount;
        updateTotal();
    }

    // Method to update total based on quantity, price, and discount
    private void updateTotal() {
        double discountedPrice = price;

        if (discount != null) {
            discountedPrice = price * (1 - (discount.getAmount()/100)); 
        }

        this.total = quantity * discountedPrice;
    }

    @Override
    public String toString() {
        return "TransactionItem{" +
                "productId=" + productId +
                ", quantity=" + quantity +
                ", price=" + price +
                ", total=" + total +
                ", discount=" + (discount != null ? discount.getName() : "No Discount") +
                '}';
    }
}
