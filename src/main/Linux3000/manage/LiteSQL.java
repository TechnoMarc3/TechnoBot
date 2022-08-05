package main.Linux3000.manage;



import org.sqlite.SQLiteDataSource;

import java.io.File;
import java.io.IOException;
import java.sql.*;

public class LiteSQL {



	static SQLiteDataSource dataSource;

	public static void createDataSource() {
		File file = new File("datenbank.db");
		if(!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException ioException) {
				ioException.printStackTrace();
			}
		}
		dataSource =new SQLiteDataSource();

		dataSource.setUrl("jdbc:sqlite:" + file.getPath());


	}


	public static SQLiteDataSource getDataSource() {
		return dataSource;
	}

	public static Connection getConnection() throws SQLException {
		return getDataSource().getConnection();

	}

	public static void onUpdate(String sql) {
		try {
			Connection connection = getConnection();
			Statement stmt = connection.createStatement();

			stmt.executeUpdate(sql);

			stmt.close();

			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}



	public static ResultSet onQuery(String sql) {
		ResultSet set = null;
		try {
			Connection connection = getConnection();
			Statement stmt = connection.createStatement();
			set = stmt.executeQuery(sql);


			stmt.close();

			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return set;
	}


}