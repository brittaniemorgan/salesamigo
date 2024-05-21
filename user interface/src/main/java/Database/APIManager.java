/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Database;


/**
 *
 * @author britt
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
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
    
    public String getCSRFToken() {
        String csrfToken = null;
        try {
            String responseData = fetchDataFromAPI("csrf-token");
            JSONObject jsonResponse = new JSONObject(responseData);
            csrfToken = jsonResponse.getString("csrf_token");
            System.out.println(csrfToken);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return csrfToken;
    }
    
    public String sendDataToAPI(String apiUrl, JSONObject obj) throws IOException {
        String messageContent = obj.toString();

        System.out.println("Sending POST request with message:\n" + messageContent);

        URL url = new URL(baseURL, apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        String errorMessage = null;

        try {
            String csrfToken = getCSRFToken();
            if (csrfToken == null) {
                System.out.println("CSRF token retrieval failed. Aborting request.");
                System.exit(0);
            }

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("X-CSRFToken", csrfToken);
            connection.setDoOutput(true);

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = messageContent.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int responseCode = connection.getResponseCode();
            System.out.println("Response from the server:");
            System.out.println("Response Code: " + responseCode);
            System.out.println("Response Message: " + connection.getResponseMessage());

            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    System.out.println("Response: " + response.toString());
                    return response.toString();
                }
            } else {
                try (BufferedReader errorReader = new BufferedReader(new InputStreamReader(connection.getErrorStream()))) {
                    StringBuilder errorResponse = new StringBuilder();
                    String line;
                    while ((line = errorReader.readLine()) != null) {
                        errorResponse.append(line);
                    }
                    System.out.println("Error Response: " + errorResponse.toString());
                    errorMessage = errorResponse.toString(); 
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }

        return errorMessage; 
    }

    public String sendPutRequestToAPI(String apiUrl, JSONObject obj) throws IOException {
        String messageContent = obj.toString();

        System.out.println("Sending PUT request with message:\n" + messageContent);

        URL url = new URL(baseURL, apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        String errorMessage = null;

        try {
            String csrfToken = getCSRFToken();
            if (csrfToken == null) {
                System.out.println("CSRF token retrieval failed. Aborting request.");
                System.exit(0);
            }

            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("X-CSRFToken", csrfToken);
            connection.setDoOutput(true);

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = messageContent.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int responseCode = connection.getResponseCode();
            System.out.println("Response from the server:");
            System.out.println("Response Code: " + responseCode);
            System.out.println("Response Message: " + connection.getResponseMessage());

            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    System.out.println("Response: " + response.toString());
                    return response.toString();
                }
            } else {
                try (BufferedReader errorReader = new BufferedReader(new InputStreamReader(connection.getErrorStream()))) {
                    StringBuilder errorResponse = new StringBuilder();
                    String line;
                    while ((line = errorReader.readLine()) != null) {
                        errorResponse.append(line);
                    }
                    System.out.println("Error Response: " + errorResponse.toString());
                    errorMessage = errorResponse.toString(); 
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }

        return errorMessage; 
    }

    public static void main(String[] args) throws IOException {
        try {
            APIManager example = APIManager.getAPIManager();
            JSONObject dataToSend = new JSONObject();
            dataToSend.put("employee_id", "1");
            dataToSend.put("password", "password123");
            example.sendDataToAPI("login", dataToSend);
        } catch (Exception e) {
            e.printStackTrace();
        } 
    }
}
