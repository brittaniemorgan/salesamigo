 /* Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PointOfSale;

import Inventory.ProductVariant;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author britt
 */
public class ProductDiscount extends Discount {
    private int productId;
    private ArrayList<ProductVariant> products;

    public ProductDiscount(int discountId, String name, String description, double amount, int productId) {
        super(discountId, name, description, amount);
        this.productId = productId;
    }
    
    public ProductDiscount(int id,String discountCode, String discountName, double discountPercent, Date startDate, Date endDate, int productId) {
        super(id, discountCode, discountName, discountPercent, startDate, endDate);
        this.productId = productId;
    }
    
    public ProductDiscount(int id,String discountCode, String discountName, double discountPercent, Date startDate, Date endDate) {
        super(id, discountCode, discountName, discountPercent, startDate, endDate);
        this.products = new ArrayList<>();
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }
    
    public ArrayList<ProductVariant> getApplicableProducts(){
        return this.products;
    }
    
    public void addProduct(ProductVariant product){
        this.products.add(product);
    }

    @Override
    public String toString() {
        return "BrandDiscount{" +
                "productId='" + productId + '\'' +
                "} " + super.toString();
    }
}
