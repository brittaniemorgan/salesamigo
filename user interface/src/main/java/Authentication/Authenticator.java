/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Authentication;

import API.APIManager;
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
        
    public String addUser(String fname, String lname, String email, String phoneNum, String address, String role, String password){
        String feedback = "";
        try {            
            JSONObject response = api.addEmployee(fname, lname, address, email, phoneNum, role, hashPassword(password));           
            
            if (response.has("error")) {
                feedback = "Error adding user: " + response.getString("error");                
            } else {
                int employeeID = response.getInt("id");
                feedback = "Employee added successfully!";
            }            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return feedback;
    }
    
    public String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());
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
