/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PointOfSale;

/**
 *
 * @author britt
 */
public class RefundItem {
    private int transactionItemId;
    private int productId;
    private int quantity;
    private int refundId;
    private double price;
    private boolean restock;

    public RefundItem(int refundId, int productId, int transactionItemId, int quantity, double price, boolean restock) {
        this.refundId = refundId;
        this.transactionItemId = transactionItemId;
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
        this.restock = restock;
    }
    
    public RefundItem(int transactionItemId, int productId, int quantity, double price, boolean restock) {
        this.transactionItemId = transactionItemId;
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
        this.restock = restock;
    }

    public int getTransactionItemId() {
        return transactionItemId;
    }

    public void setTransactionItemId(int transactionItemId) {
        this.transactionItemId = transactionItemId;
    }

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

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isRestock() {
        return restock;
    }

    public void setRestock(boolean restock) {
        this.restock = restock;
    }

    @Override
    public String toString() {
        return "RefundItem{" +
                "transactionItemId=" + transactionItemId +
                //", productId=" + productId +
                ", quantity=" + quantity +
                ", price=" + price +
                ", restock=" + restock +
                '}';
    }
}
