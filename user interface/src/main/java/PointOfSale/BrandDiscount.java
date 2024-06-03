/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PointOfSale;

/**
 *
 * @author britt
 */
public class BrandDiscount extends Discount {
    private int brandId;

    public BrandDiscount(int discountId, String name, String description, double amount, int brandId) {
        super(discountId, name, description, amount);
        this.brandId = brandId;
    }

    public int getBrandId() {
        return brandId;
    }

    public void setBrandId(int brandId) {
        this.brandId = brandId;
    }

    @Override
    public String toString() {
        return "BrandDiscount{" +
                "brandId='" + brandId + '\'' +
                "} " + super.toString();
    }
}
