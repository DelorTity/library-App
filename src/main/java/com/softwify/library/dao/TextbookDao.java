package com.softwify.library.dao;

import com.softwify.library.configuration.DataBaseConfig;
import com.softwify.library.constants.DBConstants;
import com.softwify.library.model.Author;
import com.softwify.library.model.Textbook;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TextbookDao {
    private static final Logger logger = LogManager.getLogger(TextbookDao.class.getSimpleName());

    private DataBaseConfig dataBaseConfig;

    public TextbookDao(DataBaseConfig dataBaseConfig) {
        this.dataBaseConfig = dataBaseConfig;
    }

    public List<Textbook> getAll() {
        Connection connection = null;
        List<Textbook> textbooks = new ArrayList<>();

        try {
            connection = dataBaseConfig.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(DBConstants.GET_TEXTBOOKS);
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
            PreparedStatement preparedStatement = connection.prepareStatement(DBConstants.DELETE_TEXTBOOK);
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
            PreparedStatement preparedStatement = connection.prepareStatement(DBConstants.GET_TEXTBOOK);
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
            PreparedStatement preparedStatement = connection.prepareStatement(DBConstants.ADD_TEXTBOOK, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, textbook.getTitle());
            preparedStatement.setInt(2, textbook.getAuthorId());
            preparedStatement.setInt(3, textbook.getIsbn());
            preparedStatement.setString(4, textbook.getEditor());
            java.util.Date publicationDate = textbook.getPublicationDate();
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


    public boolean checkExistingAuthor(Author author) {
        Connection connection = null;
        boolean isAuthorExisting = false;
        try {
            connection = dataBaseConfig.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(DBConstants.CHECK_IF_AUTHOR_EXIST);
            preparedStatement.setString(1, author.getFirstName());
            preparedStatement.setString(2, author.getLastName());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                isAuthorExisting = count > 0;
            }
            dataBaseConfig.closeResultSet(resultSet);
            dataBaseConfig.closePreparedStatement(preparedStatement);
        } catch (SQLException | ClassNotFoundException e) {
            logger.error("An error has occurred", e);
        } finally {
            dataBaseConfig.closeConnection(connection);
        }

        return isAuthorExisting;
    }

    public Textbook getValidTextbook(String firstName, String lastName) {
        Connection connection = null;

        Textbook textbook = null;
        try {
            connection = dataBaseConfig.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(DBConstants.GET_AUTHOR);
            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, lastName);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                textbook = new Textbook(id);
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
}
