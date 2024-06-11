/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package PointOfSale;

import Authentication.User;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author britt
 */
public class SalesHistoryFrame extends javax.swing.JFrame {

    /**
     * Creates new form SalesHistoryFrame
     */
    POS pos;
    User user;
    public SalesHistoryFrame(User user) {
        initComponents();
        this.user = user;
        pos = new POS(user);
        updateTransactionsTable(pos.getTransactionHistory());
    }
    
    private void updateTransactionsTable(ArrayList<Transaction> transactions) {
        DefaultTableModel model = (DefaultTableModel) transactionTable.getModel();
        model.setRowCount(0);

        try {
            for (Transaction transaction : transactions) {
                Object[] rowData = {
                    transaction.getTransactionId(),
                    transaction.getTransactionDate(),
                    transaction.getEmployeeId(),
                    transaction.getTotal(),
                    transaction.getPaymentMethod()
                };
                //System.out.println(product.getName());
                model.addRow(rowData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void updateItemsTable(Integer transactionID) {
        DefaultTableModel model = (DefaultTableModel) itemsTable.getModel();
        model.setRowCount(0);
        try {
            for (Transaction transaction : pos.getTransactionHistory()) {
                
                if (transactionID == null || transaction.getTransactionId() == transactionID) {
                    for (TransactionItem item : transaction.getItems()) {
                        Object[] rowData = {
                            item.getTransactionItemId(),
                            item.getProductId(),
                            item.getPrice(),                          
                            item.getQuantity(),
                            item.getTotal(),
                            item.getQuantity(),
                            false
                        };
                        model.addRow(rowData);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void updateRefundsTable(Integer transactionID) {
        DefaultTableModel model = (DefaultTableModel) refundsTable.getModel();
        model.setRowCount(0);
        try {
            for (Transaction transaction : pos.getTransactionHistory()) {
                
                if (transactionID == null || transaction.getTransactionId() == transactionID) {
                    for (RefundItem item : transaction.getRefundItems()) {
                        Object[] rowData = {
                            item.getTransactionItemId(),
                            item.getPrice(),                          
                            item.getQuantity(),
                            item.isRestock()
                        };
                        model.addRow(rowData);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /*

    private void updateVariantsTable() {
        DefaultTableModel model = (DefaultTableModel) variantTable.getModel();
        model.setRowCount(0);

        try {
            for (Product product : inventory.getProducts()) {
                for (ProductVariant variant : product.getVariants()) {
                    Object[] rowData = {
                        variant.getVariantID(),
                        product.getID(),
                        product.getName(),
                        variant.getSize(),
                        variant.getColour(),
                        variant.getPrice(),
                        variant.getQuantity(),
                        variant.getMinQuantity()
                    };
                    //System.out.println(product.getName());
                    model.addRow(rowData);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateVariantsTable(Integer productId) {
        DefaultTableModel model = (DefaultTableModel) variantTable.getModel();
        model.setRowCount(0);

        try {
            for (Product product : inventory.getProducts()) {
                if (productId == null || product.getID() == productId) {
                    for (ProductVariant variant : product.getVariants()) {
                        Object[] rowData = {
                            variant.getVariantID(),
                            product.getID(),
                            product.getName(),
                            variant.getSize(),
                            variant.getColour(),
                            variant.getPrice(),
                            variant.getQuantity(),
                            variant.getMinQuantity()
                        };
                        model.addRow(rowData);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        refundsTable = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        transactionTable = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        itemsTable = new javax.swing.JTable();
        refundBtn = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        totalTxt = new javax.swing.JLabel();
        totalBtn = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jButton5 = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(1439, 1051));

        refundsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "ID", "Price", "Quantity", "Restocked"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane1.setViewportView(refundsTable);

        transactionTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "ID", "Date", "Employee", "Amount", "Payment Method"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Object.class, java.lang.Object.class, java.lang.Double.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        transactionTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                transactionTableMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(transactionTable);

        itemsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Product ID", "Price", "Quantity Bought", "Total", "Quantity To Refund", "Refund", "Should Restock"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Integer.class, java.lang.Double.class, java.lang.Integer.class, java.lang.Double.class, java.lang.Integer.class, java.lang.Boolean.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, true, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        itemsTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                itemsTableMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(itemsTable);

        refundBtn.setBackground(new java.awt.Color(51, 102, 255));
        refundBtn.setFont(new java.awt.Font("Helvetica Neue", 1, 14)); // NOI18N
        refundBtn.setForeground(new java.awt.Color(255, 255, 255));
        refundBtn.setText("Issue Refund");
        refundBtn.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        refundBtn.setOpaque(true);
        refundBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refundBtnActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Helvetica Neue", 1, 14)); // NOI18N
        jLabel3.setText("Total:");

        totalTxt.setFont(new java.awt.Font("Helvetica Neue", 0, 14)); // NOI18N
        totalTxt.setText(" ");

        totalBtn.setBackground(new java.awt.Color(51, 102, 255));
        totalBtn.setFont(new java.awt.Font("Helvetica Neue", 1, 14)); // NOI18N
        totalBtn.setForeground(new java.awt.Color(255, 255, 255));
        totalBtn.setText("Total");
        totalBtn.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        totalBtn.setOpaque(true);
        totalBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                totalBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(135, 135, 135)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 1014, Short.MAX_VALUE)
                    .addComponent(jScrollPane2)
                    .addComponent(jScrollPane1))
                .addGap(45, 45, 45)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(totalBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(totalTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(refundBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(35, 35, 35)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(189, 189, 189)
                        .addComponent(totalBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(refundBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(totalTxt))))
                .addGap(35, 35, 35)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(33, Short.MAX_VALUE))
        );

        refundBtn.getAccessibleContext().setAccessibleDescription("");

        jPanel2.setBackground(new java.awt.Color(0, 0, 0));
        jPanel2.setPreferredSize(new java.awt.Dimension(187, 67));

        jLabel5.setFont(new java.awt.Font("Helvetica Neue", 1, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Sales Amigo");

        jLabel33.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/salesamigo-logo.png"))); // NOI18N

        jButton5.setBackground(new java.awt.Color(51, 102, 255));
        jButton5.setFont(new java.awt.Font("Helvetica Neue", 1, 19)); // NOI18N
        jButton5.setForeground(new java.awt.Color(255, 255, 255));
        jButton5.setText("Logout");
        jButton5.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton5.setOpaque(true);
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 1073, Short.MAX_VALUE)
                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jLabel33)
                        .addGap(0, 13, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jButton5)
                .addContainerGap(27, Short.MAX_VALUE))
        );

        jLabel6.setFont(new java.awt.Font("Helvetica Neue", 1, 32)); // NOI18N
        jLabel6.setText("Sales History");

        jLabel4.setFont(new java.awt.Font("Helvetica Neue", 0, 15)); // NOI18N
        jLabel4.setText("Copyright © 2024 Sales Amigo");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 1378, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(611, 611, 611))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(615, 615, 615))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel6)
                .addGap(30, 30, 30)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addContainerGap(34, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void itemsTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_itemsTableMouseClicked
        // TODO add your handling code here:                    
    }//GEN-LAST:event_itemsTableMouseClicked

    private void transactionTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_transactionTableMouseClicked
        // TODO add your handling code here:
        DefaultTableModel model = (DefaultTableModel) transactionTable.getModel();
        int selectedRowIndex = transactionTable.getSelectedRow();

        int id = (int) model.getValueAt(selectedRowIndex, 0);
        updateItemsTable(id);  
        updateRefundsTable(id);
    }//GEN-LAST:event_transactionTableMouseClicked

    private void refundBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refundBtnActionPerformed
        // TODO add your handling code here:
        DefaultTableModel transactionModel = (DefaultTableModel) transactionTable.getModel();
        int transactionRow = transactionTable.getSelectedRow();
        
        DefaultTableModel itemModel = (DefaultTableModel) itemsTable.getModel();
        int itemRow = itemsTable.getSelectedRow();
        int transactionId = (int) transactionModel.getValueAt(transactionRow, 0);
        if (transactionRow != -1) {
            ArrayList<RefundItem> refundItems = new ArrayList<>();
            for (int i = 0; i < itemModel.getRowCount(); i++){
                boolean shouldRefund = (boolean) itemModel.getValueAt(i, 6);
                if (shouldRefund){
                    int productId = (int) itemModel.getValueAt(i, 1);
                    int quantity = (int) itemModel.getValueAt(i, 5);
                    int transactionItemId = (int) itemModel.getValueAt(i, 0);
                    double price = (double) itemModel.getValueAt(i, 2);
                    boolean restock = false;
                    if (itemModel.getValueAt(i, 7) != null){
                        restock = (boolean) itemModel.getValueAt(i, 7);
                    }
                    refundItems.add(new RefundItem(transactionItemId, productId, quantity, price, restock));
                }
            }
            
           // String message = String.format("Variant Deletion:\nProduct Name: %s\nSize: %s\nColour: %s\nQuantity: %s\nPrice: %.2f\n\nMin Quantity: %d",
              //      product.getName(), size, colour, quantity, price, minQuantity);

            int option = JOptionPane.showConfirmDialog(this, "" + "\nAre you sure you want to refund these items?", "Refund", JOptionPane.YES_NO_OPTION);

            if (option == JOptionPane.YES_OPTION) {
                String feedback = pos.processRefund(transactionId, refundItems, user.getId());
                JOptionPane.showMessageDialog(this, feedback, "Variant Deleted", JOptionPane.INFORMATION_MESSAGE);
                updateItemsTable(transactionId);
                updateRefundsTable(transactionId);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Please select a vairant to delete.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_refundBtnActionPerformed

    private void totalBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_totalBtnActionPerformed
        // TODO add your handling code here:
        DefaultTableModel transactionModel = (DefaultTableModel) transactionTable.getModel();
        int transactionRow = transactionTable.getSelectedRow();
        double sum = 0;
        DefaultTableModel itemModel = (DefaultTableModel) itemsTable.getModel();
        int itemRow = itemsTable.getSelectedRow();
        int transactionId = (int) transactionModel.getValueAt(transactionRow, 0);
        if (transactionRow != -1) {
            ArrayList<RefundItem> refundItems = new ArrayList<>();
            for (int i = 0; i < itemModel.getRowCount(); i++){
                boolean shouldRefund = (boolean) itemModel.getValueAt(i, 6);
                if (shouldRefund){
                    int quantity = (int) itemModel.getValueAt(i, 5);
                    double price = (double) itemModel.getValueAt(i, 2);
                    double total = price * quantity;
                    sum += total;
                }
                boolean restock = false;
                    if (itemModel.getValueAt(i, 7) != null){
                        restock = (boolean) itemModel.getValueAt(i, 7);
                    }
                System.out.println(restock);
            }
            NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.US);
            totalTxt.setText(currencyFormatter.format(sum));
        } else {
            JOptionPane.showMessageDialog(null, "Please select a vairant to delete.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_totalBtnActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        LoginFrame loginFrame = new LoginFrame();
        loginFrame.setVisible(true);
        dispose();
    }//GEN-LAST:event_jButton5ActionPerformed

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
            java.util.logging.Logger.getLogger(SalesHistoryFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SalesHistoryFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SalesHistoryFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SalesHistoryFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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

        User dummyUser = new User(id, firstname, lastname, email, contactNumber, address,
                joinDate, role, department, salary, password);
                new SalesHistoryFrame(dummyUser).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable itemsTable;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JButton refundBtn;
    private javax.swing.JTable refundsTable;
    private javax.swing.JButton totalBtn;
    private javax.swing.JLabel totalTxt;
    private javax.swing.JTable transactionTable;
    // End of variables declaration//GEN-END:variables
}
