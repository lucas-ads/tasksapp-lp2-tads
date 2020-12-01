package br.edu.ifms.tasksapp.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
	
	public static Connection getConnection() throws SQLException {
		String dbURL = "jdbc:mysql://localhost:3306/tasksdb?useTimezone=True&serverTimezone=UTC";
		String username = "root";
		String password = "senha";
		
		Connection conexao = DriverManager.getConnection(dbURL, username, password);
		
		return conexao;
	}
}
