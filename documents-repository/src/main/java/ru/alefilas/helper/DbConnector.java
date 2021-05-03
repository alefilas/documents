package ru.alefilas.helper;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;

@Component
public class DbConnector {

    private static BasicDataSource ds;

    @Autowired
    public DbConnector(DataSource ds) {
        DbConnector.ds = (BasicDataSource) ds;
    }

    @PostConstruct
    public void init() {
        ds.setMinIdle(1);
        ds.setMaxIdle(15);
    }

    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

}
