package com.ocr.ocrdocs.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DocsProvider {
    private static final String queryUsersDict = "select * from reciever_dict";
    private static final String queryInsertDoc = "insert into docs(type, title, url) values(?, ?, ?)";
    private static final String queryLastIdDoc = "select id from docs order by id desc";
    private static final String queryInsertUserDoc =  "insert into docs_reciever(id_doc, id_reciever) values(?,?)";
    private static final String queryUsersDocs = "select * from docs left join docs_reciever on docs.id = docs_reciever.id_doc left join reciever_dict on reciever_dict.id = docs_reciever.id_reciever";

    private DocsProvider() {
    }

    public static List<UserData> getUserData() {
        List<UserData> result = new ArrayList<>();
        try (Connection conn = DataProvider.getConnection();
             ResultSet resultSet = conn.createStatement().executeQuery(queryUsersDict)) {

            while (resultSet != null && resultSet.next()) {
                UserData data = new UserData();
                data.setId(resultSet.getLong("id"));
                data.setFirstName(resultSet.getString("first_name"));
                data.setSecondName(resultSet.getString("second_name"));
                result.add(data);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return result;
    }

    public static List<DocsData> getUsersDocs() {
        List<DocsData> result = new ArrayList<>();
        try (Connection conn = DataProvider.getConnection();
             ResultSet resultSet = conn.createStatement().executeQuery(queryUsersDocs)) {

            while (resultSet != null && resultSet.next()) {
                DocsData data = new DocsData();
                data.setDocTitle(resultSet.getString("title"));
                data.setDocType(resultSet.getString("type"));
                data.setDocUrl(resultSet.getString("url"));
                data.setFirstName(resultSet.getString("first_name"));
                data.setSecondName(resultSet.getString("second_name"));
                result.add(data);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return result;
    }

    public static void saveDoc(String type, String fileTitle, String fileUrl){
        try(Connection conn = DataProvider.getConnection();
            PreparedStatement statement = conn.prepareStatement(queryInsertDoc)) {
            statement.setString(1, type);
            statement.setString(2, fileTitle);
            statement.setString(3, fileUrl);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static Long getLastIdDoc(){
        try(Connection conn = DataProvider.getConnection();
            ResultSet resultSet = conn.createStatement().executeQuery(queryLastIdDoc)) {

            if (resultSet == null || ! resultSet.next() ){
                return null;
            }

            return resultSet.getLong("id");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public static void saveUserDoc(long docId, long userId){
        try(Connection conn = DataProvider.getConnection();
            PreparedStatement statement = conn.prepareStatement(queryInsertUserDoc)) {
            statement.setLong(1, docId);
            statement.setLong(2, userId);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
