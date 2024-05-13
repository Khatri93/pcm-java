package com.example.pcm.dbconfig;

import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DataSourceProvider {

    private static HikariDataSource hikariDataSource;

    static {
        hikariDataSource = new HikariDataSource();
        hikariDataSource.setJdbcUrl(DbConfig.URL);
        hikariDataSource.setUsername(DbConfig.USER);
        hikariDataSource.setPassword(DbConfig.PASS);
        hikariDataSource.setMaximumPoolSize(20);
        hikariDataSource.setMinimumIdle(10);
    }

    public static Connection getConnection() throws SQLException {
        return hikariDataSource.getConnection();
    }
}
