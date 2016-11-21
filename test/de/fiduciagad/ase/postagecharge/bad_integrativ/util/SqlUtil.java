package de.fiduciagad.ase.postagecharge.bad_integrativ.util;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public final class SqlUtil {

	private SqlUtil() {
		super();
	}

	{
		try {
			Class.forName("org.hsqldb.jdbcDriver");
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	public static Connection connection() throws SQLException {
		return DriverManager.getConnection("jdbc:hsqldb:mem:inmemdatabase",
				"sa", "");
	}

	public static void execUpdate(Connection connection, String expression)
			throws SQLException, IOException {
		try (Statement statement = connection.createStatement()) {
			int rc = statement.executeUpdate(expression);
			if (rc < 0) {
				throw new IOException(rc + " on " + expression);
			}
		}
	}

	public static List<List<Object>> execQuery(Connection connection,
			String expression) throws SQLException, IOException {
		try (Statement statement = connection.createStatement()) {

			List<List<Object>> rows = new ArrayList<List<Object>>();
			for (ResultSet rs = statement.executeQuery(expression); rs.next();) {
				int colmax = rs.getMetaData().getColumnCount();
				List<Object> columData = new ArrayList<>();
				for (int i = 0; i < colmax; i++) {
					columData.add(rs.getObject(i + 1));
				}
				rows.add(columData);
			}
			return rows;
		}
	}

	public static void createDatabase() throws SQLException, IOException {
		try (Connection connection = connection()) {
			execUpdate(
					connection,
					"CREATE TABLE accounts (id INTEGER IDENTITY, name VARCHAR(256), balance DOUBLE)");
			execUpdate(connection,
					"CREATE TABLE ebriefs (id INTEGER IDENTITY, name VARCHAR(256), data BLOB)");
		}

	}

	public static void dropDatabase() throws SQLException {
		try (Connection connection = connection()) {
			try (Statement statement = connection.createStatement()) {
				// statement
				// .execute("TRUNCATE SCHEMA PUBLIC RESTART IDENTITY AND COMMIT NO CHECK");
				statement.execute("DROP SCHEMA PUBLIC CASCADE");
				connection.commit();
			}
		}
	}

}
