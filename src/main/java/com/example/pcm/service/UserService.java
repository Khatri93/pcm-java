package com.example.pcm.service;

import com.example.pcm.dbconfig.DataSourceProvider;
import com.example.pcm.model.Book;
import com.example.pcm.model.Users;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserService {

    public List<Users> getAllUsers() throws SQLException {
        String sql = "SELECT * FROM users";
        List<Users> users = new ArrayList<>();
        try (Connection connection = DataSourceProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Users user = new Users();
                user.setId(resultSet.getInt("id"));
                user.setUsername(resultSet.getString("username"));
                user.setEmail(resultSet.getString("email"));
                user.setRole(resultSet.getString("role"));
                user.setPassword(resultSet.getString("password"));
                users.add(user);
            }
            return users;
        }
    }

    public void updateUser(Users updatedUser) throws SQLException {
        String sql = "UPDATE users SET username = ?, password = ?, email = ?, role = ? WHERE id = ?";
        List<Users> users = new ArrayList<>();
        try (Connection connection = DataSourceProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, updatedUser.getUsername());
            statement.setString(2, updatedUser.getPassword());
            statement.setString(3, updatedUser.getEmail());
            statement.setString(4, updatedUser.getRole());
            statement.setInt(5, updatedUser.getId());
            statement.executeUpdate();
        }
    }

    public void deleteUser(int id) throws SQLException {
        String sql = "DELETE FROM users WHERE id = ?";
        try (Connection connection = DataSourceProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }

    public Users getUserById(int id) throws SQLException {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (Connection connection = DataSourceProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Users user = new Users();
                user.setId(resultSet.getInt("id"));
                user.setUsername(resultSet.getString("username"));
                user.setPassword(resultSet.getString("password"));
                user.setEmail(resultSet.getString("email"));
                user.setRole(resultSet.getString("role"));
                return user;
            }
        }
        return null;
    }
}
