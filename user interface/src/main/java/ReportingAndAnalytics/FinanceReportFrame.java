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
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author britt
 */
public class FinanceReportFrame extends javax.swing.JFrame {

    /**
     * Creates new form FinanceReportFrame
     */
    Calendar cal = Calendar.getInstance();
    ReportGenerator reportGenerator;
    private Employee employee;
    public FinanceReportFrame(Employee user) {
        initComponents();
        
        this.employee = user;
        monthlySalesTable.setAutoCreateRowSorter(true);
        yearlySalesTable.setAutoCreateRowSorter(true);
        reportGenerator = new ReportGenerator();
        employeeIdLabel.setText(String.valueOf(user.getId()));
        greetLabel.setText("Hello " + user.getFirstname() + ",");
    }

    private void updateMonthlySalesTable(JSONArray sales_data) {
        DefaultTableModel model = (DefaultTableModel) monthlySalesTable.getModel();
        model.setRowCount(0);
        for (int i = 0; i < sales_data.length(); i++) {

            try {
                JSONObject item = sales_data.getJSONObject(i);
                Object[] rowData = {
                    item.getString("sale_year") + " " + item.getString("sale_month"),
                    item.getString("total_sales"),
                    item.getString("total_expenses"),
                    item.getString("profit_loss")                       
                        ,};
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
                    item.getString("total_sales"),
                    item.getString("total_expenses"),
                    item.getString("profit_loss")  
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
                if (item.has("sale_month")) {
                    label = "Year " + item.getInt("sale_year") + " Month " + item.getString("sale_month");
                } else {
                    label = "Year " + item.getInt("sale_year");
                }
                double profit_loss = item.getInt("profit_loss");
                dataset.addValue(profit_loss, "Profit/Loss", label);
                
                double sales = item.getInt("total_sales");
                dataset.addValue(sales, "Sales", label);
                
                double expenses = item.getInt("total_expenses");
                dataset.addValue(expenses, "Expenses", label);
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

    private void printReport() {
        try {
            JTable currentTable = null;
            String tabTitle = null;
            int selectedIndex = inventoryReportTab.getSelectedIndex();
            switch (selectedIndex) {
                case 1:
                    // currentTable = dailySalesTable;
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
                    //currentTable = metricsTable;
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
            case 0:
                currentTable = monthlySalesTable;
                tabTitle = "Sales Data";
                break;
            case 1:
                currentTable = yearlySalesTable ;
                tabTitle = "Sales Data";
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

        jPanel7 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        salesStartDate = new com.toedter.calendar.JDateChooser();
        salesEndDate = new com.toedter.calendar.JDateChooser();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        generateReportBtn = new javax.swing.JButton();
        inventoryReportTab = new javax.swing.JTabbedPane();
        jPanel6 = new javax.swing.JPanel();
        jSplitPane1 = new javax.swing.JSplitPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        monthlySalesTable = new javax.swing.JTable();
        monthChartPanel = new javax.swing.JPanel();
        csvMonthlynvReport = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        jSplitPane2 = new javax.swing.JSplitPane();
        yearChartPanel = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        yearlySalesTable = new javax.swing.JTable();
        csvYearlyInvReport = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        greetLabel = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        employeeIdLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(1544, 728));

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
                .addContainerGap(16, Short.MAX_VALUE))
        );

        salesStartDate.setDate(cal.getTime());

        salesEndDate.setDate(cal.getTime());

        jLabel1.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        jLabel1.setText("Start");

        jLabel2.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        jLabel2.setText("End");

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

        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane1.setLastDividerLocation(100);

        monthlySalesTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Period", "Total Sales", "Total Expenses", "Profit/Loss"
            }
        ));
        jScrollPane2.setViewportView(monthlySalesTable);

        jSplitPane1.setTopComponent(jScrollPane2);

        monthChartPanel.setLayout(new java.awt.BorderLayout());
        jSplitPane1.setRightComponent(monthChartPanel);

