package com.quanlydaytro;

import com.quanlydaytro.ui.DangNhapFrame;
import com.quanlydaytro.ui.dungchung.DialogUtils;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
            }
            DialogUtils.installTheme();
            new DangNhapFrame().setVisible(true);
        });
    }
}
