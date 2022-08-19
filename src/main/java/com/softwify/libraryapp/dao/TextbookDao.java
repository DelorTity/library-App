package com.softwify.libraryapp.dao;

import com.softwify.libraryapp.configuration.DataBaseConfig;
import com.softwify.libraryapp.constants.Queries;
import com.softwify.libraryapp.model.Textbook;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TextbookDao {
    private static final Logger logger = LogManager.getLogger(TextbookDao.class.getSimpleName());

    private final DataBaseConfig dataBaseConfig;

    public TextbookDao(DataBaseConfig dataBaseConfig) {
        this.dataBaseConfig = dataBaseConfig;
    }

    public List<Textbook> getAll() {
        Connection connection = null;
        List<Textbook> textbooks = new ArrayList<>();

        try {
            connection = dataBaseConfig.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(Queries.GET_TEXTBOOKS);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String title = resultSet.getString("title");
                String firstname = resultSet.getString("firstname");
                String lastname = resultSet.getString("lastname");
                Textbook textbook = new Textbook(id, title, firstname, lastname);
                textbooks.add(textbook);
            }
            dataBaseConfig.closeResultSet(resultSet);
            dataBaseConfig.closePreparedStatement(preparedStatement);
        } catch (SQLException | ClassNotFoundException e) {
            logger.error("An error has occurred", e);
        } finally {
            dataBaseConfig.closeConnection(connection);
        }

        return textbooks;
    }

    public boolean deleteTextbook (int id) {
        Connection connection = null;
        boolean deleted = false;
        try {
            connection = dataBaseConfig.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(Queries.DELETE_TEXTBOOK);
            preparedStatement.setInt(1, id);
            int affectedRows = preparedStatement.executeUpdate();
            deleted = affectedRows == 1;
            dataBaseConfig.closePreparedStatement(preparedStatement);
        } catch (SQLException | ClassNotFoundException e) {
            logger.error("An error has occurred", e);
        } finally {
            dataBaseConfig.closeConnection(connection);
        }

        return deleted;
    }

    public Textbook getTextbookInformation(int id) {
        Connection connection = null;
        Textbook textbook = null;
        try {
            connection = dataBaseConfig.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(Queries.GET_TEXTBOOK);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                String title = resultSet.getString("title");
                String firstname = resultSet.getString("firstname");
                String lastname = resultSet.getString("lastname");
                int isbn = resultSet.getInt("isbn");
                String editor = resultSet.getString("editor");
                Date publicationDate = resultSet.getDate("publication_date");
                textbook = new Textbook(title, firstname, lastname, isbn, editor, publicationDate);
            }
            dataBaseConfig.closeResultSet(resultSet);
            dataBaseConfig.closePreparedStatement(preparedStatement);
        } catch (SQLException | ClassNotFoundException e) {
            logger.error("An error has occurred", e);
        } finally {
            dataBaseConfig.closeConnection(connection);
        }

        return textbook;
    }

    public Textbook save(Textbook textbook) {
        Connection connection = null;
        Textbook createdTextbook = null;
        try {
            connection = dataBaseConfig.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(Queries.ADD_TEXTBOOK, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, textbook.getTitle());
            preparedStatement.setInt(2, textbook.getAuthorId());
            preparedStatement.setInt(3, textbook.getIsbn());
            preparedStatement.setString(4, textbook.getEditor());
            java.util.Date publicationDate = textbook.getPublicationDate();
        //    java.util.Date utilPackageDate = new java.util.Date();
            java.sql.Date sqlPackageDate = new java.sql.Date(publicationDate.getTime());
            preparedStatement.setDate(5, sqlPackageDate);
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if(resultSet.next()) {
                int id = resultSet.getInt(1);
                createdTextbook = new Textbook(id, textbook.getAuthorId(), textbook.getTitle(), textbook.getIsbn(), textbook.getEditor(), textbook.getPublicationDate());
            }
            dataBaseConfig.closePreparedStatement(preparedStatement);
        } catch (SQLException | ClassNotFoundException e) {
            logger.error("An error has occurred", e);
        } finally {
            dataBaseConfig.closeConnection(connection);
        }
        return createdTextbook;
    }



}