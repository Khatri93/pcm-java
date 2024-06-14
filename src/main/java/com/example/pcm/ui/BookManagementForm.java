package com.example.pcm.ui;

import com.example.pcm.model.Book;
import com.example.pcm.model.UserSession;
import com.example.pcm.service.BookService;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class BookManagementForm extends JFrame {
    private JTextField booknameField;
    private JTextField authorField;
    private JComboBox<Integer> editionComboBox;
    private JButton addButton;
    private JButton deleteButton;
    private JButton updateButton;
    private JButton listButton;
    private JButton logoutButton;
    private BookService bookService;

    public BookManagementForm() {
        if (!UserSession.getInstance().isLoggedIn()) {
            throw new IllegalStateException("User is not logged in!");
        }

        setTitle("Book Management System");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
        bookService = new BookService();
    }

    private void initComponents() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Book Name Field
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Book Name:"), gbc);
        booknameField = new JTextField(20);
        gbc.gridx = 1;
        panel.add(booknameField, gbc);

        // Author Field
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Author:"), gbc);
        authorField = new JTextField(20);
        gbc.gridx = 1;
        panel.add(authorField, gbc);

        // Edition ComboBox
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Edition:"), gbc);
        editionComboBox = new JComboBox<>(new Integer[]{1, 2, 3, 4, 5, 6});
        gbc.gridx = 1;
        panel.add(editionComboBox, gbc);

        // Add Book Button
        addButton = new JButton("Add Book");
        addButton.addActionListener(e -> addBook());
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        panel.add(addButton, gbc);

        // Update Book Button
        updateButton = new JButton("Update Book");
        updateButton.addActionListener(e -> updateBook());
        gbc.gridy = 4;
        panel.add(updateButton, gbc);

        // Delete Book Button
        deleteButton = new JButton("Delete Book");
        deleteButton.addActionListener(e -> deleteBook());
        gbc.gridy = 5;
        panel.add(deleteButton, gbc);

        // List Books Button
        listButton = new JButton("List Books");
        listButton.addActionListener(e -> listBooks());
        gbc.gridy = 6;
        panel.add(listButton, gbc);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            new ListBooksForm().setVisible(true); // Open ListBooksForm
            dispose(); // Dispose current form
        });
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.anchor = GridBagConstraints.NORTHWEST; // Align to the northwest
        panel.add(backButton, gbc);

        add(panel);

        // Logout Button
        logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> logout());
        gbc.gridx = 1;
        gbc.gridy = 7;
        gbc.anchor = GridBagConstraints.NORTHEAST;
        panel.add(logoutButton, gbc);
    }

    private void addBook() {
        Book book = new Book();
        fetchBookFields(book);

        try {
            bookService.addBook(book);
            JOptionPane.showMessageDialog(this, "Book added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            clearFields();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error adding book: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateBook() {
        Book book = new Book();
        fetchBookFields(book);

        try {
//            int id = bookService.
            bookService.updateBook(book);
            JOptionPane.showMessageDialog(this, "Book updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            clearFields();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error updating book: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void fetchBookFields(Book book){
        String name = booknameField.getText();
        String author = authorField.getText();

        Object selectedEditionObject = editionComboBox.getSelectedItem();

        if (selectedEditionObject == null) {
            // Handle the case where no edition is selected
            JOptionPane.showMessageDialog(this, "Please select an edition.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int edition = (int) selectedEditionObject;

        if (name.isEmpty() || author.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        book.setName(name);
        book.setAuthor(author);
        book.setEdition(String.valueOf(edition));
    }

    private void deleteBook() {
        String name = booknameField.getText();

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter the book name to delete.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            bookService.deleteBookByName(name);
            JOptionPane.showMessageDialog(this, "Book deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            clearFields();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error deleting book: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void listBooks() {
        new ListBooksForm().setVisible(true);
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
}
