/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit3TestClass.java to edit this template
 */
package Inventory;

import java.util.ArrayList;
import junit.framework.TestCase;

/**
 *
 * @author richellewilliams
 */
public class InventoryTest extends TestCase {
    
    public InventoryTest(String testName) {
        super(testName);
    }
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }
    
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of addProduct method, of class Inventory.
     */
    public void testAddProduct() {
        System.out.println("addProduct");
        String name = "Shoulder Bag";
        String description = "Oval Shoulder Bag";
        double price = 54.99;
        String category = "Handbag";
        String brand = "Zara";
        String gender = "Female";
        String expResult = "success";
        
        Inventory instance = new Inventory();
        String result = instance.addProduct(name, description, price, category, brand, gender);
        assertTrue("Expected message '" + expResult + "' in response", result.contains(expResult));
    }

    /**
     * Test of updateProduct method, of class Inventory.
     */
    public void testUpdateProduct() {
        System.out.println("updateProduct");
        int productID = 15;
        String name = "Shoulder Bag";
        String category = "Handbag";
        double price = 44.89;
        String description = "Oval Shoulder Bag";
        String gender = "Female";
        String brand = "H&M";
        String expResult = "success";
        
        Inventory instance = new Inventory();
        String result = instance.updateProduct(productID, name, category, price, description, gender, brand);
        assertTrue("Expected message '" + expResult + "' in response", result.contains(expResult));
    }

    /**
     * Test of deleteProduct method, of class Inventory.
     */
    public void testDeleteProduct() {
        System.out.println("deleteProduct");
        int productID = 15;
        String expResult = "success";
    
        Inventory instance = new Inventory();
        String result = instance.deleteProduct(productID);
        assertTrue("Expected message '" + expResult + "' in response", result.contains(expResult));
    }
    
    /**
     * Test of createVariant method, of class Inventory.
     */
    public void testCreateVariant() {
        System.out.println("createVariant");
        Product product = new Product(15, "Shoulder Bag", "Oval Shoulder Bag", 54.99, "Handbag", "Zara", "Female");
        String size = "M";
        String colour = "Green";
        int quantity = 40;
        double price = 54.99;
        int minQuantity = 2;
        Inventory instance = new Inventory();
        String expResult = "success";
        
        String result = instance.createVariant(product, size, colour, quantity, price, minQuantity);
        assertTrue("Expected message '" + expResult + "' in response", result.contains(expResult));
    }

    /**
     * Test of updateVariant method, of class Inventory.
     */
    public void testUpdateVariant() {
        System.out.println("updateVariant");
        int variantID = 25;
        Product product = new Product(15, "Shoulder Bag", "Oval Shoulder Bag", 44.89, "Handbag", "H&M", "Female");;
        String size = "L";
        String colour = "Pink";
        int quantity = 50;
        double price = 44.89;
        int minQuantity = 5;
        String expResult = "success";

        Inventory instance = new Inventory();
        String result = instance.updateVariant(variantID, product, size, colour, quantity, price, minQuantity);
        assertTrue("Expected message '" + expResult + "' in response", result.contains(expResult));
    }

    /**
     * Test of deleteVariant method, of class Inventory.
     */
    public void testDeleteVariant() {
        System.out.println("deleteVariant");
        int variantID = 25;
        String expResult = "success";
    
        Inventory instance = new Inventory();
        String result = instance.deleteVariant(variantID);
        assertTrue("Expected message '" + expResult + "' in response", result.contains(expResult));
    }

    /**
     * Test of createCategory method, of class Inventory.
     */
    public void testCreateCategory() {
        System.out.println("createCategory");
        String name = "Pants";
        String expResult = "success";
        
        Inventory instance = new Inventory();
        String result = instance.createCategory(name);
        assertTrue("Expected message '" + expResult + "' in response", result.contains(expResult));
    }

    /**
     * Test of updateCategory method, of class Inventory.
     */
    public void testUpdateCategory() {
        System.out.println("updateCategory");
        int id = 17;
        String name = "Shirt";
        String expResult = "success";
        
        Inventory instance = new Inventory();
        String result = instance.updateCategory(id, name);
        assertTrue("Expected message '" + expResult + "' in response", result.contains(expResult));
    }
    
    /**
     * Test of deleteCategory method, of class Inventory.
     */
    public void testDeleteCategory() {
        System.out.println("deleteCategory");
        int id = 17;
        String expResult = "success";
        
        Inventory instance = new Inventory();
        String result = instance.deleteCategory(id);
        assertTrue("Expected message '" + expResult + "' in response", result.contains(expResult));
    }

    /**
     * Test of createBrand method, of class Inventory.
     */
    public void testCreateBrand() {
        System.out.println("createBrand");
        String name = "Forever21";
        String expResult = "success";
        
        Inventory instance = new Inventory();
        String result = instance.createBrand(name);
        assertTrue("Expected message '" + expResult + "' in response", result.contains(expResult));
    }

    /**
     * Test of updateBrand method, of class Inventory.
     */
    public void testUpdateBrand() {
        System.out.println("updateBrand");
        int id = 6;
        String name = "Forever21Style";
        String expResult = "success";
        
        Inventory instance = new Inventory();
        String result = instance.updateBrand(id, name);
        assertTrue("Expected message '" + expResult + "' in response", result.contains(expResult));
    }
    
    /**
     * Test of deleteBrand method, of class Inventory.
     */
    public void testDeleteBrand() {
        System.out.println("deleteBrand");
        int id = 6;
        String expResult = "success";
        
        Inventory instance = new Inventory();
        String result = instance.deleteBrand(id);
        assertTrue("Expected message '" + expResult + "' in response", result.contains(expResult));
    }

    /**
     * Test of createSize method, of class Inventory.
     */
    public void testCreateSize() {
        System.out.println("createSize");
        String name = "XL";
        String expResult = "success";
        
        Inventory instance = new Inventory();
        String result = instance.createSize(name);
        assertTrue("Expected message '" + expResult + "' in response", result.contains(expResult));
    }

    /**
     * Test of updateSize method, of class Inventory.
     */
    public void testUpdateSize() {
        System.out.println("updateSize");
        int id = 5;
        String name = "XXL";
        String expResult = "success";
        
        Inventory instance = new Inventory();
        String result = instance.updateSize(id, name);
        assertTrue("Expected message '" + expResult + "' in response", result.contains(expResult));
    }
    
    /**
     * Test of deleteSize method, of class Inventory.
     */
    public void testDeleteSize() {
        System.out.println("deleteSize");
        int id = 5;
        String expResult = "success";

        Inventory instance = new Inventory();
        String result = instance.deleteSize(id);
        assertTrue("Expected message '" + expResult + "' in response", result.contains(expResult));
    }

    /**
     * Test of main method, of class Inventory.
     */
    public void testMain() throws Exception {
        System.out.println("main");
        String[] args = null;
        Inventory.main(args);
    }
    
}
