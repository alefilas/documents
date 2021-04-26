package ru.alefilas.helper;

import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class DbConnector {

    private static final BasicDataSource ds = new BasicDataSource();

    static {
        ResourceBundle rs = ResourceBundle.getBundle("liquibase");
        ds.setUrl(rs.getString("url"));
        ds.setUsername(rs.getString("username"));
        ds.setPassword(rs.getString("password"));
        ds.setMinIdle(1);
        ds.setMaxIdle(15);
    }

    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

}
