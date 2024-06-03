 /* Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PointOfSale;

/**
 *
 * @author britt
 */
public class ProductDiscount extends Discount {
    private int productId;

    public ProductDiscount(int discountId, String name, String description, double amount, int productId) {
        super(discountId, name, description, amount);
        this.productId = productId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    @Override
    public String toString() {
        return "BrandDiscount{" +
                "productId='" + productId + '\'' +
                "} " + super.toString();
    }
}
