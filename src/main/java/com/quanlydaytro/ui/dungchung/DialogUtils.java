package com.quanlydaytro.ui.dungchung;
import javax.swing.JOptionPane;
import java.awt.Component;
public final class DialogUtils {
 private DialogUtils() {}
 public static void thongBao(Component p,String s) { JOptionPane.showMessageDialog(p,s); }
 public static void baoLoi(Component p,String s) { JOptionPane.showMessageDialog(p,s,"Lỗi",JOptionPane.ERROR_MESSAGE); }
}
