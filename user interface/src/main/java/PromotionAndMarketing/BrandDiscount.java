/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PromotionAndMarketing;

import Inventory.Brand;
import java.util.Date;
import java.util.ArrayList;

/**
 *
 * @author britt
 */
public class BrandDiscount extends Discount {
    private int brandId;
    private ArrayList<Brand> brands;

    public BrandDiscount(int id,String discountCode, String discountName, double discountPercent, int brandId) {
        super(id, discountCode, discountName, discountPercent);
        this.brandId = brandId;
    }

    public BrandDiscount(int id,String discountCode, String discountName, double discountPercent, Date startDate, Date endDate, int brandId) {
        super(id, discountCode, discountName, discountPercent, startDate, endDate);
        this.brandId = brandId;
    }
    
    public BrandDiscount(int id,String discountCode, String discountName, double discountPercent, Date startDate, Date endDate) {
        super(id, discountCode, discountName, discountPercent, startDate, endDate);
        this.brands = new ArrayList<>();
    }

    public int getBrandId() {
        return brandId;
    }

    public void setBrandId(int brandId) {
        this.brandId = brandId;
    }
    
    public ArrayList<Brand> getApplicableBrands(){
        return this.brands;
    }
    
    public void addBrand(Brand brand){
        this.brands.add(brand);
    }

    @Override
    public String toString() {
        return "BrandDiscount{" +
                "brandId='" + brandId + '\'' +
                "} " + super.toString();
    }
}