        csvMonthlynvReport.setBackground(new java.awt.Color(51, 102, 255));
        csvMonthlynvReport.setFont(new java.awt.Font("Helvetica Neue", 1, 14)); // NOI18N
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
                .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1103, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(csvMonthlynvReport, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(416, 416, 416))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jSplitPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 424, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(csvMonthlynvReport)
                .addContainerGap(20, Short.MAX_VALUE))
        );

        inventoryReportTab.addTab("Monthly", jPanel6);

        jSplitPane2.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane2.setLastDividerLocation(200);

        yearChartPanel.setLayout(new java.awt.BorderLayout());
        jSplitPane2.setBottomComponent(yearChartPanel);

        yearlySalesTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Period", "Total Sales", "Total Expenses", "Profit/Loss"
            }
        ));
        jScrollPane3.setViewportView(yearlySalesTable);

        jSplitPane2.setLeftComponent(jScrollPane3);

        csvYearlyInvReport.setBackground(new java.awt.Color(51, 102, 255));
        csvYearlyInvReport.setFont(new java.awt.Font("Helvetica Neue", 1, 14)); // NOI18N
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
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jSplitPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 1097, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(csvYearlyInvReport, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(419, 419, 419))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addComponent(jSplitPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 424, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                .addComponent(csvYearlyInvReport))
        );

        inventoryReportTab.addTab("Yearly", jPanel8);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(salesStartDate, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(salesEndDate, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(40, 40, 40)
                        .addComponent(generateReportBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(inventoryReportTab, javax.swing.GroupLayout.PREFERRED_SIZE, 1109, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(generateReportBtn)
                    .addComponent(jLabel1)
                    .addComponent(salesEndDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(salesStartDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(inventoryReportTab, javax.swing.GroupLayout.PREFERRED_SIZE, 501, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jLabel3.setFont(new java.awt.Font("Helvetica Neue", 1, 24)); // NOI18N
        jLabel3.setText("Finance Report");

        jLabel4.setFont(new java.awt.Font("Helvetica Neue", 0, 15)); // NOI18N
        jLabel4.setText("Copyright © 2024 Sales Amigo");

        greetLabel.setFont(new java.awt.Font("Helvetica Neue", 2, 21)); // NOI18N
        greetLabel.setText(" ");

        jLabel5.setFont(new java.awt.Font("Helvetica Neue", 1, 18)); // NOI18N
        jLabel5.setText("Staff ID:");

        employeeIdLabel.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        employeeIdLabel.setText(" ");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, 1503, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(156, 156, 156)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 1116, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel4)
                .addGap(595, 595, 595))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(employeeIdLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(greetLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 315, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 340, Short.MAX_VALUE)
                .addComponent(jLabel3)
                .addGap(635, 635, 635))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(employeeIdLabel))
                        .addGap(18, 18, 18)
                        .addComponent(greetLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, 0)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addContainerGap(61, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void generateReportBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_generateReportBtnActionPerformed
        try {
            // TODO add your handling code here:
            Date startDate = salesStartDate.getDate();
            Date endDate = salesEndDate.getDate();

            ReportFilter filter = new ReportFilter(startDate, endDate);
            JSONArray monthlySales = reportGenerator.getFinanceReport(filter).getJSONArray("monthly_profit_loss_report");
            JSONArray yearlySales = reportGenerator.getFinanceReport(filter).getJSONArray("yearly_profit_loss_report");

            updateMonthlySalesTable(monthlySales);
            updateYearlySalesTable(yearlySales);
        } catch (JSONException ex) {
            Logger.getLogger(SalesReportFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_generateReportBtnActionPerformed

    private void csvYearlyInvReportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_csvYearlyInvReportActionPerformed
        // TODO add your handling code here:
        saveTableToCSV();
    }//GEN-LAST:event_csvYearlyInvReportActionPerformed

    private void csvMonthlynvReportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_csvMonthlynvReportActionPerformed
        // TODO add your handling code here:
        saveTableToCSV();
    }//GEN-LAST:event_csvMonthlynvReportActionPerformed

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
            java.util.logging.Logger.getLogger(FinanceReportFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FinanceReportFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FinanceReportFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FinanceReportFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
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
                new FinanceReportFrame(dummyUser).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton csvMonthlynvReport;
    private javax.swing.JButton csvYearlyInvReport;
    private javax.swing.JLabel employeeIdLabel;
    private javax.swing.JButton generateReportBtn;
    private javax.swing.JLabel greetLabel;
    private javax.swing.JTabbedPane inventoryReportTab;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JPanel monthChartPanel;
    private javax.swing.JTable monthlySalesTable;
    private com.toedter.calendar.JDateChooser salesEndDate;
    private com.toedter.calendar.JDateChooser salesStartDate;
    private javax.swing.JPanel yearChartPanel;
    private javax.swing.JTable yearlySalesTable;
    // End of variables declaration//GEN-END:variables
}
