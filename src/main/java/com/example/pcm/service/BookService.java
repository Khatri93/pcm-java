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
        String sql = "INSERT INTO books (name, author, edition) VALUES (?, ?, ?)";
        try(Connection connection = DataSourceProvider.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, book.getName());
            statement.setString(2, book.getAuthor());
            statement.setString(3, book.getEdition());
            statement.executeUpdate();
        }
    }

    public void updateBook(Book book) throws SQLException {
        String sql = "UPDATE books SET name = ?, author = ?, edition = ? WHERE id = ?";
        try(Connection connection = DataSourceProvider.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, book.getName());
            statement.setString(2, book.getAuthor());
            statement.setString(3, book.getEdition());
            statement.setInt(4, book.getId());
            statement.executeUpdate();
        }
    }

    public void deleteBook(int id) throws SQLException {
        String sql = "DELETE FROM books WHERE id = ?";
        try(Connection connection = DataSourceProvider.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }

    public List<Book> getAllBooks() throws SQLException {
        String sql = "SELECT * FROM books";
        List<Book> books = new ArrayList<>();
        try(Connection connection = DataSourceProvider.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Book book = new Book();
                book.setId(resultSet.getInt("id"));
                book.setName(resultSet.getString("name"));
                book.setAuthor(resultSet.getString("author"));
                book.setEdition(resultSet.getString("edition"));
                books.add(book);
            }
            return books;
        }
    }
}
