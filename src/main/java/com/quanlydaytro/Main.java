package com.quanlydaytro;
import com.quanlydaytro.ui.DangNhapFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
public class Main {
 public static void main(String[] args) {
  SwingUtilities.invokeLater(() -> {
   try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception ignored) {}
   new DangNhapFrame().setVisible(true);
  });
 }
}
