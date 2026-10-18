package com.quanlydaytro;

import com.quanlydaytro.ui.DangNhapFrame;
import com.quanlydaytro.ui.dungchung.DialogUtils;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.formdev.flatlaf.FlatLightLaf");
        } catch (Exception e) {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        // Áp dụng theme hiện đại (font, bo góc, màu nhấn, scrollbar, tab...)
        DialogUtils.installTheme();

        SwingUtilities.invokeLater(() -> {
            DangNhapFrame frame = new DangNhapFrame();
            frame.setVisible(true);
        });
    }
}
