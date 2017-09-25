package com.wsl.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBUtil {
	public static Connection getPostgreConnection(String url, String user, String password) throws ClassNotFoundException, SQLException{
		Class.forName("org.postgresql.Driver");
		Connection connection = DriverManager
	            .getConnection(url, user, password);
		return connection;
	}
	
	public static Statement getStatement(Connection connection) throws SQLException{
		return connection.createStatement();
	}
	
	public static ResultSet query(Statement statement, String sql) throws SQLException{
		ResultSet resultSet = statement.executeQuery(sql);
		return resultSet;
	}
	
	public static ResultSet insert(Statement statement, String sql) throws SQLException{
		statement.executeUpdate(sql);
		return statement.getGeneratedKeys();
	}
}
