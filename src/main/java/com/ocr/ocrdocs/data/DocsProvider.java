package com.ocr.ocrdocs.data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DocsProvider {
    private static final String query = "select * from reciever_dict";
    private DocsProvider() {
    }

    public static List<DocsData> getData() {
        List<DocsData> result = new ArrayList<>();
        try (Connection conn = DataProvider.getConnection();
             ResultSet resultSet = conn.createStatement().executeQuery(query)) {

            while (resultSet != null && resultSet.next()){
                DocsData data = new DocsData();
                data.setFirstName(resultSet.getString("first_name"));
                data.setSecondName(resultSet.getString("second_name"));
                result.add(data);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return result;
    }
}
