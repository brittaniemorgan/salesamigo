/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package ReportingAndAnalytics;

import Authentication.LoginFrame;
import Authentication.Employee;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.print.PrinterException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author britt
 */
public class InventoryReportFrame extends javax.swing.JFrame {

    /**
     * Creates new form SalesReportFrame
     */
    ReportGenerator reportGenerator;
    Calendar cal = Calendar.getInstance();
    private Employee employee;

    public InventoryReportFrame(Employee user) {
        initComponents();
        dailySalesTable.setAutoCreateRowSorter(true);
        monthlySalesTable.setAutoCreateRowSorter(true);
        yearlySalesTable.setAutoCreateRowSorter(true);
        metricsTable.setAutoCreateRowSorter(true);
        reportGenerator = new ReportGenerator();        
        this.employee = user;
        employeeIdLabel.setText(String.valueOf(user.getId()));
        greetLabel.setText("Hello " + user.getFirstname() + ",");
    }

    private void updateDailySalesTable(JSONArray sales_data) {
        DefaultTableModel model = (DefaultTableModel) dailySalesTable.getModel();
        model.setRowCount(0);
        for (int i = 0; i < sales_data.length(); i++) {

            try {
                JSONObject item = sales_data.getJSONObject(i);
                Object[] rowData = {
                    item.getString("sale_date"),
                    item.getString("product_name"),
                    item.getString("color"),
                    item.getString("size"),
                    item.getInt("total_quantity_sold")
                };
                model.addRow(rowData);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        JFreeChart chart = createChart(sales_data, "Daily Sales", "Day", "Total Sales");
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(600, 400)); // Set preferred size for the chart panel
        dayChartPanel.removeAll();
        dayChartPanel.add(chartPanel, BorderLayout.CENTER);
        dayChartPanel.repaint();
        dayChartPanel.revalidate();

    }

    private void updateMonthlySalesTable(JSONArray sales_data) {
        DefaultTableModel model = (DefaultTableModel) monthlySalesTable.getModel();
        model.setRowCount(0);
        for (int i = 0; i < sales_data.length(); i++) {

            try {
                JSONObject item = sales_data.getJSONObject(i);
                Object[] rowData = {
                    item.getString("sale_month"),
                    item.getString("total_quantity_sold"),
                    item.getString("total_sales")
                };
                model.addRow(rowData);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        JFreeChart chart = createChart(sales_data, "Monthly Sales", "Month", "Total Sales");
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(600, 400));
        monthChartPanel.removeAll();
        monthChartPanel.add(chartPanel, BorderLayout.CENTER);
        monthChartPanel.repaint();
        monthChartPanel.revalidate();
    }

    private void updateYearlySalesTable(JSONArray sales_data) {
        DefaultTableModel model = (DefaultTableModel) yearlySalesTable.getModel();
        model.setRowCount(0);
        for (int i = 0; i < sales_data.length(); i++) {

            try {
                JSONObject item = sales_data.getJSONObject(i);
                Object[] rowData = {
                    item.getString("sale_year"),
                    item.getString("total_quantity_sold"),
                    item.getString("total_sales")
                };
                model.addRow(rowData);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        JFreeChart chart = createChart(sales_data, "Yearly Sales", "Year", "Total Sales");
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(600, 400));
        yearChartPanel.removeAll();
        yearChartPanel.add(chartPanel, BorderLayout.CENTER);
        yearChartPanel.repaint();
        yearChartPanel.revalidate();
    }

    private JFreeChart createChart(JSONArray data, String title, String categoryAxisLabel, String valueAxisLabel) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        try {
            for (int i = 0; i < data.length(); i++) {
                JSONObject item = data.getJSONObject(i);
                String label;
                if (item.has("sale_date")) {
                    label = item.getString("sale_date");
                } else if (item.has("sale_month")) {
                    label = "Year " + item.getInt("sale_year") + " Month " + item.getString("sale_month");
                } else {
                    label = "Year " + item.getInt("sale_year");
                }
                double value = item.getInt("total_quantity_sold");
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

    private void updateMetricsTable(JSONArray sales_data) {
        DefaultTableModel model = (DefaultTableModel) metricsTable.getModel();
        model.setRowCount(0);
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (int i = 0; i < sales_data.length(); i++) {

            try {
                JSONObject item = sales_data.getJSONObject(i);
                String productName = item.getString("product_name");
                int productId = item.getInt("product_id");
                int variantId = item.getInt("variant_id");
                double avgInventoryLevel = Double.parseDouble(item.getString("avg_inventory_level"));
                double sellThroughRate = Double.parseDouble(item.getString("sell_through_rate"));
                double turnoverRate = Double.parseDouble(item.getString("turnover_rate"));
                Object[] rowData = {
                    productName,
                    avgInventoryLevel,
                    sellThroughRate,
                    turnoverRate
                };
                model.addRow(rowData);
                String productVariant = productName;
                dataset.addValue(avgInventoryLevel, "Average Inventory Level", productVariant);
                dataset.addValue(sellThroughRate, "Sell-Through Rate (%)", productVariant);
                dataset.addValue(turnoverRate, "Turnover Rate (%)", productVariant);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

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
        //metricsChartPanel.add(chartPanel, BorderLayout.SOUTH);
        metricsChartPanel.removeAll();
        metricsChartPanel.add(chartPanel, BorderLayout.CENTER);
        metricsChartPanel.repaint();
        metricsChartPanel.revalidate();

    }

    private void printReport() {
        try {
            JTable currentTable = null;
            String tabTitle = null;
            int selectedIndex = inventoryReportTab.getSelectedIndex();
            switch (selectedIndex) {
                case 1:
                    currentTable = dailySalesTable;
                    tabTitle = "Sales Data";
                    break;
                case 2:
                    currentTable = monthlySalesTable;
                    tabTitle = "Sales Data";
                    break;
                case 3:
                    currentTable = yearlySalesTable;
                    tabTitle = "Sales Data";
                    break;
                case 4:
                    currentTable = metricsTable;
                    tabTitle = "Metrics Summary";
                    break;
            }
            boolean complete = currentTable.print();
            if (complete) {
                JOptionPane.showMessageDialog(this, "Printing Completed", "Print Report", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Printing Cancelled", "Print Report", JOptionPane.WARNING_MESSAGE);
            }
        } catch (PrinterException pe) {
            JOptionPane.showMessageDialog(this, "Printing Failed: " + pe.getMessage(), "Print Report", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveTableToCSV() {
        JTable currentTable = null;
        String tabTitle = null;
        int selectedIndex = inventoryReportTab.getSelectedIndex();
        switch (selectedIndex) {
                case 1:
                    currentTable = dailySalesTable;
                    tabTitle = "Sales Data";
                    break;
                case 2:
                    currentTable = monthlySalesTable;
                    tabTitle = "Sales Data";
                    break;
                case 3:
                    currentTable = yearlySalesTable;
                    tabTitle = "Sales Data";
                    break;
                case 4:
                    currentTable = metricsTable;
                    tabTitle = "Metrics Summary";
                    break;
        }

        if (currentTable != null && tabTitle != null) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Save as CSV");
            fileChooser.setSelectedFile(new File(tabTitle + ".csv"));

            int userSelection = fileChooser.showSaveDialog(this);
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();
                String filePath = fileToSave.getAbsolutePath();

                try (FileWriter writer = new FileWriter(filePath)) {
                    TableModel model = currentTable.getModel();
                    int rowCount = model.getRowCount();
                    int colCount = model.getColumnCount();

                    for (int i = 0; i < colCount; i++) {
                        writer.write(model.getColumnName(i));
                        if (i < colCount - 1) {
                            writer.write(",");
                        }
                    }
                    writer.write("\n");
                    for (int row = 0; row < rowCount; row++) {
                        for (int col = 0; col < colCount; col++) {
                            Object value = model.getValueAt(row, col);
                            writer.write(value.toString());
                            if (col < colCount - 1) {
                                writer.write(",");
                            }
                        }
                        writer.write("\n");
                    }

                    writer.close();
                    JOptionPane.showMessageDialog(this, "Table saved successfully as CSV", "Save CSV", JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Error saving table as CSV: " + ex.getMessage(), "Save CSV Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "No table selected to save", "Save CSV Error", JOptionPane.WARNING_MESSAGE);
        }
    }
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        inventoryReportTab = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jSplitPane3 = new javax.swing.JSplitPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        dailySalesTable = new javax.swing.JTable();
        dayChartPanel = new javax.swing.JPanel();
        printDailyInvReport = new javax.swing.JButton();
        csvDailyInvReport = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jSplitPane1 = new javax.swing.JSplitPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        monthlySalesTable = new javax.swing.JTable();
        monthChartPanel = new javax.swing.JPanel();
        printMonthlyInvReport = new javax.swing.JButton();
        csvMonthlynvReport = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        jSplitPane2 = new javax.swing.JSplitPane();
        jScrollPane3 = new javax.swing.JScrollPane();
        yearlySalesTable = new javax.swing.JTable();
        yearChartPanel = new javax.swing.JPanel();
        printYearlyInvReport = new javax.swing.JButton();
        csvYearlyInvReport = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jSplitPane4 = new javax.swing.JSplitPane();
        jScrollPane5 = new javax.swing.JScrollPane();
        metricsTable = new javax.swing.JTable();
        metricsChartPanel = new javax.swing.JPanel();
        printInvMetricsReport = new javax.swing.JButton();
        csvInvMetricsReport = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        greetLabel = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        employeeIdLabel = new javax.swing.JLabel();
        generateReportBtn = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        salesStartDate = new com.toedter.calendar.JDateChooser();
        jLabel1 = new javax.swing.JLabel();
        salesEndDate = new com.toedter.calendar.JDateChooser();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jSplitPane3.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        dailySalesTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Period", "Product Name", "Colour", "Size", "Total Quantity"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane1.setViewportView(dailySalesTable);

        jSplitPane3.setTopComponent(jScrollPane1);

        dayChartPanel.setLayout(new java.awt.BorderLayout());
        jSplitPane3.setRightComponent(dayChartPanel);

        printDailyInvReport.setBackground(new java.awt.Color(51, 102, 255));
        printDailyInvReport.setFont(new java.awt.Font("Helvetica Neue", 1, 13)); // NOI18N
        printDailyInvReport.setForeground(new java.awt.Color(255, 255, 255));
        printDailyInvReport.setText("Print");
        printDailyInvReport.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        printDailyInvReport.setOpaque(true);
        printDailyInvReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printDailyInvReportActionPerformed(evt);
            }
        });

        csvDailyInvReport.setBackground(new java.awt.Color(51, 102, 255));
        csvDailyInvReport.setFont(new java.awt.Font("Helvetica Neue", 1, 13)); // NOI18N
        csvDailyInvReport.setForeground(new java.awt.Color(255, 255, 255));
        csvDailyInvReport.setText("Save as CSV");
        csvDailyInvReport.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        csvDailyInvReport.setOpaque(true);
        csvDailyInvReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                csvDailyInvReportActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jSplitPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 1070, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(446, 446, 446)
                        .addComponent(printDailyInvReport, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(csvDailyInvReport, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(195, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jSplitPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 426, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(printDailyInvReport)
                    .addComponent(csvDailyInvReport))
                .addGap(40, 40, 40))
        );

        inventoryReportTab.addTab("Daily", jPanel2);

        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane1.setLastDividerLocation(100);

        monthlySalesTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Period", "Total Sales", "Total Quantity"
            }
        ));
        jScrollPane2.setViewportView(monthlySalesTable);

        jSplitPane1.setTopComponent(jScrollPane2);

        monthChartPanel.setLayout(new java.awt.BorderLayout());
        jSplitPane1.setRightComponent(monthChartPanel);

        printMonthlyInvReport.setBackground(new java.awt.Color(51, 102, 255));
        printMonthlyInvReport.setFont(new java.awt.Font("Helvetica Neue", 1, 13)); // NOI18N
        printMonthlyInvReport.setForeground(new java.awt.Color(255, 255, 255));
        printMonthlyInvReport.setText("Print");
        printMonthlyInvReport.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        printMonthlyInvReport.setOpaque(true);
        printMonthlyInvReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printMonthlyInvReportActionPerformed(evt);
            }
        });

        csvMonthlynvReport.setBackground(new java.awt.Color(51, 102, 255));
        csvMonthlynvReport.setFont(new java.awt.Font("Helvetica Neue", 1, 13)); // NOI18N
        csvMonthlynvReport.setForeground(new java.awt.Color(255, 255, 255));
        csvMonthlynvReport.setText("Save as CSV");
        csvMonthlynvReport.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        csvMonthlynvReport.setOpaque(true);
        csvMonthlynvReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                csvMonthlynvReportActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1259, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(446, 446, 446)
                .addComponent(printMonthlyInvReport, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(csvMonthlynvReport, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jSplitPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 424, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(csvMonthlynvReport)
                    .addComponent(printMonthlyInvReport))
                .addGap(40, 40, 40))
        );

        inventoryReportTab.addTab("Monthly", jPanel6);

        jSplitPane2.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane2.setLastDividerLocation(200);

        yearlySalesTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Period", "Total Sales", "Total Quantity"
            }
        ));
        jScrollPane3.setViewportView(yearlySalesTable);

        jSplitPane2.setLeftComponent(jScrollPane3);

        yearChartPanel.setLayout(new java.awt.BorderLayout());
        jSplitPane2.setBottomComponent(yearChartPanel);

        printYearlyInvReport.setBackground(new java.awt.Color(51, 102, 255));
        printYearlyInvReport.setFont(new java.awt.Font("Helvetica Neue", 1, 13)); // NOI18N
        printYearlyInvReport.setForeground(new java.awt.Color(255, 255, 255));
        printYearlyInvReport.setText("Print");
        printYearlyInvReport.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        printYearlyInvReport.setOpaque(true);
        printYearlyInvReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printYearlyInvReportActionPerformed(evt);
            }
        });

        csvYearlyInvReport.setBackground(new java.awt.Color(51, 102, 255));
        csvYearlyInvReport.setFont(new java.awt.Font("Helvetica Neue", 1, 13)); // NOI18N
        csvYearlyInvReport.setForeground(new java.awt.Color(255, 255, 255));
        csvYearlyInvReport.setText("Save as CSV");
        csvYearlyInvReport.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        csvYearlyInvReport.setOpaque(true);
        csvYearlyInvReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                csvYearlyInvReportActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addComponent(jSplitPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 1265, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(449, 449, 449)
                .addComponent(printYearlyInvReport, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(csvYearlyInvReport, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addComponent(jSplitPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 424, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(printYearlyInvReport)
                    .addComponent(csvYearlyInvReport))
                .addGap(40, 40, 40))
        );

        inventoryReportTab.addTab("Yearly", jPanel8);

        jSplitPane4.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        metricsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Product Name", "Average Inventory Level", "Sell-Through Rate (%)", "Turnover Rate"
            }
        ));
        jScrollPane5.setViewportView(metricsTable);

        jSplitPane4.setTopComponent(jScrollPane5);

        metricsChartPanel.setLayout(new java.awt.BorderLayout());
        jSplitPane4.setRightComponent(metricsChartPanel);

        printInvMetricsReport.setBackground(new java.awt.Color(51, 102, 255));
        printInvMetricsReport.setFont(new java.awt.Font("Helvetica Neue", 1, 13)); // NOI18N
        printInvMetricsReport.setForeground(new java.awt.Color(255, 255, 255));
        printInvMetricsReport.setText("Print");
        printInvMetricsReport.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        printInvMetricsReport.setOpaque(true);
        printInvMetricsReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printInvMetricsReportActionPerformed(evt);
            }
        });

        csvInvMetricsReport.setBackground(new java.awt.Color(51, 102, 255));
        csvInvMetricsReport.setFont(new java.awt.Font("Helvetica Neue", 1, 13)); // NOI18N
        csvInvMetricsReport.setForeground(new java.awt.Color(255, 255, 255));
        csvInvMetricsReport.setText("Save as CSV");
        csvInvMetricsReport.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        csvInvMetricsReport.setOpaque(true);
        csvInvMetricsReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                csvInvMetricsReportActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jSplitPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 1070, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(441, 441, 441)
                        .addComponent(printInvMetricsReport, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(csvInvMetricsReport, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(195, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jSplitPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 426, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(printInvMetricsReport)
                    .addComponent(csvInvMetricsReport))
                .addGap(40, 40, 40))
        );

        inventoryReportTab.addTab("Metrics", jPanel4);

        jScrollPane4.setViewportView(inventoryReportTab);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 1212, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 454, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30))
        );

        jPanel7.setBackground(new java.awt.Color(0, 0, 0));
        jPanel7.setPreferredSize(new java.awt.Dimension(187, 67));

        jLabel9.setFont(new java.awt.Font("Helvetica Neue", 1, 18)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Sales Amigo");

        jLabel39.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/salesamigo-logo.png"))); // NOI18N

        jButton7.setBackground(new java.awt.Color(51, 102, 255));
        jButton7.setFont(new java.awt.Font("Helvetica Neue", 1, 19)); // NOI18N
        jButton7.setForeground(new java.awt.Color(255, 255, 255));
        jButton7.setText("Logout");
        jButton7.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton7.setOpaque(true);
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton8.setBackground(new java.awt.Color(51, 102, 255));
        jButton8.setFont(new java.awt.Font("Helvetica Neue", 1, 19)); // NOI18N
        jButton8.setForeground(new java.awt.Color(255, 255, 255));
        jButton8.setText("Back to Reports");
        jButton8.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton8.setOpaque(true);
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(jLabel39, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(80, 80, 80)
                .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jLabel39)
                        .addGap(0, 13, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton7)
                    .addComponent(jButton8))
                .addContainerGap(27, Short.MAX_VALUE))
        );

        jLabel7.setFont(new java.awt.Font("Helvetica Neue", 1, 24)); // NOI18N
        jLabel7.setText("Inventory Report");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        jLabel8.setFont(new java.awt.Font("Helvetica Neue", 0, 15)); // NOI18N
        jLabel8.setText("Copyright © 2024 Sales Amigo");

        greetLabel.setFont(new java.awt.Font("Helvetica Neue", 2, 21)); // NOI18N
        greetLabel.setText(" ");

        jLabel10.setFont(new java.awt.Font("Helvetica Neue", 1, 18)); // NOI18N
        jLabel10.setText("Staff ID:");

        employeeIdLabel.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        employeeIdLabel.setText(" ");

        generateReportBtn.setBackground(new java.awt.Color(51, 102, 255));
        generateReportBtn.setFont(new java.awt.Font("Helvetica Neue", 1, 14)); // NOI18N
        generateReportBtn.setForeground(new java.awt.Color(255, 255, 255));
        generateReportBtn.setText("Generate Report");
        generateReportBtn.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        generateReportBtn.setOpaque(true);
        generateReportBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                generateReportBtnActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        jLabel2.setText("End");

        salesStartDate.setDate(cal.getTime());

        jLabel1.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        jLabel1.setText("Start");

        salesEndDate.setDate(cal.getTime());

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, 1391, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(employeeIdLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(greetLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 315, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel7)
                .addGap(630, 630, 630))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(salesStartDate, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(salesEndDate, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(40, 40, 40)
                        .addComponent(generateReportBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(61, 61, 61))
            .addGroup(layout.createSequentialGroup()
                .addGap(580, 580, 580)
                .addComponent(jLabel8)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10)
                            .addComponent(employeeIdLabel))
                        .addGap(18, 18, 18)
                        .addComponent(greetLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(39, 39, 39)
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(generateReportBtn)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1)
                            .addComponent(salesEndDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(salesStartDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(10, 10, 10)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(44, 44, 44)
                .addComponent(jLabel8)
                .addContainerGap(30, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void generateReportBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_generateReportBtnActionPerformed
        try {
            // TODO add your handling code here:
            Date startDate = salesStartDate.getDate();
            Date endDate = salesEndDate.getDate();

            ReportFilter filter = new ReportFilter(startDate, endDate);
            JSONArray dailySales = reportGenerator.getInventoryReport(filter).getJSONArray("daily_sales");
            JSONArray monthlySales = reportGenerator.getInventoryReport(filter).getJSONArray("monthly_sales");
            JSONArray yearlySales = reportGenerator.getInventoryReport(filter).getJSONArray("yearly_sales");
            JSONArray metrics = reportGenerator.getInventoryReport(filter).getJSONArray("inventory_metrics");

            updateDailySalesTable(dailySales);
            updateMonthlySalesTable(monthlySales);
            updateYearlySalesTable(yearlySales);
            updateMetricsTable(metrics);
        } catch (JSONException ex) {
            Logger.getLogger(SalesReportFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_generateReportBtnActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:
        LoginFrame loginFrame = new LoginFrame();
        loginFrame.setVisible(true);
        dispose();
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        // TODO add your handling code here:
        ReportFrame frame = new ReportFrame(employee);
        frame.setVisible(true);
        dispose();
    }//GEN-LAST:event_jButton8ActionPerformed

    private void csvInvMetricsReportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_csvInvMetricsReportActionPerformed
        // TODO add your handling code here:
        saveTableToCSV();
    }//GEN-LAST:event_csvInvMetricsReportActionPerformed

    private void printInvMetricsReportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printInvMetricsReportActionPerformed
        // TODO add your handling code here:
        printReport();
    }//GEN-LAST:event_printInvMetricsReportActionPerformed

    private void csvYearlyInvReportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_csvYearlyInvReportActionPerformed
        // TODO add your handling code here:
        saveTableToCSV();
    }//GEN-LAST:event_csvYearlyInvReportActionPerformed

    private void printYearlyInvReportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printYearlyInvReportActionPerformed
        // TODO add your handling code here:
        printReport();
    }//GEN-LAST:event_printYearlyInvReportActionPerformed

    private void csvMonthlynvReportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_csvMonthlynvReportActionPerformed
        // TODO add your handling code here:
        saveTableToCSV();
    }//GEN-LAST:event_csvMonthlynvReportActionPerformed

    private void printMonthlyInvReportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printMonthlyInvReportActionPerformed
        // TODO add your handling code here:
        printReport();
    }//GEN-LAST:event_printMonthlyInvReportActionPerformed

    private void csvDailyInvReportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_csvDailyInvReportActionPerformed
        // TODO add your handling code here:
        saveTableToCSV();
    }//GEN-LAST:event_csvDailyInvReportActionPerformed

    private void printDailyInvReportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printDailyInvReportActionPerformed
        // TODO add your handling code here:
        printReport();
    }//GEN-LAST:event_printDailyInvReportActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(InventoryReportFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(InventoryReportFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(InventoryReportFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(InventoryReportFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                int id = 1;
        String firstname = "John";
        String lastname = "Doe";
        String email = "johndoe@example.com";
        String contactNumber = "123-456-7890";
        String address = "123 Street, City, Country";
        String joinDate = "2024-01-01"; // Assuming join date is in yyyy-MM-dd format
        String role = "Employee";
        String department = "IT";
        float salary = 50000.0f; // Assuming salary is in dollars
        String password = "password123";

        Employee dummyUser = new Employee(id, firstname, lastname, email, contactNumber, address,
                joinDate, role, department, salary, password);
                new InventoryReportFrame(dummyUser).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton csvDailyInvReport;
    private javax.swing.JButton csvInvMetricsReport;
    private javax.swing.JButton csvMonthlynvReport;
    private javax.swing.JButton csvYearlyInvReport;
    private javax.swing.JTable dailySalesTable;
    private javax.swing.JPanel dayChartPanel;
    private javax.swing.JLabel employeeIdLabel;
    private javax.swing.JButton generateReportBtn;
    private javax.swing.JLabel greetLabel;
    private javax.swing.JTabbedPane inventoryReportTab;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JSplitPane jSplitPane3;
    private javax.swing.JSplitPane jSplitPane4;
    private javax.swing.JPanel metricsChartPanel;
    private javax.swing.JTable metricsTable;
    private javax.swing.JPanel monthChartPanel;
    private javax.swing.JTable monthlySalesTable;
    private javax.swing.JButton printDailyInvReport;
    private javax.swing.JButton printInvMetricsReport;
    private javax.swing.JButton printMonthlyInvReport;
    private javax.swing.JButton printYearlyInvReport;
    private com.toedter.calendar.JDateChooser salesEndDate;
    private com.toedter.calendar.JDateChooser salesStartDate;
    private javax.swing.JPanel yearChartPanel;
    private javax.swing.JTable yearlySalesTable;
    // End of variables declaration//GEN-END:variables
}
