package ru.alefilas.helper;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;

@Component
public class DbConnector {

    private static BasicDataSource ds;

    @Autowired
    public DbConnector(BasicDataSource ds) {
        DbConnector.ds = ds;
    }

    @PostConstruct
    public void init() {
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
