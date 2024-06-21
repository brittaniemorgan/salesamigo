/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ReportingAndAnalytics;

/**
 *
 * @author britt
 */import Database.APIManager;
import org.jfree.chart.ChartFactory; 
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.entity.CategoryItemEntity;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.plot.CategoryPlot;

public class InventorySalesReportUI extends JFrame {

    private JPanel contentPane;
    private APIManager apiManager;
    private JTable dailySalesTable;
    private JTable weeklySalesTable;
    private JTable monthlySalesTable;
    private JTable yearlySalesTable;
    private JTabbedPane tabbedPane;

    public InventorySalesReportUI() {
        setTitle("Inventory and Sales Reports");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1000, 800);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(0, 0));

        try {
            // Initialize API manager
            apiManager = APIManager.getAPIManager();
        } catch (Exception ex) {
            Logger.getLogger(InventorySalesReportUI.class.getName()).log(Level.SEVERE, null, ex);
        }

        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        contentPane.add(tabbedPane, BorderLayout.CENTER);

        // Fetch data and create tabs for each report
        String endpoint = "/inventory_report_test?start_date=2024-06-01&end_date=2024-12-31";
        String response = apiManager.fetchDataFromAPI(endpoint);
        JSONObject jsonData;
        try {
            jsonData = new JSONObject(response);
            createDailySalesTab(jsonData.getJSONArray("daily_sales"));
            createWeeklySalesTab(jsonData.getJSONArray("weekly_sales"));
            createMonthlySalesTab(jsonData.getJSONArray("monthly_sales"));
            createYearlySalesTab(jsonData.getJSONArray("yearly_sales"));
            createInventoryMetricsTab(jsonData.getJSONArray("inventory_metrics"));
        } catch (JSONException ex) {
            Logger.getLogger(InventorySalesReportUI.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, "Failed to fetch data from API: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        setVisible(true);
    }

    private void createDailySalesTab(JSONArray dailySalesData) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Create table model and sort by sale_date initially
        DefaultTableModel model = createTableModel(dailySalesData);
        dailySalesTable = new JTable(model);
        dailySalesTable.setAutoCreateRowSorter(true);

        JScrollPane scrollPane = new JScrollPane(dailySalesTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Create chart
        JFreeChart chart = createChart(dailySalesData, "Daily Sales", "Sale Date", "Total Sales");
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(600, 400)); // Set preferred size for the chart panel
        panel.add(chartPanel, BorderLayout.SOUTH);

        tabbedPane.addTab("Daily Sales", null, panel, "Daily Sales Report");
    }

    private void createWeeklySalesTab(JSONArray weeklySalesData) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Create table model and sort by sale_year, sale_week initially
        DefaultTableModel model = createTableModel(weeklySalesData);
        weeklySalesTable = new JTable(model);
        weeklySalesTable.setAutoCreateRowSorter(true);

        JScrollPane scrollPane = new JScrollPane(weeklySalesTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Create chart
        JFreeChart chart = createChart(weeklySalesData, "Weekly Sales", "Week", "Total Sales");
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(600, 400)); // Set preferred size for the chart panel
        panel.add(chartPanel, BorderLayout.SOUTH);

        tabbedPane.addTab("Weekly Sales", null, panel, "Weekly Sales Report");
    }

    private void createMonthlySalesTab(JSONArray monthlySalesData) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Create table model and sort by sale_year, sale_month initially
        DefaultTableModel model = createTableModel(monthlySalesData);
        monthlySalesTable = new JTable(model);
        monthlySalesTable.setAutoCreateRowSorter(true);

        JScrollPane scrollPane = new JScrollPane(monthlySalesTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Create chart
        JFreeChart chart = createChart(monthlySalesData, "Monthly Sales", "Month", "Total Sales");
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(600, 400)); // Set preferred size for the chart panel
        panel.add(chartPanel, BorderLayout.SOUTH);

        tabbedPane.addTab("Monthly Sales", null, panel, "Monthly Sales Report");
    }

    private void createYearlySalesTab(JSONArray yearlySalesData) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Create table model and sort by sale_year initially
        DefaultTableModel model = createTableModel(yearlySalesData);
        yearlySalesTable = new JTable(model);
        yearlySalesTable.setAutoCreateRowSorter(true);

        JScrollPane scrollPane = new JScrollPane(yearlySalesTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Create chart
        JFreeChart chart = createChart(yearlySalesData, "Yearly Sales", "Year", "Total Sales");
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(600, 400)); // Set preferred size for the chart panel
        panel.add(chartPanel, BorderLayout.SOUTH);

        tabbedPane.addTab("Yearly Sales", null, panel, "Yearly Sales Report");
    }

