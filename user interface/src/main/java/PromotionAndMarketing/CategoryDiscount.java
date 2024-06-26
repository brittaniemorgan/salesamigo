/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PromotionAndMarketing;

import Inventory.Category;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author britt
 */

public class CategoryDiscount extends Discount {
    private int categoryId;
    private ArrayList<Category> categories;

    public CategoryDiscount(int id,String discountCode, String discountName, double discountPercent,int categoryId) {
        super(id, discountCode, discountName, discountPercent);
        this.categoryId = categoryId;
    }
    
    public CategoryDiscount(int id,String discountCode, String discountName, double discountPercent, Date startDate, Date endDate, int categoryId) {
        super(id, discountCode, discountName, discountPercent, startDate, endDate);
        this.categoryId = categoryId;
    }
    
    public CategoryDiscount(int id,String discountCode, String discountName, double discountPercent, Date startDate, Date endDate) {
        super(id, discountCode, discountName, discountPercent, startDate, endDate);
        this.categories = new ArrayList<>();
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }
    
    public ArrayList<Category> getApplicableCategories(){
        return this.categories;
    }
    
    public void addCategory(Category category){
        this.categories.add(category);
    }

    @Override
    public String toString() {
        return "CategoryDiscount{" +
                "categoryId='" + categoryId + '\'' +
                "} " + super.toString();
    }
}

