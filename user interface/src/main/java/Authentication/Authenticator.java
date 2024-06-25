/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Authentication;

import Database.APIManager;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import org.json.JSONObject;

/**
 *
 * @author britt
 */
public class Authenticator {
    APIManager api;
    
    public Authenticator(){
        try{
            api = APIManager.getAPIManager();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /*
    public JSONArray getUsers(){
        JSONArray jsonResponse = null;
        try{    
            String responseData = api.fetchDataFromAPI("/users");
            jsonResponse = new JSONObject(responseData).getJSONArray("users");
            //System.out.println(responseData);
            return jsonResponse;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonResponse;
    }   */
    
    public String addUser(String fname, String lname, String email, String phoneNum, String address, String role, String password){
        String feedback = "";
        try {            
            JSONObject response = api.addEmployee(fname, lname, address, email, phoneNum, role, hashPassword(password));           
            
            if (response.has("error")) {
                feedback = "Error adding user: " + response.getString("error");                
            } else {
                int employeeID = response.getInt("id");
                //product.addVariant(new ProductVariant(variantId, product, size, colour, quantity, price, minQuantity));
                feedback = "Employee added successfully!";
            }            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return feedback;
    }
    
    public String hashPassword(String password) {
        try {
            // Create MessageDigest instance for SHA-256
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            // Add password bytes to digest
            byte[] hashedBytes = md.digest(password.getBytes());

            // Convert the byte array into a string using Base64 encoding
            return Base64.getEncoder().encodeToString(hashedBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not found", e);
        }
    }
    
    public JSONObject validateUser(String username, String password) throws IOException {
        JSONObject response = null;
        try {
            JSONObject userInfo = new JSONObject();
            userInfo.put("employee_id", username);
            userInfo.put("password", hashPassword(password));
            response = new JSONObject(api.sendDataToAPI("login", userInfo));
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return response;
        } 
    }
    
    
    
    public static void main(String[] args) throws IOException {
        Authenticator auth = new Authenticator();
        System.out.println(auth.validateUser("1", "password123"));
    }
}
