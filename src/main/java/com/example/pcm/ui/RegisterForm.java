package com.example.pcm.ui;

import com.example.pcm.model.Users;
import com.example.pcm.service.LoginRegisterService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.sql.SQLException;
import java.util.regex.Pattern;

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

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(240, 248, 255)); // Alice Blue
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setLayout(new BorderLayout());

        // Header
        JLabel headerLabel = new JLabel("User Registration", JLabel.CENTER);
        headerLabel.setFont(new Font("Serif", Font.BOLD, 24));
        headerLabel.setForeground(new Color(0, 102, 204)); // Medium Blue
        mainPanel.add(headerLabel, BorderLayout.NORTH);

        // Form panel
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setOpaque(false); // Make panel transparent
        formPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Adding form components
        formPanel.add(createLabel("Username:", new Color(25, 25, 112))); // Midnight Blue
        usernameField = createTextField();
        formPanel.add(usernameField);

        formPanel.add(createLabel("Password:", new Color(25, 25, 112))); // Midnight Blue
        passwordField = createPasswordField();
        formPanel.add(passwordField);

        formPanel.add(createLabel("Email:", new Color(25, 25, 112))); // Midnight Blue
        emailField = createTextField();
        formPanel.add(emailField);

        formPanel.add(createLabel("Role:", new Color(25, 25, 112))); // Midnight Blue
        roleComboBox = new JComboBox<>(new String[]{"User", "Admin"});
        formPanel.add(roleComboBox);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        buttonPanel.setOpaque(false); // Make button panel transparent

        // Register Button
        registerButton = new JButton("Register");
        customizeButton(registerButton, new Color(60, 179, 113)); // Medium Sea Green
        registerButton.addActionListener(e -> registerUser());
        buttonPanel.add(registerButton);

        // Login Button
        loginButton = new JButton("Login");
        customizeButton(loginButton, new Color(70, 130, 180)); // Steel Blue
        loginButton.addActionListener(e -> switchToLogin());
        buttonPanel.add(loginButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JLabel createLabel(String text, Color color) {
        JLabel label = new JLabel(text);
        label.setForeground(color);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        return label;
    }

    private JTextField createTextField() {
        JTextField textField = new JTextField();
        textField.setBorder(new LineBorder(new Color(25, 25, 112), 1, true)); // Midnight Blue border
        textField.setFont(new Font("Arial", Font.PLAIN, 14));
        return textField;
    }

    private JPasswordField createPasswordField() {
        JPasswordField passwordField = new JPasswordField();
        passwordField.setBorder(new LineBorder(new Color(25, 25, 112), 1, true)); // Midnight Blue border
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        return passwordField;
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

    private void registerUser() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String email = emailField.getText();
        String role = (String) roleComboBox.getSelectedItem();

        if (!isValidUsername(username)) {
            JOptionPane.showMessageDialog(this, "Invalid username. Please enter a valid username (non-empty, alphanumeric).", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!isValidPassword(password)) {
            JOptionPane.showMessageDialog(this, "Invalid password. Password must be at least 8 characters long, contain at least one number and one special character.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!isValidEmail(email)) {
            JOptionPane.showMessageDialog(this, "Invalid email. Please enter a valid email address.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        LoginRegisterService registerService = new LoginRegisterService();
        Users users = new Users();
        users.setUsername(username);
        users.setPassword(password);
        users.setEmail(email);
        users.setRole(role);

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

    private boolean isValidUsername(String username) {
        return username != null && !username.trim().isEmpty() && username.matches("^[a-zA-Z0-9]+$");
    }

    private boolean isValidPassword(String password) {
        if (password.length() < 8) {
            return false;
        }
        boolean hasNumber = false;
        boolean hasSpecialChar = false;
        for (char c : password.toCharArray()) {
            if (Character.isDigit(c)) {
                hasNumber = true;
            } else if (!Character.isLetterOrDigit(c)) {
                hasSpecialChar = true;
            }
        }
        return hasNumber && hasSpecialChar;
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pat = Pattern.compile(emailRegex);
        return email != null && pat.matcher(email).matches();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new RegisterForm().setVisible(true);
        });
    }
}
