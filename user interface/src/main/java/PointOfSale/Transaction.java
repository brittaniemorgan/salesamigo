/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PointOfSale;

/**
 *
 * @author britt
 */
import java.util.ArrayList;
import java.util.Date;

public class Transaction {
    private int transactionId;
    private Date transactionDate;
    private int customerId;
    private int employeeId;
    private double total;
    private ArrayList<TransactionItem> items;
    private String paymentMethod;
    private int pointsApplied;
    
    public Transaction(int transactionId, Date transactionDate, int customerId, int employeeId) {
        this.transactionId = transactionId;
        this.transactionDate = transactionDate;
        this.customerId = customerId;
        this.employeeId = employeeId;
        this.items = new ArrayList<TransactionItem>();
        this.total = 0;
        this.pointsApplied = 0;
    }

    public Transaction(int transactionId, Date transactionDate, int customerId, int employeeId, double total, ArrayList<TransactionItem> items, String paymentMethod) {
        this.transactionId = transactionId;
        this.transactionDate = transactionDate;
        this.customerId = customerId;
        this.employeeId = employeeId;        
        this.items = items;
        this.paymentMethod = paymentMethod;
        this.total = 0;
        for (TransactionItem item : items){
            this.total += item.getTotal();
        }
    }

    // Getters and Setters

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public double getTotal() {
        return total;
    }

    public void calculateTotal() {
        int sum = 0;
        for (TransactionItem item : items){
            sum += item.getTotal();
        }
        this.total = total - (pointsApplied/100);
        this.total = sum;
    }
    
    public void updateTotal(double num) {
        for (TransactionItem item : items){
            this.total += num;
        }
    }

    public ArrayList<TransactionItem> getItems() {
        return items;
    }

    public void setItems(ArrayList<TransactionItem> items) {
        this.items = items;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
    
    public TransactionItem getItemByID(int id){
        for (TransactionItem item : items){
            if (item.getProductId() == id){
                return item;
            }
        }
        return null;
    }

    // Method to add an transaction item
    public void addTransactionItem(TransactionItem transactionItem) {
        this.items.add(transactionItem);
    }

    // Method to remove an transaction item
    public void removeTransactionItem(TransactionItem transactionItem) {
        this.items.remove(transactionItem);
    }
    
    public void setPointsApplied(int points) {
        this.pointsApplied = points;
        this.total -= points/100;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "transactionId=" + transactionId +
                ", transactionDate=" + transactionDate +
                ", customerId=" + customerId +
                ", employeeId=" + employeeId +
                ", total=" + total +
                ", items=" + items +
                ", paymentMethod='" + paymentMethod + '\'' +
                '}';
    }
}