private void createInventoryMetricsTab(JSONArray inventoryMetricsData) {
    JPanel panel = new JPanel();
    panel.setLayout(new BorderLayout());

    // Define column names for the table
    String[] columnNames = {"Product ID", "Variant ID", "Average Inventory Level", "Sell-Through Rate (%)", "Turnover Rate (%)"};

    // Create data array for the table
    Object[][] data = new Object[inventoryMetricsData.length()][columnNames.length];
    DefaultCategoryDataset dataset = new DefaultCategoryDataset();

    try {
        for (int i = 0; i < inventoryMetricsData.length(); i++) {
            JSONObject metric = inventoryMetricsData.getJSONObject(i);
            int productId = metric.getInt("product_id");
            int variantId = metric.getInt("variant_id");
            double avgInventoryLevel = Double.parseDouble(metric.getString("avg_inventory_level"));
            double sellThroughRate = Double.parseDouble(metric.getString("sell_through_rate"));
            double turnoverRate = Double.parseDouble(metric.getString("turnover_rate"));

            data[i][0] = productId;
            data[i][1] = variantId;
            data[i][2] = avgInventoryLevel;
            data[i][3] = sellThroughRate;
            data[i][4] = turnoverRate;

            String productVariant = "Product " + productId + " - Variant " + variantId;
            dataset.addValue(avgInventoryLevel, "Average Inventory Level", productVariant);
            dataset.addValue(sellThroughRate, "Sell-Through Rate (%)", productVariant);
            dataset.addValue(turnoverRate, "Turnover Rate (%)", productVariant);
        }
    } catch (JSONException ex) {
        ex.printStackTrace();
    }

    // Create table with the data
    JTable table = new JTable(data, columnNames);
    table.setFont(new Font("Arial", Font.PLAIN, 14));
    table.setRowHeight(20);

    // Add table to scroll pane
    JScrollPane scrollPane = new JScrollPane(table);
    panel.add(scrollPane, BorderLayout.CENTER);

    // Create the bar chart
    JFreeChart barChart = ChartFactory.createBarChart(
            "Inventory Metrics",
            "Product Variant",
            "Value",
            dataset,
            PlotOrientation.VERTICAL,
            true, true, false);

    // Customize the plot
    CategoryPlot plot = barChart.getCategoryPlot();
    plot.setDomainGridlinesVisible(true);

    // Create a panel to hold the chart
    ChartPanel chartPanel = new ChartPanel(barChart);
    chartPanel.setPreferredSize(new Dimension(600, 400)); // Set preferred size for the chart panel

    // Add the chart panel to the main panel
    panel.add(chartPanel, BorderLayout.SOUTH);

    // Add a ChartMouseListener to the chart panel
    chartPanel.addChartMouseListener(new ChartMouseListener() {
        @Override
        public void chartMouseClicked(ChartMouseEvent event) {
            // Get the plot and point where the user clicked
            ChartEntity entity = event.getEntity();
            if (entity instanceof CategoryItemEntity) {
                CategoryItemEntity item = (CategoryItemEntity) entity;
                Comparable columnKey = item.getColumnKey();
                String[] parts = columnKey.toString().split(" - Variant ");
                int productId = Integer.parseInt(parts[0].substring(8));
                int variantId = Integer.parseInt(parts[1]);

                // Highlight the corresponding row in the table
                for (int row = 0; row < table.getRowCount(); row++) {
                    if (table.getValueAt(row, 0).equals(productId) && table.getValueAt(row, 1).equals(variantId)) {
                        table.setRowSelectionInterval(row, row);
                        Rectangle rect = table.getCellRect(row, 0, true);
                        table.scrollRectToVisible(rect);
                        break;
                    }
                }
            }
        }
        
        @Override
        public void chartMouseMoved(ChartMouseEvent event) {
            ChartEntity entity = event.getEntity();
            if (entity instanceof CategoryItemEntity) {
                CategoryItemEntity item = (CategoryItemEntity) entity;
                Comparable columnKey = item.getColumnKey();
                String[] parts = columnKey.toString().split(" - Variant ");
                int productId = Integer.parseInt(parts[0].substring(8));
                int variantId = Integer.parseInt(parts[1]);

                // Find the corresponding row in the table
                for (int row = 0; row < table.getRowCount(); row++) {
                    if (table.getValueAt(row, 0).equals(productId) && table.getValueAt(row, 1).equals(variantId)) {
                        // Show tooltip with values
                        String tooltipText = String.format("<html><b>Product ID:</b> %d<br><b>Variant ID:</b> %d<br>" +
                                "<b>Average Inventory Level:</b> %.2f<br><b>Sell-Through Rate:</b> %.2f%%<br>" +
                                "<b>Turnover Rate:</b> %.2f%%</html>",
                                productId, variantId,
                                (double) table.getValueAt(row, 2), (double) table.getValueAt(row, 3),
                                (double) table.getValueAt(row, 4));
                        chartPanel.setToolTipText(tooltipText);
                        return;
                    }
                }
            }
            // Clear tooltip if no bar is hovered
            chartPanel.setToolTipText(null);
        }
    });

    // Add the panel to the tabbed pane
    tabbedPane.addTab("Inventory Metrics", null, panel, "Inventory Metrics");
}




    private DefaultTableModel createTableModel(JSONArray data) {
        String[] columnNames = {"Sale Date/Year", "Product ID", "Product Name", "Variant ID", "Size ID", "Color",
                "Total Quantity Sold", "Total Sales"};

        Object[][] rowData = new Object[data.length()][columnNames.length];

        for (int i = 0; i < data.length(); i++) {
            try {
                JSONObject item = data.getJSONObject(i);
                if (item.has("sale_date")) {
                    rowData[i][0] = item.getString("sale_date");
                } else {
                    rowData[i][0] = "Year " + item.getInt("sale_year"); // + ", Week " + item.getInt("sale_week");
                }
                rowData[i][1] = item.getInt("product_id");
                rowData[i][2] = item.getString("product_name");
                rowData[i][3] = item.getInt("variant_id");
                rowData[i][4] = item.getInt("size_id");
                rowData[i][5] = item.getString("color");
                rowData[i][6] = item.getInt("total_quantity_sold");
                rowData[i][7] = item.getDouble("total_sales");
            } catch (JSONException ex) {
                Logger.getLogger(InventorySalesReportUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        DefaultTableModel model = new DefaultTableModel(rowData, columnNames) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 6) {
                    return Integer.class;
                } else if (columnIndex == 7) {
                    return Double.class;
                }
                return super.getColumnClass(columnIndex);
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        return model;
    }

    private JFreeChart createChart(JSONArray data, String title, String categoryAxisLabel, String valueAxisLabel) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        try {
            for (int i = 0; i < data.length(); i++) {
                JSONObject item = data.getJSONObject(i);
                String label;
                if (item.has("sale_date")) {
                    label = item.getString("sale_date");
                } else {
                    label = "Year " + item.getInt("sale_year"); // + ", Week " + item.getInt("sale_week");
                }
                double value = item.getDouble("total_sales");
                dataset.addValue(value, "Sales", label);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JFreeChart chart = ChartFactory.createLineChart(
                title,
                categoryAxisLabel,
                valueAxisLabel,
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        return chart;
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                InventorySalesReportUI frame = new InventorySalesReportUI();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
