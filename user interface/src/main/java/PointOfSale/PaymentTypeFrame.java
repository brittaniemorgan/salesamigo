/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package PointOfSale;

import Authentication.AdminFrame;
import Authentication.User;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author britt
 */
public class PaymentTypeFrame extends javax.swing.JFrame {

    /**
     * Creates new form PaymentTypeFrame
     */
    POS pos;
    User employee;
    public PaymentTypeFrame(User user) {
        initComponents();
        this.employee = user;
        pos = new POS(user);
        updatePaymentTable();
        employeeIdLabel.setText(String.valueOf(user.getId()));
        greetLabel.setText("Hello " + user.getFirstname() + ",");
    }
    
    
    private void updatePaymentTable() {
        DefaultTableModel model = (DefaultTableModel) paymentTable.getModel();
        model.setRowCount(0);
        try {
            ArrayList<PaymentType> paymentTypes = pos.getPaymentTypes();
            for (PaymentType paymentType : paymentTypes) {
                Object[] rowData = {
                    paymentType.getId(),
                    paymentType.getName()};
                model.addRow(rowData);
            }
            //loadPaymentDropDown();
        } catch (Exception e) {
            e.printStackTrace();
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

        jPanel5 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        paymentNameField = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        paymentTable = new javax.swing.JTable();
        jLabel35 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jPanel14 = new javax.swing.JPanel();
        addPaymentBtn = new javax.swing.JButton();
        editPaymentBtn = new javax.swing.JButton();
        deletePaymentBtn = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        greetLabel = new javax.swing.JLabel();
        employeeIdLabel = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        jButton6 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel5.setPreferredSize(new java.awt.Dimension(1442, 800));

        jLabel13.setFont(new java.awt.Font("Helvetica Neue", 0, 16)); // NOI18N
        jLabel13.setText("Enter Payment Type:");

        paymentNameField.setFont(new java.awt.Font("Helvetica Neue", 0, 14)); // NOI18N
        paymentNameField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                paymentNameFieldActionPerformed(evt);
            }
        });

        paymentTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "ID", "Name"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        paymentTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                paymentTableMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(paymentTable);

        jLabel35.setFont(new java.awt.Font("Helvetica Neue", 0, 15)); // NOI18N
        jLabel35.setText("Copyright © 2024 Sales Amigo");

        jLabel10.setFont(new java.awt.Font("Helvetica Neue", 1, 32)); // NOI18N
        jLabel10.setText("Payment Type");

        addPaymentBtn.setBackground(new java.awt.Color(51, 102, 255));
        addPaymentBtn.setFont(new java.awt.Font("Helvetica Neue", 1, 14)); // NOI18N
        addPaymentBtn.setForeground(new java.awt.Color(255, 255, 255));
        addPaymentBtn.setText("Add");
        addPaymentBtn.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        addPaymentBtn.setOpaque(true);
        addPaymentBtn.setPreferredSize(new java.awt.Dimension(72, 24));
        addPaymentBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addPaymentBtnActionPerformed(evt);
            }
        });

        editPaymentBtn.setBackground(new java.awt.Color(255, 153, 0));
        editPaymentBtn.setFont(new java.awt.Font("Helvetica Neue", 1, 14)); // NOI18N
        editPaymentBtn.setForeground(new java.awt.Color(255, 255, 255));
        editPaymentBtn.setText("Edit");
        editPaymentBtn.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        editPaymentBtn.setOpaque(true);
        editPaymentBtn.setPreferredSize(new java.awt.Dimension(72, 24));
        editPaymentBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editPaymentBtnActionPerformed(evt);
            }
        });

        deletePaymentBtn.setBackground(new java.awt.Color(204, 0, 0));
        deletePaymentBtn.setFont(new java.awt.Font("Helvetica Neue", 1, 14)); // NOI18N
        deletePaymentBtn.setForeground(new java.awt.Color(255, 255, 255));
        deletePaymentBtn.setText("Delete");
        deletePaymentBtn.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        deletePaymentBtn.setOpaque(true);
        deletePaymentBtn.setPreferredSize(new java.awt.Dimension(72, 24));
        deletePaymentBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deletePaymentBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(addPaymentBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36)
                .addComponent(editPaymentBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36)
                .addComponent(deletePaymentBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(42, Short.MAX_VALUE))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addPaymentBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(deletePaymentBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(editPaymentBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel6.setFont(new java.awt.Font("Helvetica Neue", 1, 18)); // NOI18N
        jLabel6.setText("Staff ID:");

        greetLabel.setFont(new java.awt.Font("Helvetica Neue", 2, 21)); // NOI18N
        greetLabel.setText(" ");

        employeeIdLabel.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        employeeIdLabel.setText(" ");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(38, 38, 38)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(employeeIdLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(greetLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 315, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(262, 262, 262)
                        .addComponent(jLabel10))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(619, 619, 619)
                        .addComponent(jLabel35))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(276, 276, 276)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel13)
                                .addGap(18, 18, 18)
                                .addComponent(paymentNameField, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(136, 136, 136)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(205, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(employeeIdLabel))
                        .addGap(18, 18, 18)
                        .addComponent(greetLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(35, 35, 35)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel13)
                            .addComponent(paymentNameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(56, 56, 56)
                        .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(345, 345, 345)
                        .addComponent(jLabel35))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(233, Short.MAX_VALUE))
        );

        jPanel6.setBackground(new java.awt.Color(0, 0, 0));
        jPanel6.setPreferredSize(new java.awt.Dimension(187, 67));

        jLabel8.setFont(new java.awt.Font("Helvetica Neue", 1, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Sales Amigo");

        jLabel38.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/salesamigo-logo.png"))); // NOI18N

        jButton6.setBackground(new java.awt.Color(51, 102, 255));
        jButton6.setFont(new java.awt.Font("Helvetica Neue", 1, 19)); // NOI18N
        jButton6.setForeground(new java.awt.Color(255, 255, 255));
        jButton6.setText("Logout");
        jButton6.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton6.setOpaque(true);
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton12.setBackground(new java.awt.Color(51, 102, 255));
        jButton12.setFont(new java.awt.Font("Helvetica Neue", 1, 19)); // NOI18N
        jButton12.setForeground(new java.awt.Color(255, 255, 255));
        jButton12.setText("Back to Dashboard");
        jButton12.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton12.setOpaque(true);
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(jLabel38, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(100, 100, 100)
                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jLabel38)
                        .addGap(0, 13, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton6)
                    .addComponent(jButton12))
                .addContainerGap(27, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, 1563, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 121, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 830, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void paymentNameFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_paymentNameFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_paymentNameFieldActionPerformed

    private void paymentTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_paymentTableMouseClicked
        // TODO add your handling code here:
        DefaultTableModel model = (DefaultTableModel) paymentTable.getModel();
        int selectedRowIndex = paymentTable.getSelectedRow();
        String name = (String) model.getValueAt(selectedRowIndex, 1);
        paymentNameField.setText(name);
    }//GEN-LAST:event_paymentTableMouseClicked

    private void addPaymentBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addPaymentBtnActionPerformed
        // TODO add your handling code here:
        String name = paymentNameField.getText();

        String message = String.format("Payment Update:\nName: %s\n", name);
        int option = JOptionPane.showConfirmDialog(this, message + "\nAre you sure you want add to this payment?", "Add New Payment", JOptionPane.YES_NO_OPTION);

        if (option == JOptionPane.YES_OPTION) {
            String feedback = pos.createPaymentType(name);
            updatePaymentTable();
            // Show details in another JOptionPane
            System.out.println(feedback);
            if (feedback.contains("success")) {
                JOptionPane.showMessageDialog(this, feedback, "Payment Added", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, feedback, "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_addPaymentBtnActionPerformed

    private void editPaymentBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editPaymentBtnActionPerformed
        // TODO add your handling code here:
        DefaultTableModel model = (DefaultTableModel) paymentTable.getModel();
        int selectedRow = paymentTable.getSelectedRow();
        if (selectedRow != -1) {
            int id = (int) model.getValueAt(selectedRow, 0);
            String name = paymentNameField.getText();
            String message = String.format("Payment Update:\nName: %s\n", name);
            int option = JOptionPane.showConfirmDialog(this, message + "\nAre you sure you want to update this payment with ID " + id + " ?", "Update Payment", JOptionPane.YES_NO_OPTION);

            if (option == JOptionPane.YES_OPTION) {
                String feedback = pos.updatePaymentType(id, name);  // Assuming pos is your point of sale class
                updatePaymentTable();
                // Show details in another JOptionPane
                if (feedback.contains("success")) {
                    JOptionPane.showMessageDialog(this, feedback, "Payment Updated", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, feedback, "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Please select a payment type to update.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_editPaymentBtnActionPerformed

    private void deletePaymentBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deletePaymentBtnActionPerformed
        // TODO add your handling code here:
        DefaultTableModel model = (DefaultTableModel) paymentTable.getModel();
        int selectedRow = paymentTable.getSelectedRow();
        if (selectedRow != -1) {
            int id = (int) model.getValueAt(selectedRow, 0);
            String name = (String) model.getValueAt(selectedRow, 1);
            String message = String.format("Payment Deletion:\nName: %s\n", name);
            int option = JOptionPane.showConfirmDialog(this, message + "\nAre you sure you want to delete this payment type with ID " + id + " ?", "Delete Payment", JOptionPane.YES_NO_OPTION);

            if (option == JOptionPane.YES_OPTION) {
                String feedback = pos.deletePaymentType(id);  // Assuming pos is your point of sale class
                updatePaymentTable();
                // Show details in another JOptionPane
                if (feedback.contains("success")) {
                    JOptionPane.showMessageDialog(this, feedback, "Payment Deleted", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, feedback, "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Please select a payment type to delete.", "Error", JOptionPane.ERROR_MESSAGE);
        }

    }//GEN-LAST:event_deletePaymentBtnActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        // TODO add your handling code here:
        AdminFrame frame = new AdminFrame(employee);
        frame.setVisible(true);
        dispose();
    }//GEN-LAST:event_jButton12ActionPerformed

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
            java.util.logging.Logger.getLogger(PaymentTypeFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PaymentTypeFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PaymentTypeFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PaymentTypeFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                //fix
                //new PaymentTypeFrame(user).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addPaymentBtn;
    private javax.swing.JButton deletePaymentBtn;
    private javax.swing.JButton editPaymentBtn;
    private javax.swing.JLabel employeeIdLabel;
    private javax.swing.JLabel greetLabel;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton6;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextField paymentNameField;
    private javax.swing.JTable paymentTable;
    // End of variables declaration//GEN-END:variables
}
