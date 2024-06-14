package com.example.pcm.ui;

import com.example.pcm.model.Book;
import com.example.pcm.model.UserSession;
import com.example.pcm.service.BookService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class ListBooksForm extends JFrame {
    private BookService bookService;
    private JTable bookTable;
    private DefaultTableModel tableModel;
    private JButton deleteButton;
    private JButton logoutButton;
    private JButton updateButton;
    private JButton refreshButton; // New refresh button

    public ListBooksForm() {
        setTitle("List of Books");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        bookService = new BookService();

        initComponents();
        loadBookData();
    }

    private void initComponents() {
        String[] columnNames = {"ID", "Name", "Author", "Edition"};
        tableModel = new DefaultTableModel(columnNames, 0);
        bookTable = new JTable(tableModel);
        bookTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        bookTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                bookTableMouseClicked(evt);
            }
        });

        JScrollPane scrollPane = new JScrollPane(bookTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new BorderLayout());

        // Refresh Button
        refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> loadBookData());
        buttonPanel.add(refreshButton, BorderLayout.NORTH); // Place refresh button at the top

        deleteButton = new JButton("Delete Selected Book");
        deleteButton.addActionListener(e -> deleteSelectedBook());
        buttonPanel.add(deleteButton, BorderLayout.WEST);

        // Update Button
        updateButton = new JButton("Update Book");
        updateButton.addActionListener(e -> updateSelectedBook());
        buttonPanel.add(updateButton, BorderLayout.CENTER);

        // Logout Button
        logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> logout());
        buttonPanel.add(logoutButton, BorderLayout.EAST);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            new BookManagementForm().setVisible(true); // Open BookManagementForm
            dispose(); // Dispose current form
        });
        buttonPanel.add(backButton, BorderLayout.SOUTH);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    void loadBookData() {
        try {
            List<Book> books = bookService.getAllBooks();
            tableModel.setRowCount(0); // Clear existing data
            for (Book book : books) {
                Object[] rowData = {
                        book.getId(),
                        book.getName(),
                        book.getAuthor(),
                        book.getEdition()
                };
                tableModel.addRow(rowData);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading books: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteSelectedBook() {
        int selectedRow = bookTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a book to delete.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int bookId = (int) tableModel.getValueAt(selectedRow, 0);
        try {
            bookService.deleteBook(bookId);
            JOptionPane.showMessageDialog(this, "Book deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            loadBookData(); // Refresh the table data
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error deleting book: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateSelectedBook() {
        int selectedRow = bookTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a book to update.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Get book details from the selected row
        int bookId = (int) tableModel.getValueAt(selectedRow, 0);
        String bookName = (String) tableModel.getValueAt(selectedRow, 1);
        String author = (String) tableModel.getValueAt(selectedRow, 2);
        String edition = (String) tableModel.getValueAt(selectedRow, 3);

        // Open update form with selected book data
        UpdateBookForm updateBookForm = new UpdateBookForm(bookId, bookName, author, edition, new ListBooksForm());
        updateBookForm.setVisible(true);
    }

    private void bookTableMouseClicked(java.awt.event.MouseEvent evt) {
        // Handle events on book table (if needed)
    }

    private void logout() {
        UserSession.getInstance().logout();
        dispose();
        new LoginForm().setVisible(true);
    }
}
