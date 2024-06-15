package com.example.pcm.ui;

import com.example.pcm.model.Book;
import com.example.pcm.service.BookService;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class UpdateBookForm extends JFrame {
    private BookService bookService;
    private ListBooksForm listBooksForm; // Reference to ListBooksForm
    private JTextField bookNameField;
    private JTextField authorField;
    private JComboBox<Integer> editionComboBox;
    private JTextField totalField;
    private JTextField availableField;
    private JButton updateButton;
    private int bookId;

    public UpdateBookForm(int bookId, String bookName, String author, String edition, int total, int available, ListBooksForm listBooksForm) {
        this.bookId = bookId;
        this.listBooksForm = listBooksForm; // Store reference to ListBooksForm

        setTitle("Update Book");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        bookService = new BookService();

        initComponents(bookName, author, edition, total, available);
    }

    private void initComponents(String bookName, String author, String edition, int total, int available) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(240, 248, 255)); // Light blue background
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Book Name Field
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Book Name:"), gbc);
        bookNameField = new JTextField(20);
        bookNameField.setText(bookName); // Set initial value
        gbc.gridx = 1;
        panel.add(bookNameField, gbc);

        // Author Field
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Author:"), gbc);
        authorField = new JTextField(20);
        authorField.setText(author); // Set initial value
        gbc.gridx = 1;
        panel.add(authorField, gbc);

        // Edition ComboBox
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Edition:"), gbc);
        editionComboBox = new JComboBox<>(new Integer[]{1, 2, 3, 4, 5, 6});
        editionComboBox.setSelectedItem(Integer.parseInt(edition)); // Set initial value
        gbc.gridx = 1;
        panel.add(editionComboBox, gbc);

        // Total Copies Field
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Total Copies:"), gbc);
        totalField = new JTextField(20);
        totalField.setText(String.valueOf(total)); // Set initial value
        gbc.gridx = 1;
        panel.add(totalField, gbc);

        // Available Copies Field
        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(new JLabel("Available Copies:"), gbc);
        availableField = new JTextField(20);
        availableField.setText(String.valueOf(available)); // Set initial value
        gbc.gridx = 1;
        panel.add(availableField, gbc);

        // Update Button
        updateButton = new JButton("Update");
        updateButton.setBackground(new Color(70, 130, 180)); // Steel blue background
        updateButton.setForeground(Color.WHITE); // White text color
        updateButton.addActionListener(e -> {
            try {
                updateBook(bookId);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error updating book: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 0, 0, 0); // Larger top margin
        panel.add(updateButton, gbc);

        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Add padding to the panel

        add(panel);
    }

    private void updateBook(int id) throws SQLException {
        Book book = bookService.getBookById(id);
        if (book == null) {
            JOptionPane.showMessageDialog(this, "Book does not exist.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String author = authorField.getText();
        String name = bookNameField.getText();
        Object selectedEditionObject = editionComboBox.getSelectedItem();
        int total;
        int available;

        try {
            total = Integer.parseInt(totalField.getText());
            available = Integer.parseInt(availableField.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Total and Available must be numeric values.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (selectedEditionObject != null) {
            int edition = (int) selectedEditionObject;
            book.setEdition(String.valueOf(edition));
        }

        if (!name.isEmpty()) {
            book.setName(name);
        }

        if (!author.isEmpty()) {
            book.setAuthor(author);
        }

        book.setTotal(total);
        book.setAvailable(available);

        bookService.updateBook(book);
        JOptionPane.showMessageDialog(this, "Book updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

        // Refresh the data in ListBooksForm
        listBooksForm.loadBookData();

        dispose(); // Close the update form
    }
}
