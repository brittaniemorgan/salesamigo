/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ReportingAndAnalytics;

import Database.APIManager;
import java.text.SimpleDateFormat;
import org.json.JSONObject;

/**
 *
 * @author britt
 */
public class ReportGenerator {
    private APIManager api;

    public ReportGenerator() {
        try {
            api = APIManager.getAPIManager();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public JSONObject getSalesReport(ReportFilter filter) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String startDate = dateFormat.format(filter.getStartDate());
        String endDate = dateFormat.format(filter.getEndDate());
        JSONObject response = api.getSalesReport(startDate, endDate);
        // Process the response as needed
        return response;
    }
    
    public JSONObject getInventoryReport(ReportFilter filter) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String startDate = dateFormat.format(filter.getStartDate());
        String endDate = dateFormat.format(filter.getEndDate());
        JSONObject response = api.getInventoryReport(startDate, endDate);
        // Process the response as needed
        return response;
    }
    
    public JSONObject getFinanceReport(ReportFilter filter) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String startDate = dateFormat.format(filter.getStartDate());
        String endDate = dateFormat.format(filter.getEndDate());
        JSONObject response = api.getFinanceReport(startDate, endDate);
        // Process the response as needed
        return response;
    }
}
