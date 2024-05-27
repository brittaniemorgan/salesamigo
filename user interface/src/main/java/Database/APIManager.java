/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Database;


/**
 *
 * @author britt
 */
import Inventory.Product;
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
    
    public String sendDeleteRequestToAPI(String apiUrl, JSONObject obj) throws IOException {
        String messageContent = obj.toString();

        System.out.println("Sending DELETE request with message:\n" + messageContent);

        URL url = new URL(baseURL, apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        String errorMessage = null;

        try {
            String csrfToken = getCSRFToken();
            if (csrfToken == null) {
                System.out.println("CSRF token retrieval failed. Aborting request.");
                System.exit(0);
            }

            connection.setRequestMethod("DELETE");
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
    
    public JSONObject getProducts(){
        JSONObject products = null;
        try {
            products = new JSONObject(fetchDataFromAPI("products"));
            return products;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return products;
    }
    
    public JSONObject addProduct(String name, String description, double price, int category, int brand, String gender) {
        JSONObject response = null;
        try {
            JSONObject productInfo = new JSONObject();
            productInfo.put("name", name);
            productInfo.put("category", category);
            productInfo.put("price", price);
            productInfo.put("description", description);
            productInfo.put("brand", brand);
            productInfo.put("gender", gender);

            response =  new JSONObject(sendDataToAPI("products", productInfo));
            System.out.println("Response from server: " + response.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }
    
    public JSONObject updateProduct(int id, String name, String description, double price, int category, int brand, String gender) {
        JSONObject response = null;
        try {
            JSONObject productInfo = new JSONObject();
            productInfo.put("product_id", id);
            productInfo.put("name", name);
            productInfo.put("category_id", category);
            productInfo.put("price", price);
            productInfo.put("description", description);
            productInfo.put("gender", gender);
            productInfo.put("brand_id", brand);
            
            response = new JSONObject(sendPutRequestToAPI("products", productInfo));
            System.out.println("Response from server: " + response.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    public JSONObject deleteProduct(int productID) {
        JSONObject response = null;
        try {
            JSONObject productInfo = new JSONObject();
            productInfo.put("product_id", productID);
            
            response = new JSONObject(sendDeleteRequestToAPI("products", productInfo));
            System.out.println("Response from server: " + response.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }
    
    public JSONObject getCategories(){
        JSONObject categories = null;
        try {
            categories = new JSONObject(fetchDataFromAPI("categories"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return categories;
    }
    
    public JSONObject addCategory(String name) {
        JSONObject response = null;
        try {
            JSONObject categoryInfo = new JSONObject();
            categoryInfo.put("name", name);

            response = new JSONObject(sendDataToAPI("categories", categoryInfo));
            System.out.println("Response from server: " + response.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }
    
    public JSONObject updateCategory(int categoryID, String name) {
        JSONObject response = null;
        try {
            JSONObject categoryInfo = new JSONObject();
            categoryInfo.put("category_id", categoryID);
            categoryInfo.put("name", name);
            
            response = new JSONObject(sendPutRequestToAPI("categories", categoryInfo));
            System.out.println("Response from server: " + response.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }
    
    public JSONObject deleteCategory(int catID) {
        JSONObject response = null;
        try {
            JSONObject categoryInfo = new JSONObject();
            categoryInfo.put("category_id", catID);            
           
            response = new JSONObject(sendDeleteRequestToAPI("categories", categoryInfo));
            System.out.println("Response from server: " + response.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }
    
    public JSONObject getBrands(){
        JSONObject brands = null;
        try {
            brands = new JSONObject(fetchDataFromAPI("brands"));
            return brands;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return brands;
    }
    
    public JSONObject getSizes(){
        JSONObject sizes = null;
        try {
            sizes = new JSONObject(fetchDataFromAPI("sizes"));
            return sizes;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sizes;
    }
    
    public JSONObject getVariants(){
        JSONObject variants = null;
        try {
            variants = new JSONObject(fetchDataFromAPI("product_variants"));
            System.out.println(variants);
            return variants;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return variants;
    }
        
    public JSONObject addVariant(int productID, int size_id, String colour, int quantity, double price, int minQuantity) {
        JSONObject response = null;
        try {
            JSONObject variantInfo = new JSONObject();
            variantInfo.put("product_id", productID);
            variantInfo.put("size", size_id);
            variantInfo.put("color", colour);
            variantInfo.put("quantity", quantity);
            variantInfo.put("price", price);
            variantInfo.put("min_quantity", minQuantity);

            response =  new JSONObject(sendDataToAPI("product_variants", variantInfo));
            System.out.println("Response from server: " + response.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    
    public JSONObject updateVariant(int variant_id, int product_id, int size_id, String colour, int quantity, double price, int minQuantity) {
        JSONObject response = null;
        try {
            JSONObject variantInfo = new JSONObject();
            //variantInfo.put("product_id", product_id);
           // variantInfo.put("variant_id", variant_id);
            variantInfo.put("size", size_id);
            variantInfo.put("color", colour);
            variantInfo.put("quantity", quantity);
            variantInfo.put("price", price);
            variantInfo.put("min_quantity", minQuantity);
            
            response = new JSONObject(sendPutRequestToAPI("product_variants/" + variant_id, variantInfo));
            System.out.println("Response from server: " + response.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    public JSONObject deleteVariant(int variantID) {
        JSONObject response = null;
        try {
            JSONObject variantInfo = new JSONObject();
            response = new JSONObject(sendDeleteRequestToAPI("product_variants/"+variantID, variantInfo));
            System.out.println("Response from server: " + response.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    public static void main(String[] args) throws IOException {
        try {
            APIManager example = APIManager.getAPIManager();
        } catch (Exception e) {
            e.printStackTrace();
        } 
    }
}
