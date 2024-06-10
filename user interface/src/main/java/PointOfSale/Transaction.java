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
    private double amtGiven;
    private ArrayList<TransactionItem> items;
    private ArrayList<RefundItem> refundItems;
    private String paymentMethod;
    private int pointsApplied;
    
    public Transaction (){
        this.items = new ArrayList<TransactionItem>();
        this.total = 0;
        this.pointsApplied = 0;
    }
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
        this.total = 0.0;
        for (TransactionItem item : items){
            this.total += item.getTotal();
        }
        
        
    }
    
    public Transaction(int transactionId, Date transactionDate, int customerId, int employeeId, double total, ArrayList<TransactionItem> items, String paymentMethod, ArrayList<RefundItem> refundItems) {
        this.transactionId = transactionId;
        this.transactionDate = transactionDate;
        this.customerId = customerId;
        this.employeeId = employeeId;        
        this.items = items;
        this.paymentMethod = paymentMethod;
        this.total = 0.0;
        for (TransactionItem item : items){
            this.total += item.getTotal();
        }
        this.refundItems = refundItems;
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
        double sum = 0.0;
        for (TransactionItem item : items){
            sum += item.getTotal();
        }//update
        this.total = total - (pointsApplied/100);
        this.total = sum;
        this.total = Math.round(this.total*  100.0)/100.0;
    }
    
    public void updateTotal(double num) {
        //update
        for (TransactionItem item : items){
            this.total += num;
            this.total = Math.round(this.total*  100.0)/100.0;
        }
    }
    
    public void setAmtGiven(double amt) {
        this.amtGiven = amt;
    }
    
    public double getChange(){
        calculateTotal();
        double finalTotal = this.total * 1.15;
        return Math.round((this.amtGiven - finalTotal)*100.0)/100.0;       
    }

    public ArrayList<TransactionItem> getItems() {
        return items;
    }

    public void setItems(ArrayList<TransactionItem> items) {
        this.items = items;
    }
    
    public ArrayList<RefundItem> getRefundItems() {
        return refundItems;
    }

    public void setRefundItems(ArrayList<RefundItem> items) {
        this.refundItems = items;
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
    
    //Updateed
    public int getPointsApplied() {
        return this.pointsApplied;
    }
    //updated
    public void setPointsApplied(int points) {
        this.pointsApplied = points;
        this.total -= points/10;
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
