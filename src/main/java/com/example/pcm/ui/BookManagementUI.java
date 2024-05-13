package com.example.pcm.ui;

import com.example.pcm.model.Book;
import com.example.pcm.service.BookService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

public class BookManagementUI extends JFrame implements ActionListener {

    private JTextField nameField;
    private JTextField authorField;
    private JTextField editionField;
    private JButton addButton;
    private JButton deleteButton;
    private JButton updateButton;
    private JButton refreshButton;
    private JTextArea bookListArea;

    public BookManagementUI() {
        super("Book Management System");
        initUI();
        refreshBookList();
    }

    private void initUI() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        // Labels
        JLabel nameLabel = new JLabel("Book Name:");
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 0;
        constraints.gridy = 0;
        mainPanel.add(nameLabel, constraints);

        JLabel authorLabel = new JLabel("Author:");
        constraints.gridy = 1;
        mainPanel.add(authorLabel, constraints);

        JLabel editionLabel = new JLabel("Edition:");
        constraints.gridy = 2;
        mainPanel.add(editionLabel, constraints);

        // Text Fields
        nameField = new JTextField(20);
        constraints.gridx = 1;
        constraints.gridy = 0;
        mainPanel.add(nameField, constraints);

        authorField = new JTextField(20);
        constraints.gridy = 1;
        mainPanel.add(authorField, constraints);

        editionField = new JTextField(20);
        constraints.gridy = 2;
        mainPanel.add(editionField, constraints);

        // Buttons
        addButton = new JButton("Add Book");
        addButton.addActionListener(this);
        constraints.gridx = 0;
        constraints.gridy = 3;
        mainPanel.add(addButton, constraints);

        deleteButton = new JButton("Delete Book");
        deleteButton.addActionListener(this);
        constraints.gridx = 1;
        constraints.gridy = 3;
        mainPanel.add(deleteButton, constraints);

        updateButton = new JButton("Update Book");
        updateButton.addActionListener(this);
        constraints.gridx = 0;
        constraints.gridy = 4;
        mainPanel.add(updateButton, constraints);

        refreshButton = new JButton("Refresh List");
        refreshButton.addActionListener(this);
        constraints.gridx = 1;
        constraints.gridy = 4;
        mainPanel.add(refreshButton, constraints);

        // Book List Area
        bookListArea = new JTextArea(10, 30);
        bookListArea.setEditable(false);
        constraints.gridx = 0;
        constraints.gridy = 5;
        constraints.gridwidth = 2;
        mainPanel.add(bookListArea, constraints);

        JScrollPane scrollPane = new JScrollPane(bookListArea);
        mainPanel.add(scrollPane, constraints);

        // Set overall layout
        this.add(mainPanel);
        this.pack();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    private void refreshBookList() {
        try {
            List<Book> books = BookService.getAllBooks();
            StringBuilder bookList = new StringBuilder();
            for (Book book : books) {
                bookList.append(String.format("ID: %d, Name: %s, Author: %s, Edition: %s\n", book.getId(), book.getName(), book.getAuthor(), book.getEdition()));
            }
            bookListArea.setText(bookList.toString());
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error retrieving books!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addButton) {
            String name = nameField.getText();
            String author = authorField.getText();
            String edition = editionField.getText();

            try {
                Book book = new Book();
                book.setName(name);
                book.setAuthor(author);
                book.setEdition(edition);
                BookService.addBook(book);
                JOptionPane.showMessageDialog(this, "Book added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                refreshBookList();
                clearFields();
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error adding book!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == deleteButton) {
            int selectedRow = bookListArea.getSelectionStart();
            if (selectedRow >= 0) { // Check if a book is selected
                int id = Integer.parseInt(bookListArea.getText().substring(0, bookListArea.getText().indexOf(","))); // Extract ID from selected line
                try {
                    BookService.deleteBook(id);
                    JOptionPane.showMessageDialog(this, "Book deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    refreshBookList();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Error deleting book!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a book to delete!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == updateButton) {
            int selectedRow = bookListArea.getSelectionStart();
            if (selectedRow >= 0) { // Check if a book is selected
                int id = Integer.parseInt(bookListArea.getText().substring(0, bookListArea.getText().indexOf(","))); // Extract ID from selected line
                String name = nameField.getText();
                String author = authorField.getText();
                String edition = editionField.getText();

                try {
                    Book book = new Book();
                    book.setId(id);
                    book.setName(name);
                    book.setAuthor(author);
                    book.setEdition(edition);
                    BookService.updateBook(book);
                    JOptionPane.showMessageDialog(this, "Book updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    refreshBookList();
                    clearFields();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Error updating book!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a book to update!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == refreshButton) {
            refreshBookList();
        }
    }

    private void clearFields() {
        nameField.setText("");
        authorField.setText("");
        editionField.setText("");
    }

    public static void main(String[] args) {
        new BookManagementUI();
    }
}


