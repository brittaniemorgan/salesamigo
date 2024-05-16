/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Authentication;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author britt
 */
public class Authenticator {
    APIManager api;
    JSONArray users;
    
    public Authenticator(){
        try{
            api = APIManager.getAPIManager();
            users = api.getUsers();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public JSONObject getUser(String username) {
        JSONObject user = null;
        try{
            for (int i = 0; i < users.length(); i++) {
                user = users.getJSONObject(i);
                if (user.getString("username").equals(username)) {
                    break;
                }
                user = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }
    
    public JSONObject validateUser(String username, String password){
        JSONObject user = getUser(username);
        try{
            if (user.getString("password").equals(password)){
                return user;
            }
            user = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }
    
    
    public static void main(String[] args) {
        Authenticator auth = new Authenticator();
        System.out.println(auth.getUser("JohnB"));
        auth.validateUser("johnB", "password123");
    }
}
