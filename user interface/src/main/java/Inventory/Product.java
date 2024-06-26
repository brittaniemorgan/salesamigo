/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Inventory;

import PromotionAndMarketing.Discount;
import java.util.ArrayList;

/**
 *
 * @author britt
 */
public class Product {
    private int id;
    private String name;
    private String description;
    private double price;
    private String category;
    private String brand;
    private String gender;
    private ArrayList<ProductVariant> variants;
    private ArrayList<Discount> discounts;

    // Constructor
    public Product(int id, String name, String description, double price, String category, String brand, String gender) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.brand = brand;
        this.gender = gender;
        this.variants = new ArrayList<ProductVariant>();
    }

    // Getters and Setters
    public int getID() {
        return id;
    }

    public void setID(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
    
    public void addDiscount(Discount discount){
        this.discounts.add(discount);
    }
    
    public ArrayList<Discount> getDiscounts(){
        return this.discounts;
    }
    
    public void addVariant(ProductVariant variant){
        this.variants.add(variant);
    }
    
    public ArrayList<ProductVariant> getVariants(){
        return this.variants;
    }

    // Override toString method for debugging
    @Override
    public String toString() {
        return "Product{" +
                "productId=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", category='" + category + '\'' +
                ", brand='" + brand + '\'' +
                ", gender=" + gender +
                '}';
    }
}

