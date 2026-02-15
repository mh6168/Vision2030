package DigialID;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.client.j2se.MatrixToImageWriter;

public class Digital_ID extends javax.swing.JFrame {

    private JLabel titleLabel;   // "Digital ID" label
    private JLabel qrLabel;      // QR code label
    // Used for debugging and recording errors
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Digital_ID.class.getName());

    public Digital_ID(String userId) {
        initComponents();
        generateAndShowQR(userId);
    }

    public Digital_ID() {
        initComponents();
    }

    private void generateAndShowQR(String userId) {
        if (userId == null || userId.isEmpty()) {
            qrLabel.setText("Invalid ID for QR");
            return;
        }

        try {
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

            // Generate QR code
            int size = 300;
            BitMatrix matrix = new MultiFormatWriter().encode(
                    userId,
                    BarcodeFormat.QR_CODE,
                    size,
                    size,
                    hints
            );

            BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(matrix);
            qrLabel.setIcon(new ImageIcon(qrImage));
            qrLabel.setText("");

        } catch (Exception ex) {
            logger.log(java.util.logging.Level.SEVERE, "Failed to generate QR for: " + userId, ex);
            qrLabel.setText("Error generating QR");
        }
    }

    private void initComponents() {
        titleLabel = new JLabel("Digital ID");
        titleLabel.setFont(new java.awt.Font("Segoe UI", 1, 28));
        titleLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT); // center horizontally

        qrLabel = new JLabel();
        qrLabel.setPreferredSize(new Dimension(300, 300));
        qrLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT); // center horizontally

        // Panel with vertical layout
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(Box.createVerticalGlue());          // top spacing
        panel.add(titleLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 20))); // space between label and QR
        panel.add(qrLabel);
        panel.add(Box.createVerticalGlue());          // bottom spacing

        getContentPane().add(panel);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setSize(400, 450);   // window size
        setLocationRelativeTo(null); // center the window
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> new Digital_ID("USER_001").setVisible(true));
    }
}
