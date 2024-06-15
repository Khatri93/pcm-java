package com.example.pcm.ui;

import com.example.pcm.model.Book;
import com.example.pcm.model.UserSession;
import com.example.pcm.service.BookService;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class ListBooksForm extends JFrame {
    private BookService bookService;
    private JTable bookTable;
    private DefaultTableModel tableModel;
    private JButton deleteButton;
    private JButton updateButton;
    private JButton refreshButton;
    private JButton logoutButton;
    private JButton borrowButton;
    private JButton returnButton;

    public ListBooksForm() {
        setTitle("List of Books");
        setSize(800, 600); // Increased size for better layout
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true); // Allow resizing
        setBackground(new Color(240, 248, 255)); // Alice Blue background

        bookService = new BookService();

        initComponents();
        loadBookData();
    }

    private void initComponents() {
        String[] columnNames = {"ID", "Name", "Author", "Edition", "Total", "Available"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Disable cell editing
            }
        };

        bookTable = new JTable(tableModel);
        bookTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        bookTable.setFont(new Font("Arial", Font.PLAIN, 14)); // Font for table cells
        bookTable.setRowHeight(30); // Increase row height
        bookTable.setGridColor(new Color(211, 211, 211)); // Light grey grid lines
        bookTable.setShowVerticalLines(false); // Hide vertical lines for cleaner look
        bookTable.setIntercellSpacing(new Dimension(0, 1)); // Adjust spacing between cells

        // Alternate row colors for better readability
        bookTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                           boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (row % 2 == 0) {
                    c.setBackground(new Color(224, 255, 255)); // Light Cyan for even rows
                } else {
                    c.setBackground(Color.WHITE); // White for odd rows
                }
                return c;
            }
        });

        // Table header customization
        JTableHeader header = bookTable.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 16));
        header.setForeground(Color.WHITE);
        header.setBackground(new Color(0, 120, 215)); // Blue header background
        header.setPreferredSize(new Dimension(header.getWidth(), 40)); // Increase header height

        JScrollPane scrollPane = new JScrollPane(bookTable);
        add(scrollPane, BorderLayout.CENTER);

        // Button panel customization
        JPanel buttonPanel = new JPanel(new GridLayout(1, 7, 10, 10));
        buttonPanel.setBackground(new Color(245, 245, 245)); // Light grey panel background
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Padding around the panel

        // Refresh Button
        refreshButton = createButton("Refresh", new Color(60, 179, 113)); // Medium Sea Green
        refreshButton.addActionListener(e -> loadBookData());
        buttonPanel.add(refreshButton);

        // Delete Button
        deleteButton = createButton("Delete Selected Book", new Color(255, 174, 0)); // Red
        deleteButton.addActionListener(e -> deleteSelectedBook());
        buttonPanel.add(deleteButton);

        // Update Button
        updateButton = createButton("Update Book", new Color(70, 130, 180)); // Steel Blue
        updateButton.addActionListener(e -> updateSelectedBook());
        buttonPanel.add(updateButton);

        // Borrow Button
        borrowButton = createButton("Borrow Book", new Color(123, 104, 238)); // Medium Slate Blue
        borrowButton.addActionListener(e -> borrowSelectedBook());
        buttonPanel.add(borrowButton);

        // Return Button
        returnButton = createButton("Return Book", new Color(238, 130, 238)); // Violet
        returnButton.addActionListener(e -> returnSelectedBook());
        buttonPanel.add(returnButton);

        // Back Button
        JButton backButton = createButton("Back", new Color(255, 165, 0)); // Orange
        backButton.addActionListener(e -> {
            new BookManagementForm().setVisible(true);
            dispose();
        });
        buttonPanel.add(backButton);

        // Logout Button
        logoutButton = createButton("Logout", new Color(255, 0, 0)); // Red
        logoutButton.addActionListener(e -> logout());
        buttonPanel.add(logoutButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private JButton createButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(color.darker(), 2),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        button.setPreferredSize(new Dimension(150, 40)); // Preferred size to ensure visibility

        // Adding hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(color.brighter());
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(color);
            }
        });

        return button;
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
                        book.getEdition(),
                        book.getTotal(),
                        book.getAvailable()
                };
                tableModel.addRow(rowData);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading books: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteSelectedBook() {
        if (!"Admin".equals(UserSession.getInstance().getRole())) {
            JOptionPane.showMessageDialog(this, "Only admins can access this feature.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

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
        if (!"Admin".equals(UserSession.getInstance().getRole())) {
            JOptionPane.showMessageDialog(this, "Only admins can access this feature.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

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
        int total = (int) tableModel.getValueAt(selectedRow, 4);
        int available = (int) tableModel.getValueAt(selectedRow, 5);

        // Open update form with selected book data
        UpdateBookForm updateBookForm = new UpdateBookForm(bookId, bookName, author, edition, total, available, this);
        updateBookForm.setVisible(true);
    }

    private void borrowSelectedBook() {
        int selectedRow = bookTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a book to borrow.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int bookId = (int) tableModel.getValueAt(selectedRow, 0);
        int availableCopies = (int) tableModel.getValueAt(selectedRow, 5);
        if (availableCopies == 0) {
            JOptionPane.showMessageDialog(this, "No copies available to borrow.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Check if the current user is an admin
        if ("Admin".equals(UserSession.getInstance().getRole())) {
            String username = JOptionPane.showInputDialog(this, "Enter the username to borrow the book for:", "Borrow Book for User", JOptionPane.QUESTION_MESSAGE);
            if (username == null || username.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Username cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                bookService.borrowBookForUser(bookId, username);
                JOptionPane.showMessageDialog(this, "Book borrowed successfully for user " + username + "!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadBookData(); // Refresh the table data
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error borrowing book: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            try {
                bookService.borrowBook(bookId);
                JOptionPane.showMessageDialog(this, "Book borrowed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadBookData(); // Refresh the table data
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error borrowing book: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void returnSelectedBook() {
        int selectedRow = bookTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a book to return.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int bookId = (int) tableModel.getValueAt(selectedRow, 0);

        try {
            bookService.returnBook(bookId);
            JOptionPane.showMessageDialog(this, "Book returned successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            loadBookData(); // Refresh the table data
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error returning book: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void logout() {
        UserSession.getInstance().logout();
        dispose();
        new LoginForm().setVisible(true);
    }
}
