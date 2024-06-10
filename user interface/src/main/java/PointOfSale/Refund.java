/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PointOfSale;

import java.util.ArrayList;
/**
 *
 * @author britt
 */



public class Refund {
    private int transactionId;
    private int employeeId;
    private double moneyRefunded;
    private ArrayList<RefundItem> items;
    
    public Refund(int transactionId, int employeeId, ArrayList<RefundItem> items) {
        this.transactionId = transactionId;
        this.employeeId = employeeId;
        this.items = items;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }
    
    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public double getMoneyRefunded() {
        return moneyRefunded;
    }

    public void calculateTotal() {
        double sum = 0;
        for (RefundItem item : items){
            sum += item.getPrice();
        }
        this.moneyRefunded = sum * 1.15;
        this.moneyRefunded = Math.round(this.moneyRefunded * 100.0) / 100.0;
    }
    
    public ArrayList<RefundItem> getItems() {
        return items;
    }

    public void setItems(ArrayList<RefundItem> items) {
        this.items = items;
    }

    /*
    public RefundItem getItemByID(int id){
        for (RefundItem item : items){
            if (item.getProductId() == id){
                return item;
            }
        }
        return null;
    }*/

    public void addRefundItem(RefundItem refundItem) {
        this.items.add(refundItem);
    }

    public void removeRefundItem(RefundItem refundItem) {
        this.items.remove(refundItem);
    }

    @Override
    public String toString() {
        return "Refund{" +
                "transactionId=" + transactionId +
                ", employeeId=" + employeeId +
                ", moneyRefunded=" + moneyRefunded +
                ", items=" + items +
                '}';
    }
}
