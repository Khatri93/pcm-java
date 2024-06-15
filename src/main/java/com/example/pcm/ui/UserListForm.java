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
    private JButton deleteButton;
    private JButton updateButton;

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
        JPanel buttonPanel = new JPanel(new GridLayout(1, 4, 10, 0));
        buttonPanel.setBackground(new Color(245, 245, 245)); // WhiteSmoke color

        // Back Button
        backButton = new JButton("Back");
        backButton.setBackground(new Color(144, 238, 144)); // LightGreen color
        backButton.setForeground(Color.BLACK);
        backButton.addActionListener(e -> {
            new BookManagementForm().setVisible(true); // Open BookManagementForm
            dispose(); // Dispose current form
        });
        buttonPanel.add(backButton);

        // Logout Button
        logoutButton = new JButton("Logout");
        logoutButton.setBackground(new Color(255, 182, 193)); // LightPink color
        logoutButton.setForeground(Color.BLACK);
        logoutButton.addActionListener(e -> logout());
        buttonPanel.add(logoutButton);

        // Delete Button
        deleteButton = new JButton("Delete");
        deleteButton.setBackground(new Color(255, 99, 71)); // Tomato color
        deleteButton.setForeground(Color.BLACK);
        deleteButton.addActionListener(e -> deleteUser());
        buttonPanel.add(deleteButton);

        // Update Button
        updateButton = new JButton("Update");
        updateButton.setBackground(new Color(135, 206, 235)); // SkyBlue color
        updateButton.setForeground(Color.BLACK);
        updateButton.addActionListener(e -> {
            try {
                updateUser();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
        buttonPanel.add(updateButton);

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

    private void deleteUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow != -1) {
            int userId = (int) tableModel.getValueAt(selectedRow, 0);
            try {
                userService.deleteUser(userId);
                tableModel.removeRow(selectedRow);
                JOptionPane.showMessageDialog(this, "User deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error deleting user: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a user to delete.", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void updateUser() throws SQLException {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow != -1) {
            int userId = (int) tableModel.getValueAt(selectedRow, 0);
            String username = (String) tableModel.getValueAt(selectedRow, 1);
            String email = (String) tableModel.getValueAt(selectedRow, 2);
            String role = (String) tableModel.getValueAt(selectedRow, 3);

            String newUsername = JOptionPane.showInputDialog(this, "Enter new username:", username);
            String newEmail = JOptionPane.showInputDialog(this, "Enter new email:", email);
            String[] roles = {"Admin", "User"};
            String newRole = (String) JOptionPane.showInputDialog(this, "Select new role:", "Update Role",
                    JOptionPane.QUESTION_MESSAGE, null, roles, role);

            String password = JOptionPane.showInputDialog(this, "Enter new password:");

            Users user = userService.getUserById(userId);
            user.setUsername(newUsername);
            user.setEmail(newEmail);
            user.setRole(newRole);
            user.setPassword(password);

            if (newUsername == null && newEmail == null && newRole == null && password == null) {
                JOptionPane.showMessageDialog(this, "Please select a user to update.", "Warning", JOptionPane.WARNING_MESSAGE);
            } else {
                try {
                    userService.updateUser(user);
                    tableModel.setValueAt(newUsername, selectedRow, 1);
                    tableModel.setValueAt(newEmail, selectedRow, 2);
                    tableModel.setValueAt(newRole, selectedRow, 3);
                    JOptionPane.showMessageDialog(this, "User updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(this, "Error updating user: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
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
