package com.example.pcm.ui;

import com.example.pcm.model.UserSession;
import com.example.pcm.model.Users;
import com.example.pcm.service.LoginRegisterService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
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

        // Main panel setup
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(230, 230, 250)); // Lavender
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setLayout(new BorderLayout());

        // Header label
        JLabel headerLabel = new JLabel("User Login", JLabel.CENTER);
        headerLabel.setFont(new Font("Serif", Font.BOLD, 24));
        headerLabel.setForeground(new Color(75, 0, 130)); // Indigo
        mainPanel.add(headerLabel, BorderLayout.NORTH);

        // Form panel setup
        JPanel formPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        formPanel.setOpaque(false); // Make form panel transparent

        // Username field setup
        formPanel.add(createLabel("Username:", new Color(72, 61, 139))); // Dark Slate Blue
        usernameField = new JTextField();
        formPanel.add(usernameField);

        // Password field setup
        formPanel.add(createLabel("Password:", new Color(72, 61, 139))); // Dark Slate Blue
        passwordField = new JPasswordField();
        formPanel.add(passwordField);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Button panel setup
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        buttonPanel.setOpaque(false); // Make button panel transparent

        // Login button setup
        loginButton = new JButton("Login");
        customizeButton(loginButton, new Color(60, 179, 113)); // Medium Sea Green
        loginButton.addActionListener(e -> loginUser());
        buttonPanel.add(loginButton);

        // Register button setup
        registerButton = new JButton("Register");
        customizeButton(registerButton, new Color(30, 144, 255)); // Dodger Blue
        registerButton.addActionListener(e -> switchToRegister());
        buttonPanel.add(registerButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JLabel createLabel(String text, Color color) {
        JLabel label = new JLabel(text);
        label.setForeground(color);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        return label;
    }

    private void customizeButton(JButton button, Color color) {
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(color.darker(), 2),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
    }

    private void loginUser() {
        LoginRegisterService registerService = new LoginRegisterService();
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        Users user = registerService.loginUser(username, password);
        if (user != null) {
            UserSession.getInstance().login(user.getUsername(), user.getRole());
            JOptionPane.showMessageDialog(this, "Login successful!");
            SwingUtilities.invokeLater(() -> {
                new BookManagementForm().setVisible(true);
            });
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid username or password.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void switchToRegister() {
        SwingUtilities.invokeLater(() -> {
            new RegisterForm().setVisible(true);
        });
        dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginForm().setVisible(true);
        });
    }
}
