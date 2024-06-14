package com.example.pcm.ui;

import com.example.pcm.model.UserSession;
import com.example.pcm.service.LoginRegisterService;

import javax.swing.*;
import java.awt.*;

public class LoginForm extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;

    public LoginForm() {
        setTitle("Login Form");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2));

        panel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        panel.add(usernameField);

        panel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        panel.add(passwordField);

        loginButton = new JButton("Login");
        loginButton.addActionListener(e -> loginUser());
        panel.add(loginButton);

        registerButton = new JButton("Register");
        registerButton.addActionListener(e -> switchToRegister());
        panel.add(registerButton);

        add(panel);
    }

    private void loginUser() {
        LoginRegisterService registerService = new LoginRegisterService();
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (registerService.loginUser(username, password)) {
            UserSession.getInstance().login(username);
            JOptionPane.showMessageDialog(this, "Login successful!");
            SwingUtilities.invokeLater(() -> {
                new BookManagementForm().setVisible(true);
            });
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid username or password.");
        }
    }

    private void switchToRegister() {
        SwingUtilities.invokeLater(() -> {
            new RegisterForm().setVisible(true);
        });
        dispose();
    }
}
