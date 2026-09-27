package org.example;

import javax.swing.*;

public class OknovyVystup implements Vystup {
    @Override
    public void zobraz(String zprava) {
        JOptionPane.showMessageDialog(null, zprava);
    }
}
