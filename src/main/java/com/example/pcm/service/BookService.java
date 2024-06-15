package com.example.pcm.service;

import com.example.pcm.dbconfig.DataSourceProvider;
import com.example.pcm.model.Book;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookService {

    public void addBook(Book book) throws SQLException {
        String sql = "INSERT INTO books (name, author, edition, total, available) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DataSourceProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, book.getName());
            statement.setString(2, book.getAuthor());
            statement.setString(3, book.getEdition());
            statement.setInt(4, book.getTotal());
            statement.setInt(5, book.getAvailable());
            statement.executeUpdate();
        }
    }

    public void updateBook(Book book) throws SQLException {
        String sql = "UPDATE books SET name = ?, author = ?, edition = ?, total = ?, available = ? WHERE id = ?";
        try (Connection connection = DataSourceProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, book.getName());
            statement.setString(2, book.getAuthor());
            statement.setString(3, book.getEdition());
            statement.setInt(4, book.getTotal());
            statement.setInt(5, book.getAvailable());
            statement.setInt(6, book.getId());
            statement.executeUpdate();
        }
    }

    public List<Book> getAllBooks() throws SQLException {
        String sql = "SELECT * FROM books order by id asc";
        List<Book> books = new ArrayList<>();
        try (Connection connection = DataSourceProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Book book = new Book();
                book.setId(resultSet.getInt("id"));
                book.setName(resultSet.getString("name"));
                book.setAuthor(resultSet.getString("author"));
                book.setEdition(resultSet.getString("edition"));
                book.setTotal(resultSet.getInt("total"));
                book.setAvailable(resultSet.getInt("available"));
                books.add(book);
            }
            return books;
        }
    }

    public void deleteBook(int id) throws SQLException {
        String sql = "DELETE FROM books WHERE id = ?";
        try (Connection connection = DataSourceProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }

    public Book getBookById(int id) throws SQLException {
        String sql = "SELECT * FROM books WHERE id = ?";
        try (Connection connection = DataSourceProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Book book = new Book();
                book.setId(resultSet.getInt("id"));
                book.setName(resultSet.getString("name"));
                book.setAuthor(resultSet.getString("author"));
                book.setEdition(resultSet.getString("edition"));
                book.setTotal(resultSet.getInt("total"));
                book.setAvailable(resultSet.getInt("available"));
                System.out.println(book);
                return book;
            }
        }
        return null;
    }

    public Book getBookByName(String name) throws SQLException {
        String sql = "SELECT * FROM books WHERE name = ?";
        try (Connection connection = DataSourceProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Book book = new Book();
                book.setId(resultSet.getInt("id"));
                book.setName(resultSet.getString("name"));
                book.setAuthor(resultSet.getString("author"));
                book.setEdition(resultSet.getString("edition"));
                book.setTotal(resultSet.getInt("total"));
                book.setAvailable(resultSet.getInt("available"));
                System.out.println(book);
                return book;
            }
        }
        return null;
    }
}

