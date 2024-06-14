package com.example.pcm.ui;

import com.example.pcm.model.Users;
import com.example.pcm.service.LoginRegisterService;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class RegisterForm extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField emailField;
    private JComboBox<String> roleComboBox;
    private JButton registerButton;
    private JButton loginButton;

    public RegisterForm() {
        setTitle("Register Form");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 2));

        panel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        panel.add(usernameField);

        panel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        panel.add(passwordField);

        panel.add(new JLabel("Email:"));
        emailField = new JTextField();
        panel.add(emailField);

        panel.add(new JLabel("Role:"));
        roleComboBox = new JComboBox<>(new String[]{"User", "Admin"});
        panel.add(roleComboBox);

        registerButton = new JButton("Register");
        registerButton.addActionListener(e -> registerUser());
        panel.add(registerButton);

        loginButton = new JButton("Login");
        loginButton.addActionListener(e -> switchToLogin());
        panel.add(loginButton);

        add(panel);
    }

    private void registerUser() {
        LoginRegisterService registerService = new LoginRegisterService();
        Users users = new Users();
        users.setUsername(usernameField.getText());
        users.setPassword(new String(passwordField.getPassword()));
        users.setEmail(emailField.getText());
        users.setRole((String) roleComboBox.getSelectedItem());
        try {
            registerService.registerUser(users);
            JOptionPane.showMessageDialog(this, "User registered successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error registering user: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void switchToLogin() {
        SwingUtilities.invokeLater(() -> {
            new LoginForm().setVisible(true);
        });
        dispose();
    }
}
