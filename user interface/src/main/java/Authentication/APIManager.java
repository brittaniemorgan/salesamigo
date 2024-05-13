/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Authentication;


/**
 *
 * @author britt
 */
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

public class APIManager {
    private static APIManager api;
    private final URL baseURL;

    private APIManager() throws Exception {
        this.baseURL = new URL("http://localhost:5000/");
    }

    public static synchronized APIManager getAPIManager() throws Exception {
        if (api == null) {
            api = new APIManager();
        }
        return api;
    }

    public String fetchDataFromAPI(String apiUrl) {
        StringBuilder response = new StringBuilder();
        HttpURLConnection connection = null;
        try {
            URL url = new URL(baseURL, apiUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return response.toString();
    }

    public JSONObject getUsers(){
        JSONObject jsonResponse = null;
        try{    
        String responseData = fetchDataFromAPI("/users");
        jsonResponse = new JSONObject(responseData);
        System.out.println(responseData);
        return jsonResponse;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonResponse;
    }
    
    public static void main(String[] args) {
        try {
            APIManager apiManager = APIManager.getAPIManager();
            String apiUrl = "/users"; // Example API endpoint
            JSONObject responseData = apiManager.getUsers();
            //JSONObject responseData = apiManager.getUsers();
            /*;
            
            String message = jsonResponse.getString("message");
            System.out.println("Message: " + message);*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
