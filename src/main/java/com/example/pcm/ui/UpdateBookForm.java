package com.example.pcm.ui;

import com.example.pcm.model.Book;
import com.example.pcm.service.BookService;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class UpdateBookForm extends JFrame {
    private BookService bookService;
    private JTextField bookNameField;
    private JTextField authorField;
    private JComboBox<Integer> editionComboBox;
    private JButton updateButton;
    private int bookId;
    private ListBooksForm listBooksForm; // Reference to ListBooksForm

    public UpdateBookForm(int bookId, String bookName, String author, String edition, ListBooksForm listBooksForm) {
        this.bookId = bookId;
        this.listBooksForm = listBooksForm; // Store reference to ListBooksForm

        setTitle("Update Book");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        bookService = new BookService();

        initComponents(bookName, author, edition);
    }

    private void initComponents(String bookName, String author, String edition) {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

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

        // Update Button
        updateButton = new JButton("Update");
        updateButton.addActionListener(e -> updateBook());
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        panel.add(updateButton, gbc);

        add(panel);
    }

    private void updateBook() {
        String name = bookNameField.getText();
        String author = authorField.getText();
        int edition = (int) editionComboBox.getSelectedItem();

        if (name.isEmpty() || author.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Book book = new Book();
        book.setId(bookId);
        book.setName(name);
        book.setAuthor(author);
        book.setEdition(String.valueOf(edition));

        try {
            bookService.updateBook(book);
            JOptionPane.showMessageDialog(this, "Book updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            // After updating, refresh the data in ListBooksForm
            listBooksForm.loadBookData();
            dispose(); // Close the update form
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error updating book: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
