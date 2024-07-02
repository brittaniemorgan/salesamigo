/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Inventory;

/**
 *
 * @author britt
 */
public class ProductVariant {
    private int variantId;
    private Product product;
    private String size;
    private String colour;
    private int quantity;
    private double price;
    private int minQuantity;

    public ProductVariant(int variantId, Product product, String size, String colour, int quantity, double price, int minQuantity) {
        this.variantId = variantId;
        this.product = product;
        this.size = size;
        this.colour = colour;
        this.quantity = quantity;
        this.price = price;
        this.minQuantity = minQuantity;
    }

    public int getVariantID() {
        return variantId;
    }

    public void setVariantID(int variantId) {
        this.variantId = variantId;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
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

    public int getMinQuantity() {
        return minQuantity;
    }

    public void setMinQuantity(int minQuantity) {
        this.minQuantity = minQuantity;
    }
    
    public boolean isLowStock() {
        return quantity < minQuantity;
    }
}

