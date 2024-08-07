/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package API;

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
import java.util.ArrayList;
import org.json.JSONArray;
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

    public JSONObject addEmployee(String firstname, String lastname, String address, String email, String number, String role, String password) {
        JSONObject response = null;
        try {
            JSONObject productInfo = new JSONObject();
            productInfo.put("firstname", firstname);
            productInfo.put("lastname", lastname);
            productInfo.put("address", address);
            productInfo.put("email", email);
            productInfo.put("contact_number", number);
            productInfo.put("role", role);
            productInfo.put("password", password);

            response = new JSONObject(sendDataToAPI("register_user", productInfo));
            System.out.println("Response from server: " + response.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    public JSONObject getCustomers() {
        JSONObject customers = null;
        try {
            customers = new JSONObject(fetchDataFromAPI("customers"));
            return customers;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return customers;
    }

    public JSONObject getCustomerByID(int id) {
        JSONObject customer = null;
        try {
            customer = new JSONObject(fetchDataFromAPI("customers/" + id));

            return customer;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return customer;
    }

    public JSONObject getProducts() {
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

            response = new JSONObject(sendDataToAPI("products", productInfo));
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

    public JSONObject getCategories() {
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

    public JSONObject updateCategory(int catID, String name) {
        JSONObject response = null;
        try {
            JSONObject categoryInfo = new JSONObject();
            categoryInfo.put("name", name);

            response = new JSONObject(sendPutRequestToAPI("categories/" + catID, categoryInfo));
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

            response = new JSONObject(sendDeleteRequestToAPI("categories/" + catID, categoryInfo));
            System.out.println("Response from server: " + response.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    public JSONObject getBrands() {
        JSONObject brands = null;
        try {
            brands = new JSONObject(fetchDataFromAPI("brands"));
            return brands;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return brands;
    }

    public JSONObject addBrand(String name) {
        JSONObject response = null;
        try {
            JSONObject brandInfo = new JSONObject();
            brandInfo.put("name", name);

            response = new JSONObject(sendDataToAPI("brands", brandInfo));
            System.out.println("Response from server: " + response.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    public JSONObject updateBrand(int brandID, String name) {
        JSONObject response = null;
        try {
            JSONObject brandInfo = new JSONObject();
            brandInfo.put("name", name);

            response = new JSONObject(sendPutRequestToAPI("brands/" + brandID, brandInfo));
            System.out.println("Response from server: " + response.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    public JSONObject deleteBrand(int brandID) {
        JSONObject response = null;
        try {
            JSONObject brandInfo = new JSONObject();

            response = new JSONObject(sendDeleteRequestToAPI("brands/" + brandID, brandInfo));
            System.out.println("Response from server: " + response.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    public JSONObject getSizes() {
        JSONObject sizes = null;
        try {
            sizes = new JSONObject(fetchDataFromAPI("sizes"));
            return sizes;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sizes;
    }

    public JSONObject addSize(String name) {
        JSONObject response = null;
        try {
            JSONObject sizeInfo = new JSONObject();
            sizeInfo.put("name", name);

            response = new JSONObject(sendDataToAPI("sizes", sizeInfo));
            System.out.println("Response from server: " + response.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    public JSONObject updateSize(int sizeID, String name) {
        JSONObject response = null;
        try {
            JSONObject sizeInfo = new JSONObject();
            sizeInfo.put("name", name);

            response = new JSONObject(sendPutRequestToAPI("sizes/" + sizeID, sizeInfo));
            System.out.println("Response from server: " + response.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    public JSONObject deleteSize(int sizeID) {
        JSONObject response = null;
        try {
            JSONObject sizeInfo = new JSONObject();

            response = new JSONObject(sendDeleteRequestToAPI("sizes/" + sizeID, sizeInfo));
            System.out.println("Response from server: " + response.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    public JSONObject getVariants() {
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

            response = new JSONObject(sendDataToAPI("product_variants", variantInfo));
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
            response = new JSONObject(sendDeleteRequestToAPI("product_variants/" + variantID, variantInfo));
            System.out.println("Response from server: " + response.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    public JSONObject getDiscounts() {
        JSONObject discounts = null;
        try {
            discounts = new JSONObject(fetchDataFromAPI("discounts"));
            return discounts;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return discounts;
    }

    public JSONObject getTransactions() {
        JSONObject transactions = null;
        try {
            transactions = new JSONObject(fetchDataFromAPI("sales_transactions"));
            System.out.println(transactions);
            return transactions;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return transactions;
    }

    public JSONObject addTransaction(int employeeId, int customerId, double total, String paymentMethod, JSONArray items, int pointsApplied) {
        JSONObject response = null;
        try {
            JSONObject transactionInfo = new JSONObject();
            transactionInfo.put("employee_id", employeeId);
            transactionInfo.put("customer_id", customerId);
            transactionInfo.put("total", total);
            transactionInfo.put("payment_method", paymentMethod);
            transactionInfo.put("items", items);
            transactionInfo.put("pointsApplied", pointsApplied);

            response = new JSONObject(sendDataToAPI("/sales_transactions", transactionInfo));
            System.out.println("Response from server: " + response.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    public JSONObject getPaymentTypes() {
        JSONObject paymentTypes = null;
        try {
            paymentTypes = new JSONObject(fetchDataFromAPI("payment_types"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return paymentTypes;
    }

    public JSONObject addPaymentType(String name) {
        JSONObject response = null;
        try {
            JSONObject paymentTypeInfo = new JSONObject();
            paymentTypeInfo.put("name", name);

            response = new JSONObject(sendDataToAPI("payment_types", paymentTypeInfo));
            System.out.println("Response from server: " + response.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    public JSONObject updatePaymentType(int paymentTypeID, String name) {
        JSONObject response = null;
        try {
            JSONObject paymentTypeInfo = new JSONObject();
            paymentTypeInfo.put("name", name);

            response = new JSONObject(sendPutRequestToAPI("payment_types/" + paymentTypeID, paymentTypeInfo));
            System.out.println("Response from server: " + response.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    public JSONObject deletePaymentType(int paymentTypeID) {
        JSONObject response = null;
        try {
            JSONObject paymentTypeInfo = new JSONObject();

            response = new JSONObject(sendDeleteRequestToAPI("payment_types/" + paymentTypeID, paymentTypeInfo));
            System.out.println("Response from server: " + response.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    public JSONObject processRefund(int transactionId, JSONArray items, int employeeId, double amount) {
        JSONObject response = null;
        try {
            JSONObject refundInfo = new JSONObject();
            refundInfo.put("items", items);
            refundInfo.put("employee_id", employeeId);
            refundInfo.put("amount", amount);
            response = new JSONObject(sendDataToAPI("/refunds/" + transactionId, refundInfo));
            System.out.println("Response from server: " + response.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    public JSONObject addDiscount(String discountCode, String discountName, double discountPercent, String startDate, String endDate, ArrayList<Integer> applicable_ids, String discountType) {
        JSONObject response = null;
        try {
            JSONObject discountInfo = new JSONObject();
            discountInfo.put("discount_code", discountCode);
            discountInfo.put("discount_name", discountName);
            discountInfo.put("discount_percent", discountPercent);
            discountInfo.put("start_date", startDate);
            discountInfo.put("end_date", endDate);
            discountInfo.put("applicable_ids", applicable_ids);
            discountInfo.put("discount_type", discountType);

            response = new JSONObject(sendDataToAPI("discounts", discountInfo));
            System.out.println("Response from server: " + response.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    public JSONObject updateDiscount(int discountID, String discountCode, String discountName, double discountPercent, String startDate, String endDate, ArrayList<Integer> applicable_ids, String discountType) {
        JSONObject response = null;
        try {
            JSONObject discountInfo = new JSONObject();
            discountInfo.put("discount_code", discountCode);
            discountInfo.put("discount_name", discountName);
            discountInfo.put("discount_percent", discountPercent);
            discountInfo.put("start_date", startDate);
            discountInfo.put("end_date", endDate);
            discountInfo.put("applicable_ids", applicable_ids);
            discountInfo.put("discount_type", discountType);

            response = new JSONObject(sendPutRequestToAPI("discounts/" + discountID, discountInfo));
            System.out.println("Response from server: " + response.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    public JSONObject deleteDiscount(int discountID) {
        JSONObject response = null;
        try {
            JSONObject discountInfo = new JSONObject();
            response = new JSONObject(sendDeleteRequestToAPI("discounts/" + discountID, discountInfo));
            System.out.println("Response from server: " + response.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    public JSONObject getSalesReport(String startDate, String endDate) {
        JSONObject salesData = null;
        try {
            String endpoint = "sales_report?start_date=" + startDate + "&end_date=" + endDate;
            salesData = new JSONObject(fetchDataFromAPI(endpoint));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return salesData;
    }

    public JSONObject getInventoryReport(String startDate, String endDate) {
        JSONObject salesData = null;
        try {
            String endpoint = "inventory_report_test?start_date=" + startDate + "&end_date=" + endDate;
            salesData = new JSONObject(fetchDataFromAPI(endpoint));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return salesData;
    }

    public JSONObject getFinanceReport(String startDate, String endDate) {
        JSONObject salesData = null;
        try {
            String endpoint = "finance_report?start_date=" + startDate + "&end_date=" + endDate;
            salesData = new JSONObject(fetchDataFromAPI(endpoint));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return salesData;
    }

    public JSONObject getProductRecommendations(int customerID) {
        JSONObject productRecommendations = null;
        try {
            productRecommendations = new JSONObject(fetchDataFromAPI("product_recommendations/" + customerID));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return productRecommendations;
    }

    public static void main(String[] args) throws IOException {
        try {
            APIManager example = APIManager.getAPIManager();

            JSONObject response = example.getCustomerByID(1);
            System.out.println("Response from server: " + response.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
