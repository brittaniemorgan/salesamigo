/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ReportingAndAnalytics;

/**
 *
 * @author britt
 */
public class ProductRecommendation {
    private int productId;
    private String brand;
    private String category;
    private String color;
    private String gender;
    private double price;

    // Constructor
    public ProductRecommendation(int productId, String brand, String category, String color, String gender, double price) {
        this.productId = productId;
        this.brand = brand;
        this.category = category;
        this.color = color;
        this.gender = gender;
        this.price = price;
    }

    // Getters and Setters
    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    // toString method
    @Override
    public String toString() {
        return "ProductRecommendation{" +
                "productId=" + productId +
                ", brand='" + brand + '\'' +
                ", category='" + category + '\'' +
                ", color='" + color + '\'' +
                ", gender='" + gender + '\'' +
                ", price=" + price +
                '}';
    }

    public static void main(String[] args) {
        // Example usage
        ProductRecommendation product = new ProductRecommendation(6, "Zara", "Handbag", "pink", "Female", 129.99);
        System.out.println(product);
    }
}
