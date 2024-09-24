package com.ocr.ocrdocs.data;

import com.google.gson.Gson;
import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DataProvider {

    private static DataSource source;
    public static final Gson DEFAULT_GSON = new Gson();

    public static void init(String url) {

        try {

            final PGSimpleDataSource dataSource = new PGSimpleDataSource();
            dataSource.setUrl(url);

            source = dataSource;

        } catch (Exception e) {
            System.out.println("DataSource::init " + e);
        }

    }

    public static Connection getConnection() {
        try {
            return source.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException("DataProvider::getConnection" + e.getMessage());
        }
    }
}
