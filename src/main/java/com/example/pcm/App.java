package com.example.pcm;

import com.example.pcm.ui.LoginForm;

import javax.swing.*;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginForm form = new LoginForm();
            form.setVisible(true);
        });
    }
}
