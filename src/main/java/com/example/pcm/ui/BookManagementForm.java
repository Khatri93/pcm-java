package com.example.pcm.ui;

import com.example.pcm.model.Book;
import com.example.pcm.model.UserSession;
import com.example.pcm.service.BookService;
import com.example.pcm.service.LoginRegisterService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookManagementForm extends JFrame {
    private JTextField booknameField;
    private JTextField authorField;
    private JComboBox<Integer> roleComboBox;
    private JButton addButton;
    private JButton deleteButton;
    private JButton updateButton;
    private JButton listButton;
    private BookService bookService;
    private JButton logoutButton;

    public BookManagementForm() {
        if (!UserSession.getInstance().isLoggedIn()){
            throw new IllegalStateException("User is not logged in!");
        }

        setTitle("BOOK Management System");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setLayout(new GridLayout(5, 2));

        panel.add(new JLabel("Book Name:"));
        booknameField = new JTextField();
        panel.add(booknameField);

        panel.add(new JLabel("Author:"));
        authorField = new JPasswordField();
        panel.add(authorField);

        panel.add(new JLabel("Edition:"));
        roleComboBox = new JComboBox<>(new Integer[]{1, 2, 3, 4, 5, 6});
        panel.add(roleComboBox);

        addButton = new JButton("Add Book");
        addButton.addActionListener(e -> addBook());
        panel.add(addButton);

        listButton = new JButton("List Books");
        listButton.addActionListener(e -> listBook());
        panel.add(listButton);

        deleteButton = new JButton("Delete Books");
        deleteButton.addActionListener(e -> deleteBook());
        panel.add(deleteButton);

        updateButton = new JButton("Update Books");
        updateButton.addActionListener(e -> updateBook());
        panel.add(updateButton);

        add(panel);

        logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> logout());

        add(logoutButton, BorderLayout.SOUTH);
        // Center the window on the screen
        setLocationRelativeTo(null);
    }

    private void addBook() {
        bookService = new BookService();
        Book book = new Book();
        book.setName(booknameField.getText());
        book.setAuthor(authorField.getText());
        book.setEdition(String.valueOf(roleComboBox.getSelectedItem()));
        try {
            bookService.addBook(book);
            JOptionPane.showMessageDialog(this, "Book added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error adding Book: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteBook() {
        bookService = new BookService();
        int id = 1;
        try {
            bookService.deleteBook(id);
            JOptionPane.showMessageDialog(this, "Book deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error deleting Book: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void listBook() {
        bookService = new BookService();
        try {
            List<Book> book = bookService.getAllBooks();
            JOptionPane.showMessageDialog(this, "Book listed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error listing Books: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateBook() {
        bookService = new BookService();
        LoginRegisterService registerService = new LoginRegisterService();
        Book book = new Book();
        book.setName(booknameField.getText());
        book.setAuthor(authorField.getText());
        book.setEdition(String.valueOf(roleComboBox.getSelectedItem()));
        try {
            bookService.updateBook(book);
            JOptionPane.showMessageDialog(this, "Book updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error updating Book: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void logout() {
        UserSession.getInstance().logout();
        dispose();
        new LoginRegisterForm();
    }
}
