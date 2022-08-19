package com.softwify.libraryAPP.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.softwify.libraryAPP.configuration.DataBaseConfig;
import com.softwify.libraryAPP.constants.Queries;
import com.softwify.libraryAPP.model.Author;

public class AuthorDao {
	private static final Logger logger = LogManager.getLogger(AuthorDao.class.getSimpleName());

	private final DataBaseConfig dataBaseConfig;

	public AuthorDao(DataBaseConfig dataBaseConfig) {
		this.dataBaseConfig = dataBaseConfig;
	}

	public List<Author> getAuthors() {
		Connection connection = null;
		List<Author> authors = new ArrayList<>();

		try {
			connection = dataBaseConfig.getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(Queries.GET_AUTHORS);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				int id = resultSet.getInt("id");
				String firstname = resultSet.getString("firstname");
				String lastname = resultSet.getString("lastname");
				Author author = new Author(id, firstname, lastname);
				authors.add(author);
			}
			dataBaseConfig.closeResultSet(resultSet);
			dataBaseConfig.closePreparedStatement(preparedStatement);
		} catch (SQLException | ClassNotFoundException e) {
			logger.error("An error has occurred", e);
		} finally {
			dataBaseConfig.closeConnection(connection);
		}
		return authors;
	}

	public boolean deleteAuthor(int id) {
		Connection connection = null;
		boolean deleted = false;
		try {
			connection = dataBaseConfig.getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(Queries.DELETE_AUTHOR);
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

	public Author save(Author author) {
		Connection connection = null;
		Author createdAuthor = null;
		try {
			connection = dataBaseConfig.getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(Queries.ADD_AUTHOR, Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setString(1, author.getFirstName());
			preparedStatement.setString(2, author.getLastName());
			preparedStatement.executeUpdate();
			ResultSet resultSet = preparedStatement.getGeneratedKeys();
			if(resultSet.next()) {
				int id = resultSet.getInt(1);
				createdAuthor = new Author(id, author.getFirstName(), author.getLastName());
			}
			dataBaseConfig.closePreparedStatement(preparedStatement);
		} catch (SQLException | ClassNotFoundException e) {
			logger.error("An error has occurred", e);
		} finally {
			dataBaseConfig.closeConnection(connection);
		}
		return createdAuthor;
	}

	public boolean checkExistingAuthor(Author author) {
		Connection connection = null;
		boolean isAuthorExist = false;
		try {
			connection = dataBaseConfig.getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(Queries.CHECK_IF_AUTHOR_EXIST);
			preparedStatement.setString(1, author.getFirstName());
			preparedStatement.setString(2, author.getLastName());
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				int count = resultSet.getInt(1);
				isAuthorExist = count > 0;
			}
			dataBaseConfig.closeResultSet(resultSet);
			dataBaseConfig.closePreparedStatement(preparedStatement);
		} catch (SQLException | ClassNotFoundException e) {
			logger.error("An error has occurred", e);
		} finally {
			dataBaseConfig.closeConnection(connection);
		}

		return isAuthorExist;
	}

	public Author getByFirstNameAndLastName(String firstName, String lastName) {
		Connection connection = null;
		Author author = null;
		try {
			connection = dataBaseConfig.getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(Queries.GET_AUTHOR);
			preparedStatement.setString(1, firstName);
			preparedStatement.setString(2, lastName);
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				int id = resultSet.getInt("id");
				author = new Author(id, firstName, lastName);
			}
			dataBaseConfig.closeResultSet(resultSet);
			dataBaseConfig.closePreparedStatement(preparedStatement);
		} catch (SQLException | ClassNotFoundException e) {
			logger.error("An error has occurred", e);
		} finally {
			dataBaseConfig.closeConnection(connection);
		}

		return author;
	}

}