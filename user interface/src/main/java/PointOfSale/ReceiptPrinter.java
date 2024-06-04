/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PointOfSale;

/**
 *
 * @author britt
 */

import java.awt.*;
import java.awt.print.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

public class ReceiptPrinter {

    public void printReceipt(Transaction transaction) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            DecimalFormat decimalFormat = new DecimalFormat("#.##");

            // Receipt content
            StringBuilder content = new StringBuilder();
            content.append("Receipt\n");
            content.append("----------------------------------\n");
            content.append("Transaction ID: ").append(transaction.getTransactionId()).append("\n");
            content.append("Date: ").append(dateFormat.format(transaction.getTransactionDate())).append("\n");
            content.append("Employee ID: ").append(transaction.getEmployeeId()).append("\n");
            content.append("Customer ID: ").append(transaction.getCustomerId()).append("\n");
            content.append("Change: $").append(decimalFormat.format(transaction.getChange())).append("\n");
            content.append("Sub Total: $").append(decimalFormat.format(transaction.getTotal())).append("\n");
            content.append("Tax: $").append(decimalFormat.format(transaction.getTotal() * 0.15)).append("\n");
            content.append("Total: $").append(decimalFormat.format(transaction.getTotal() * 1.15)).append("\n");
            content.append("Payment Method: ").append(transaction.getPaymentMethod()).append("\n");
            content.append("----------------------------------\n");
            content.append("Items:\n");

            for (TransactionItem item : transaction.getItems()) {
                content.append("Product ID: ").append(item.getProductId())
                       .append(", Quantity: ").append(item.getQuantity())
                       .append(", Price: $").append(decimalFormat.format(item.getPrice())).append("\n");
            }
            content.append("----------------------------------\n");
            content.append("Thank you for your purchase!");

            // Create a PrinterJob
            PrinterJob printerJob = PrinterJob.getPrinterJob();

            // Set page format with custom paper size and margins
            PageFormat pageFormat = printerJob.defaultPage();
            Paper paper = new Paper();
            
            // Set paper size to 3 inches (width) x 11.69 inches (height)
            double paperWidth = 3 * 72.0; // 1 inch = 72 points
            double paperHeight = 11.69 * 72.0;
            paper.setSize(paperWidth, paperHeight);
            
            pageFormat.setPaper(paper);

            // Set printable with custom implementation
            printerJob.setPrintable(new Printable() {
                @Override
                public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
                    if (pageIndex > 0) {
                        return Printable.NO_SUCH_PAGE;
                    }

                    Graphics2D g2d = (Graphics2D) graphics;
                    g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

                    // Font for printing
                    Font font = new Font("Monospaced", Font.PLAIN, 12);
                    g2d.setFont(font);

                    // Print content
                    String[] lines = content.toString().split("\n");
                    int y = 50; // Initial Y position

                    for (String line : lines) {
                        g2d.drawString(line, 50, y);
                        y += 15; // Increment Y position for next line
                        if (y > pageFormat.getImageableHeight() - 50) {
                            // End of page, break out of the loop
                            break;
                        }
                    }

                    return Printable.PAGE_EXISTS;
                }
            }, pageFormat);

            // Show print dialog
            if (printerJob.printDialog()) {
                try {
                    printerJob.print();
                } catch (PrinterException ex) {
                    ex.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
