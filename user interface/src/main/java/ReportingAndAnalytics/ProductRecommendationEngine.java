/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ReportingAndAnalytics;

import Database.APIManager;
import PromotionAndMarketing.Email;
import PromotionAndMarketing.MarketingManager;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author britt
 */
public class ProductRecommendationEngine {
    private APIManager api;

    public ProductRecommendationEngine() {
        try {
            api = APIManager.getAPIManager();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public ArrayList<ProductRecommendation> getRecommendationsFor(int customerID){        
         ArrayList<ProductRecommendation> recommended_products = new ArrayList<ProductRecommendation>();
        try {
            JSONObject response = api.getProductRecommendations(customerID);
            JSONArray recommendationArray = response.getJSONArray("recommended_products");
            for (int i = 0; i < recommendationArray.length(); i++) {
                JSONObject recommendation = recommendationArray.getJSONObject(i);
                recommended_products.add(new ProductRecommendation(
                    recommendation.getInt("product_id"), 
                    recommendation.getString("brand"),
                    recommendation.getString("category"),                    
                    recommendation.getString("color"),
                    recommendation.getString("gender"),                
                    recommendation.getDouble("price")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return recommended_products;
    } 
}
    