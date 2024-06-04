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
public class Inventory {
    APIManager api;
    ArrayList<Product> products; 
    ArrayList<Category> categories; 
    ArrayList<Brand> brands; 
    ArrayList<Size> sizes; 
    
    public Inventory(){
        try{
            api = APIManager.getAPIManager();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        //Loading products
        try {
            JSONObject response = new JSONObject(api.fetchDataFromAPI("products"));
            JSONArray productsArray = response.getJSONArray("products");
            products = new ArrayList<Product>();
            for (int i = 0; i < productsArray.length(); i++) {
                JSONObject product = productsArray.getJSONObject(i);
                products.add(new Product(
                    product.getInt("product_id"),
                    product.getString("name"),
                    product.getString("description"),  
                    product.getDouble("price"),
                    product.getString("category"),
                    product.getString("brand"),
                    product.getString("gender")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        //Loading variants
        try {
            JSONObject response = api.getVariants();
            JSONArray variantsArray = response.getJSONArray("product_variations");
            
            for (int i = 0; i < variantsArray.length(); i++) {
                JSONObject variantObj = variantsArray.getJSONObject(i);
                Product product = findProductByID(variantObj.getInt("product_id"));
                ProductVariant variant = new ProductVariant(
                    variantObj.getInt("variant_id"),
                    product,
                    variantObj.getString("size"),  
                    variantObj.getString("color"),
                    variantObj.getInt("quantity"),
                    variantObj.getDouble("price"),
                    variantObj.getInt("min_quantity"));
                
                product.addVariant(variant);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        //Loading categories
        categories = new ArrayList<>();
        try{
            JSONObject response = api.getCategories();
            JSONArray categoriesArray = response.getJSONArray("categories");

            for (int i = 0; i < categoriesArray.length(); i++) {
                JSONObject categoryObject = categoriesArray.getJSONObject(i);
                int id = categoryObject.getInt("category_id");
                String name = categoryObject.getString("category");
                categories.add(new Category(id, name));

            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        //Loading brands
        brands = new ArrayList<>();
        try{
            JSONObject response = api.getBrands();
            JSONArray brandsArray = response.getJSONArray("brands");

            for (int i = 0; i < brandsArray.length(); i++) {
            JSONObject brandObject = brandsArray.getJSONObject(i);
            int id = brandObject.getInt("brand_id");
            String name = brandObject.getString("brand");
            brands.add(new Brand(id, name));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        //Loading sizes
        sizes = new ArrayList<>();
        try{
            JSONObject response = api.getSizes();
            JSONArray sizesArray = response.getJSONArray("sizes");

            for (int i = 0; i < sizesArray.length(); i++) {
                JSONObject sizeObject = sizesArray.getJSONObject(i);
                int id = sizeObject.getInt("size_id");
                String name = sizeObject.getString("size");
                sizes.add(new Size(id, name));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public ArrayList<Product> getProducts(){
        return products;
    }
    
    public String addProduct(String name, String description, double price, String category, String brand, String gender) {
        String feedback = "";
        try {
            
            int category_id = findCategoryByName(category).getId();
            int brand_id = findBrandByName(brand).getId();
            JSONObject response = api.addProduct(name, description, price, category_id, brand_id, gender);
            
            if (response.has("error")) {
                feedback = "Error adding product: " + response.getString("error");                
            } else {
                int id = response.getInt("id");
                products.add(new Product(id, name, description, price, category, brand, gender));
                feedback = "Product added successfully!";
            }            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return feedback;
    }
    
    public String updateProduct(int productID, String name, String category, double price, String description,
                           String gender, String brand) {
        String feedback = "";
        try {
            JSONObject productInfo = new JSONObject();
            int category_id = findCategoryByName(category).getId();
            int brand_id = findBrandByName(brand).getId();
                       
            JSONObject response = api.updateProduct(productID, name, description, price, category_id, brand_id, gender);
            
            if (response.has("error")) {
                feedback = "Error adding product: " + response.getString("error");                
            } else {
                for (Product product : products){
                    if (product.getID() == productID){
                        product.setName(name);
                        product.setCategory(category);
                        product.setDescription(description);
                        product.setPrice(price);
                        product.setGender(gender);
                        product.setBrand(brand);
                        break;
                    }
                }
                feedback = "Product updated successfully!";
            } 
            System.out.println("Response from server: " + response.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return feedback;
    }

    public String deleteProduct(int productID) {
        String feedback = "";
        try {
            
            JSONObject response = api.deleteProduct(productID);
            if (response.has("error")) {
                feedback = "Error adding product: " + response.getString("error");                
            } else {
                for (Product product : products){
                    if (product.getID() == productID){
                        products.remove(product);
                        feedback = "Product deleted successfully!";
                        break;
                    }
                }
            } 
            System.out.println("Response from server: " + response.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return feedback;
    }
    
    public String createVariant(Product product, String size, String colour, int quantity, double price, int minQuantity) {
        String feedback = "";
        try {            
            int size_id = findSizeByName(size).getId();
            JSONObject response = api.addVariant(product.getID(), size_id, colour, quantity, price, minQuantity);
            
            
            if (response.has("error")) {
                feedback = "Error adding variant: " + response.getString("error");                
            } else {
                int variantId = response.getInt("id");
                product.addVariant(new ProductVariant(variantId, product, size, colour, quantity, price, minQuantity));
                feedback = "Variant added successfully!";
            }            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return feedback;
    }
    
    public String updateVariant(int variantID, Product product, String size, String colour, int quantity, double price, int minQuantity) {
        String feedback = "";
        try {
            int size_id = findSizeByName(size).getId();
                       
            JSONObject response = api.updateVariant(variantID, product.getID(), size_id, colour, quantity, price, minQuantity);
            
            if (response.has("error")) {
                feedback = "Error updating variant: " + response.getString("error");                
            } else {
                for (ProductVariant variant : product.getVariants()){
                    if (variant.getVariantID() == variantID){
                        variant.setSize(size);
                        variant.setColour(colour);
                        variant.setQuantity(quantity);
                        variant.setPrice(price);
                        variant.setMinQuantity(minQuantity);
                        break;
                    }
                }
                feedback = "Variant updated successfully!";
            } 
            System.out.println("Response from server: " + response.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return feedback;
    }

    public String deleteVariant(int variantID) {
        String feedback = "";
        try {
            
            JSONObject response = api.deleteVariant(variantID);
            if (response.has("error")) {
                feedback = "Error deleting variant: " + response.getString("error");                
            } else {
                for (Product product : products){
                    for (ProductVariant productVariant : product.getVariants()) {
                        if (productVariant.getVariantID() == variantID) {
                            product.getVariants().remove(productVariant);
                            feedback = "Variant deleted successfully!";
                            break;
                        }
                    }
                }
            } 
            System.out.println("Response from server: " + response.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return feedback;
    }
    
    public ArrayList<Category> getCategories() {
        return categories;
    }
    
    public String createCategory(String name) {
        String feedback = "";
        try {            
            JSONObject response = api.addCategory(name);
            
            if (response.has("error")) {
                feedback = "Error adding category: " + response.getString("error");                
            } else {
                int catID = response.getInt("id");
                categories.add(new Category(catID, name));
                feedback = "Category added successfully!";
            }            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return feedback;
    }
    
    public String updateCategory(int id, String name) {
        String feedback = "";
        try {
            JSONObject response = api.updateCategory(id, name);
            
            if (response.has("error")) {
                feedback = "Error updating category: " + response.getString("error");                
            } else {
                for (Category category : categories){
                    if (category.getId() == id){
                        category.setName(name);
                        break;
                    }
                }
                feedback = "Category updated successfully!";
            } 
            System.out.println("Response from server: " + response.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return feedback;
    }

    public String deleteCategory(int id) {
        String feedback = "";
        try {
            
            JSONObject response = api.deleteCategory(id);
            if (response.has("error")) {
                feedback = "Error deleting category: " + response.getString("error");                
            } else {
                for (Category category : categories){
                    if (category.getId() == id){
                        categories.remove(category);
                        feedback = "Category deleted successfully!";
                        break;
                    }
                }
            } 
            System.out.println("Response from server: " + response.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return feedback;
    }
    
    public ArrayList<Brand> getBrands() {
        return brands;
    }
    
    public String createBrand(String name) {
        String feedback = "";
        try {            
            JSONObject response = api.addBrand(name);
            
            if (response.has("error")) {
                feedback = "Error adding brand: " + response.getString("error");                
            } else {
                int id = response.getInt("id");
                brands.add(new Brand(id, name));
                feedback = "Brand added successfully!";
            }            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return feedback;
    }
    
    public String updateBrand(int id, String name) {
        String feedback = "";
        try {
            JSONObject response = api.updateBrand(id, name);
            
            if (response.has("error")) {
                feedback = "Error updating brand: " + response.getString("error");                
            } else {
                for (Brand brand : brands){
                    if (brand.getId() == id){
                        brand.setName(name);
                        feedback = "Brand updated successfully!";
                        break;
                    }
                }
                
            } 
            System.out.println("Response from server: " + response.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return feedback;
    }

    public String deleteBrand(int id) {
        String feedback = "";
        try {
            
            JSONObject response = api.deleteBrand(id);
            if (response.has("error")) {
                feedback = "Error deleting brand: " + response.getString("error");                
            } else {
                for (Brand brand : brands){
                    if (brand.getId() == id){
                        brands.remove(brand);
                        feedback = "Brand deleted successfully!";
                        break;
                    }
                }
            } 
            System.out.println("Response from server: " + response.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return feedback;
    }
    
    public ArrayList<Size> getSizes() {
        return sizes;
    }
    
    public String createSize(String name) {
        String feedback = "";
        try {            
            JSONObject response = api.addSize(name);
                        
            if (response.has("error")) {                
                feedback = "Error adding size: " + response.getString("error");      
                System.out.println(feedback);
            } else {
                int id = response.getInt("id");
                sizes.add(new Size(id, name));
                feedback = "Size added successfully!";
            }            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return feedback;
    }
    
    public String updateSize(int id, String name) {
        String feedback = "";
        try {
            JSONObject response = api.updateSize(id, name);
            
            if (response.has("error")) {
                feedback = "Error updating size: " + response.getString("error");                
            } else {
                for (Size size : sizes){
                    if (size.getId() == id){
                        size.setName(name);
                        break;
                    }
                }
                feedback = "Size updated successfully!";
            } 
            System.out.println("Response from server: " + response.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return feedback;
    }

    public String deleteSize(int id) {
        String feedback = "";
        try {
            
            JSONObject response = api.deleteSize(id);
            if (response.has("error")) {
                feedback = "Error deleting size: " + response.getString("error");                
            } else {
                for (Size size : sizes){
                    if (size.getId() == id){
                        sizes.remove(size);
                        feedback = "Size deleted successfully!";
                        break;
                    }
                }
            } 
            System.out.println("Response from server: " + response.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return feedback;
    }
    
    public ArrayList<ProductVariant> getVariants() {
        ArrayList<ProductVariant> variants = new ArrayList<ProductVariant>();
        for (Product product : products) {
            for (ProductVariant productVariant : product.getVariants()) {
                variants.add(productVariant);
            }
        }
        return variants;
    }
    
    public Category findCategoryByName(String name) {
        for (Category category : categories) {
            if (category.getName().equals(name)) {
                return category;
            }
        }
        return null; 
    }
    
    public Product findProductByID(int id) {
        for (Product product : products) {
            if (product.getID() == id) {
                return product;
            }
        }
        return null;
    }
    
    public ProductVariant findVarantByID(int id) {
        for (Product product : products) {
            for (ProductVariant productVariant : product.getVariants()) {
                if (productVariant.getVariantID() == id) {
                    return productVariant;
                }
            }    
        }
        return null;
    }   

    public Brand findBrandByName(String name) {
        for (Brand brand : brands) {
            if (brand.getName().equals(name)) {
                return brand;
            }
        }
        return null;
    }

    public Size findSizeByName(String name) {
        for (Size size : sizes) {
            if (size.getName().equals(name)) {
                return size;
            }
        }
        return null; 
    }
    
    public ArrayList<Product> searchProducts(String query) {
        ArrayList<Product> filteredProducts = new ArrayList<>();
        for (Product product : products) {
            if (product.getName().toLowerCase().contains(query.toLowerCase()) ||
                product.getCategory().toLowerCase().contains(query.toLowerCase()) ||
                product.getDescription().toLowerCase().contains(query.toLowerCase())) {
                filteredProducts.add(product);
            }
        }
        return filteredProducts;
    }
    
    public ArrayList<Product> getProductsByNameOrBrand(String query) {
        ArrayList<Product> filteredProducts = new ArrayList<Product>();
        for (Product product : products) {
            if (product.getBrand().toLowerCase().contains(query) || product.getName().toLowerCase().contains(query)) {
                filteredProducts.add(product);
            }
        }
        return filteredProducts;
    }
    
    public static void main(String[] args) throws IOException {
        try {
            Inventory inv = new Inventory();
        } catch (Exception e) {
            e.printStackTrace();
        } 
        
    }

    public void setVisible(boolean b) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
