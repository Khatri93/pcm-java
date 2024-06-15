package com.example.pcm.ui;

import com.example.pcm.model.Users;
import com.example.pcm.model.UserSession;
import com.example.pcm.service.UserService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class UserListForm extends JFrame {
    private UserService userService;
    private JTable userTable;
    private DefaultTableModel tableModel;
    private JButton backButton;
    private JButton logoutButton;

    public UserListForm() {
        if (!UserSession.getInstance().isLoggedIn() || !UserSession.getInstance().getRole().equals("Admin")) {
            throw new IllegalStateException("Access denied! Only admins can access this screen.");
        }

        setTitle("List of Users");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        userService = new UserService();

        initComponents();
        loadUserData();
    }

    private void initComponents() {
        // Set background color of the frame
        getContentPane().setBackground(new Color(240, 248, 255)); // AliceBlue color

        // Table setup
        String[] columnNames = {"ID", "Username", "Email", "Role"};
        tableModel = new DefaultTableModel(columnNames, 0);
        userTable = new JTable(tableModel);
        userTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Customize table appearance
        userTable.getTableHeader().setBackground(new Color(70, 130, 180)); // SteelBlue color
        userTable.getTableHeader().setForeground(Color.WHITE);
        userTable.setBackground(new Color(255, 250, 240)); // FloralWhite color
        userTable.setForeground(Color.BLACK);
        userTable.setFont(new Font("Arial", Font.PLAIN, 14));
        userTable.setRowHeight(30);

        // Scroll pane for the table
        JScrollPane scrollPane = new JScrollPane(userTable);
        add(scrollPane, BorderLayout.CENTER);

        // Button panel setup
        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.setBackground(new Color(245, 245, 245)); // WhiteSmoke color

        // Back Button
        backButton = new JButton("Back");
        backButton.setBackground(new Color(144, 238, 144)); // LightGreen color
        backButton.setForeground(Color.BLACK);
        backButton.addActionListener(e -> {
            new BookManagementForm().setVisible(true); // Open BookManagementForm
            dispose(); // Dispose current form
        });
        buttonPanel.add(backButton, BorderLayout.WEST);

        // Logout Button
        logoutButton = new JButton("Logout");
        logoutButton.setBackground(new Color(255, 182, 193)); // LightPink color
        logoutButton.setForeground(Color.BLACK);
        logoutButton.addActionListener(e -> logout());
        buttonPanel.add(logoutButton, BorderLayout.EAST);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadUserData() {
        try {
            List<Users> users = userService.getAllUsers();
            tableModel.setRowCount(0); // Clear existing data
            for (Users user : users) {
                Object[] rowData = {
                        user.getId(),
                        user.getUsername(),
                        user.getEmail(),
                        user.getRole()
                };
                tableModel.addRow(rowData);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading users: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void logout() {
        UserSession.getInstance().logout();
        dispose();
        new LoginForm().setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            UserListForm form = new UserListForm();
            form.setVisible(true);
        });
    }
}
