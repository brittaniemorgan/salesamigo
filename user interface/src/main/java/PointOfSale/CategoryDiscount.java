/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PointOfSale;

/**
 *
 * @author britt
 */

public class CategoryDiscount extends Discount {
    private int categoryId;

    public CategoryDiscount(int discountId, String name, String description, double amount, int categoryId) {
        super(discountId, name, description, amount);
        this.categoryId = categoryId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public String toString() {
        return "CategoryDiscount{" +
                "categoryId='" + categoryId + '\'' +
                "} " + super.toString();
    }
}

