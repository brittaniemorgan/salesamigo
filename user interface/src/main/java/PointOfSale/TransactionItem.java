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
    private double price;
    private double total;

    public TransactionItem(int productId, int quantity, double price) {
        this.productId = productId;
        //this.transaction = transaction;
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
    
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }
    
    public double getTotal() {
        return total;
    }
    
    public Transaction getTransaction() {
        return transaction;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "productId=" + productId +
                ", quantity=" + quantity +
                ", price=" + price +
                '}';
    }
}

