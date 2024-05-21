/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Inventory;

import Database.APIManager;
import java.io.IOException;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author britt
 */
public class InventoryController {
    APIManager api;
    ArrayList<Product> products;  //for now
    
    public InventoryController(){
        try{
            api = APIManager.getAPIManager();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        try {
            JSONObject response = new JSONObject(api.fetchDataFromAPI("products"));
            JSONArray productsArray = response.getJSONArray("products");
            products = new ArrayList<Product>();
            for (int i = 0; i < productsArray.length(); i++) {
                JSONObject product = productsArray.getJSONObject(i);
                products.add(new Product(
                    product.getInt("product_id"),
                    product.getString("name"),
                    product.getString("category"),
                    product.getDouble("price"),
                    product.getString("description"),
                    product.getInt("minimum_quantity"),
                    product.getInt("quantity")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public ArrayList<Product> getProducts(){
        return products;
    }
    
    public void addProduct(String name, String category, double price, String description,
                           int minQuantity, int quantity) {
        try {
            JSONObject productInfo = new JSONObject();
            productInfo.put("name", name);
            productInfo.put("category", category);
            productInfo.put("price", price);
            productInfo.put("description", description);
            productInfo.put("min_quantity", minQuantity);
            productInfo.put("quantity", quantity);

            String response = api.sendDataToAPI("products", productInfo);
            System.out.println("Response from server: " + response.toString());
            products.add(new Product(products.size()+1, name, category, price, description, minQuantity, quantity));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void updateProduct(int productID, String name, String category, double price, String description,
                           int minQuantity, int quantity) {
        try {
            JSONObject productInfo = new JSONObject();
            productInfo.put("product_id", productID);
            productInfo.put("name", name);
            productInfo.put("category", category);
            productInfo.put("price", price);
            productInfo.put("description", description);
            productInfo.put("min_quantity", minQuantity);
            productInfo.put("quantity", quantity);
            
            for (Product product : products){
                if (product.getID() == productID){
                    product.setName(name);
                    product.setCategory(category);
                    product.setDescription(description);
                    product.setPrice(price);
                    product.setMinQuantity(minQuantity);
                    product.setQuantity(quantity);
                    System.out.println(name);
                    break;
                }
            }

            String response = api.sendPutRequestToAPI("products", productInfo);
            System.out.println("Response from server: " + response.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//delete?
    
    public static void main(String[] args) throws IOException {
        try {
            InventoryController inv = new InventoryController();
            System.out.println(inv.products);
            //inv.updateProduct(6, "name", "category1", 82.0, "description", 5, 10);
        } catch (Exception e) {
            e.printStackTrace();
        } 
        
    }
}
