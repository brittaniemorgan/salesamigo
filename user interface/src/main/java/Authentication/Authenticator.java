/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Authentication;

import Database.APIManager;
import java.io.IOException;
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
    
    public JSONObject validateUser(String username, String password) throws IOException {
        JSONObject response = null;
        try {
            JSONObject userInfo = new JSONObject();
            userInfo.put("employee_id", username);
            userInfo.put("password", password);
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
