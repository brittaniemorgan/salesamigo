/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package ReportingAndAnalytics;

import Authentication.LoginFrame;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;
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
public class SalesReportFrame extends javax.swing.JFrame {

    /**
     * Creates new form SalesReportFrame
     */
    ReportGenerator reportGenerator;
    Calendar cal = Calendar.getInstance();

    public SalesReportFrame() {
        initComponents();
        dailySalesTable.setAutoCreateRowSorter(true);
        monthlySalesTable.setAutoCreateRowSorter(true);
        yearlySalesTable.setAutoCreateRowSorter(true);
        reportGenerator = new ReportGenerator();
    }

    private void updateDailySalesTable(JSONArray sales_data) {
        DefaultTableModel model = (DefaultTableModel) dailySalesTable.getModel();
        model.setRowCount(0);
        for (int i = 0; i < sales_data.length(); i++) {

            try {
                JSONObject item = sales_data.getJSONObject(i);
                Object[] rowData = {
                    item.getString("sale_date"),
                    item.getString("total_quantity"),
                    item.getString("total_sales")
                };
                model.addRow(rowData);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        JFreeChart chart = createChart(sales_data, "Daily Sales", "Day", "Total Sales");
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(600, 400)); 
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
                    item.getString("total_quantity"),
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
                item.getString("total_quantity"),
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

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        salesStartDate = new com.toedter.calendar.JDateChooser();
        salesEndDate = new com.toedter.calendar.JDateChooser();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        generateReportBtn = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jSplitPane3 = new javax.swing.JSplitPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        dailySalesTable = new javax.swing.JTable();
        dayChartPanel = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jSplitPane1 = new javax.swing.JSplitPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        monthlySalesTable = new javax.swing.JTable();
        monthChartPanel = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jSplitPane2 = new javax.swing.JSplitPane();
        yearChartPanel = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        yearlySalesTable = new javax.swing.JTable();
        jPanel7 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        jButton7 = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(1442, 800));

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
        generateReportBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                generateReportBtnActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Helvetica Neue", 0, 16)); // NOI18N
        jLabel3.setText("Total sales");

        jLabel4.setFont(new java.awt.Font("Helvetica Neue", 0, 16)); // NOI18N
        jLabel4.setText("Online");

        jLabel5.setFont(new java.awt.Font("Helvetica Neue", 0, 16)); // NOI18N
        jLabel5.setText("Instore");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(953, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(56, 56, 56)
                .addComponent(jLabel3)
                .addGap(18, 18, 18)
                .addComponent(jLabel4)
                .addGap(18, 18, 18)
                .addComponent(jLabel5)
                .addContainerGap(320, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Summary", jPanel3);

        jSplitPane3.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        dailySalesTable.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(dailySalesTable);

        jSplitPane3.setTopComponent(jScrollPane1);

        dayChartPanel.setLayout(new java.awt.BorderLayout());
        jSplitPane3.setRightComponent(dayChartPanel);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jSplitPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 1070, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(39, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jSplitPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 426, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(46, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Daily", jPanel2);

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

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1103, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jSplitPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 424, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(48, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Monthly", jPanel6);

        jSplitPane2.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane2.setLastDividerLocation(200);

        yearChartPanel.setLayout(new java.awt.BorderLayout());
        jSplitPane2.setBottomComponent(yearChartPanel);

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

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 1109, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSplitPane2)
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSplitPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 458, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 14, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Yearly", jPanel8);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(salesStartDate, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(salesEndDate, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(40, 40, 40)
                        .addComponent(generateReportBtn))
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1109, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(generateReportBtn)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1)
                    .addComponent(salesEndDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(salesStartDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 507, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(jLabel39, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 870, Short.MAX_VALUE)
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
                .addComponent(jButton7)
                .addContainerGap(27, Short.MAX_VALUE))
        );

        jLabel7.setFont(new java.awt.Font("Helvetica Neue", 1, 24)); // NOI18N
        jLabel7.setText("Sales Report");

        jLabel8.setFont(new java.awt.Font("Helvetica Neue", 0, 15)); // NOI18N
        jLabel8.setText("Copyright © 2024 Sales Amigo");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, 1175, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addGap(653, 653, 653))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(29, 29, 29))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addGap(604, 604, 604))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 487, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 57, Short.MAX_VALUE)
                .addComponent(jLabel8)
                .addGap(44, 44, 44))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void generateReportBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_generateReportBtnActionPerformed
        try {
            // TODO add your handling code here:
            Date startDate = salesStartDate.getDate();
            Date endDate = salesEndDate.getDate();

            ReportFilter filter = new ReportFilter(startDate, endDate);
            JSONArray dailySales = reportGenerator.getSalesReport(filter).getJSONArray("daily_sales");
            JSONArray monthlySales = reportGenerator.getSalesReport(filter).getJSONArray("monthly_sales");
            JSONArray yearlySales = reportGenerator.getSalesReport(filter).getJSONArray("yearly_sales");

            updateDailySalesTable(dailySales);
            updateMonthlySalesTable(monthlySales);
            updateYearlySalesTable(yearlySales);
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
            java.util.logging.Logger.getLogger(SalesReportFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SalesReportFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SalesReportFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SalesReportFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new SalesReportFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable dailySalesTable;
    private javax.swing.JPanel dayChartPanel;
    private javax.swing.JButton generateReportBtn;
    private javax.swing.JButton jButton7;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JSplitPane jSplitPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JPanel monthChartPanel;
    private javax.swing.JTable monthlySalesTable;
    private com.toedter.calendar.JDateChooser salesEndDate;
    private com.toedter.calendar.JDateChooser salesStartDate;
    private javax.swing.JPanel yearChartPanel;
    private javax.swing.JTable yearlySalesTable;
    // End of variables declaration//GEN-END:variables
}
