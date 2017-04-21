package dal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.mysql.jdbc.ResultSet;
import com.mysql.jdbc.Statement;

public class ConnectionFactory {
	private static final String DRIVER = "com.mysql.jdbc.Driver";
	private static final String DBURL = "jdbc:mysql://localhost:3306/mydb";
	private static final String USER = "root";
	private static final String PASS = "varga13";

	private static ConnectionFactory singleInstance = new ConnectionFactory();

	private ConnectionFactory() {
		try {
			System.out.println("[ConnectionFactory] Loading driver");
			Class.forName(DRIVER);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private Connection createConnection() {
		Connection connection = null;
		try {
			System.out.println("[ConnectionFactory] Connecting to the database");
			connection = DriverManager.getConnection(DBURL, USER, PASS);
		} catch (SQLException e) {
			System.out.println("[ConnectionFactory] An error occured while trying to connect to the database");
			e.printStackTrace();
		}
		return connection;
	}
	
	public static Connection getConnection() {
		return singleInstance.createConnection();
	}
	
	public static void close(Connection connection) {
		if (connection != null) {
			try {
				System.out.println("[ConnectionFactory] closing connection " + connection.toString());
				connection.close();
			} catch (SQLException e) {
				System.out.println("[ConnectionFactory] An error occured while trying to close the connection");
			}
		}
	}

	public static void close(Statement statement) {
		if (statement != null) {
			try {
				System.out.println("[ConnectionFactory] closing statement " + statement.toString());
				statement.close();
				} catch (SQLException e) {
				System.out.println("[ConnectionFactory] An error occured while trying to close the statement");
			}
		}
	}

	public static void close(ResultSet resultSet) {
		if (resultSet != null) {
			try {
				System.out.println("[ConnectionFactory] closing result set " + resultSet.toString());
				resultSet.close();
			} catch (SQLException e) {
				System.out.println("[ConnectionFactory] An error occured while trying to close the ResultSet");
			}
		}
	}

}
