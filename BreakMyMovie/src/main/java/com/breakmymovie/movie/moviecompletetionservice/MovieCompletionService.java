package com.breakmymovie.movie.moviecompletetionservice;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.springframework.stereotype.Service;

@Service
public class MovieCompletionService {

	// TODO
	public int insertTheStatusIntoTable(String movieName, int parts) {
		String jdbcURL = "jdbc:mysql://localhost:3306/your_database";
		String username = "your_username";
		String password = "your_password";
		int retVal = 0;
		String insertSQL = "INSERT INTO DUMMY_TABLE (id, movie_name, parts, duration_of_each_part) VALUES (?, ?, ?, ?)";

		try (Connection connection = DriverManager.getConnection(jdbcURL, username, password);
				PreparedStatement statement = connection.prepareStatement(insertSQL)) {

			// Set parameters
			statement.setInt(1, 1);
			statement.setString(2, "John Doe");
			statement.setString(3, "john@example.com");

			// Execute the insert
			retVal = statement.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return retVal;
	}
}
