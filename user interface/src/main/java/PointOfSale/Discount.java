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
public class Discount {
    private int id;
    private String name;
    private String code;
    private double amount;
    private ArrayList<String> selectProducts;
    private ArrayList<String> selectBrands;
    private ArrayList<String> selectCategories;
    

    public Discount(int id, String name, String code, double amount) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.amount = amount;
    }

    // Getters and Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Discount{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", amount=" + amount +
                '}';
    }
}

