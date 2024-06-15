package com.example.pcm.ui;

import com.example.pcm.model.Book;
import com.example.pcm.model.UserSession;
import com.example.pcm.service.BookService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.sql.SQLException;

public class BookManagementForm extends JFrame {
    private JTextField booknameField;
    private JTextField authorField;
    private JComboBox<Integer> editionComboBox;
    private JButton addButton;
    private JButton listButton;
    private JButton logoutButton;
    private JButton userListButton;
    private BookService bookService;

    public BookManagementForm() {
        if (!UserSession.getInstance().isLoggedIn()) {
            throw new IllegalStateException("User is not logged in!");
        }

        setTitle("Book Management System");
        setSize(700, 500); // Increase frame size for better accommodation
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false); // Prevent resizing to keep layout consistent

        bookService = new BookService();
        initComponents();
        pack(); // Adjust frame size based on contents
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(245, 245, 245)); // Light Gray background
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20)); // Padding around the panel
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Title
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; // Span across two columns
        JLabel headerLabel = new JLabel("Book Management System", JLabel.CENTER);
        headerLabel.setFont(new Font("Serif", Font.BOLD, 24));
        headerLabel.setForeground(new Color(0, 102, 204)); // Medium Blue
        mainPanel.add(headerLabel, gbc);

        // Book Name Field
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        mainPanel.add(createLabel("Book Name:", new Color(25, 25, 112)), gbc); // Midnight Blue
        booknameField = createTextField();
        gbc.gridx = 1;
        mainPanel.add(booknameField, gbc);

        // Author Field
        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(createLabel("Author:", new Color(25, 25, 112)), gbc); // Midnight Blue
        authorField = createTextField();
        gbc.gridx = 1;
        mainPanel.add(authorField, gbc);

        // Edition ComboBox
        gbc.gridx = 0;
        gbc.gridy = 3;
        mainPanel.add(createLabel("Edition:", new Color(25, 25, 112)), gbc); // Midnight Blue
        editionComboBox = new JComboBox<>(new Integer[]{1, 2, 3, 4, 5, 6});
        gbc.gridx = 1;
        mainPanel.add(editionComboBox, gbc);

        // Add Book Button
        addButton = createButton("Add Book", new Color(60, 179, 113)); // Medium Sea Green
        addButton.addActionListener(e -> addBook());
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        mainPanel.add(addButton, gbc);

        // List Books Button
        listButton = createButton("List Books", new Color(70, 130, 180)); // Steel Blue
        listButton.addActionListener(e -> listBooks());
        gbc.gridy = 5;
        mainPanel.add(listButton, gbc);

        // Conditional User List Button for Admins
        if ("Admin".equals(UserSession.getInstance().getRole())) {
            userListButton = createButton("User List", new Color(0, 149, 255)); // Red-Orange for Admin access
            userListButton.addActionListener(e -> showUserList());
            gbc.gridy = 6;
            mainPanel.add(userListButton, gbc);
        }

        // Back Button
        JButton backButton = createButton("Back", new Color(123, 104, 238)); // Medium Slate Blue
        backButton.addActionListener(e -> {
            new ListBooksForm().setVisible(true);
            dispose();
        });
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(backButton, gbc);

        // Logout Button
        logoutButton = createButton("Logout", new Color(255, 69, 0)); // Red
        logoutButton.addActionListener(e -> logout());
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(logoutButton, gbc);

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

    private JButton createButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(color.darker(), 2),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        button.setPreferredSize(new Dimension(150, 30)); // Preferred size to ensure visibility
        return button;
    }

    private void addBook() {
        Book book = new Book();
        boolean fieldsValid = fetchBookFields(book);
        if (!fieldsValid) {
            return; // Stop the process if validation fails
        }

        try {
            bookService.addBook(book);
            JOptionPane.showMessageDialog(this, "Book added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            clearFields();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error adding book: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean fetchBookFields(Book book) {
        String name = booknameField.getText();
        String author = authorField.getText();
        Object selectedEditionObject = editionComboBox.getSelectedItem();

        if (selectedEditionObject == null) {
            JOptionPane.showMessageDialog(this, "Please select an edition.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        int edition = (int) selectedEditionObject;

        if (name.isEmpty() || author.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        book.setName(name);
        book.setAuthor(author);
        book.setEdition(String.valueOf(edition));
        return true;
    }

    private void listBooks() {
        new ListBooksForm().setVisible(true);
        dispose();
    }

    private void showUserList() {
        new UserListForm().setVisible(true);
        dispose();
    }

    private void logout() {
        UserSession.getInstance().logout();
        new LoginForm().setVisible(true);
        dispose();
    }

    private void clearFields() {
        booknameField.setText("");
        authorField.setText("");
        editionComboBox.setSelectedIndex(0);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            BookManagementForm form = new BookManagementForm();
            form.setVisible(true);
        });
    }
}
