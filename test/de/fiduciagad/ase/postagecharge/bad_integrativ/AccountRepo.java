package de.fiduciagad.ase.postagecharge.bad_integrativ;

import static de.fiduciagad.ase.postagecharge.bad_integrativ.util.SqlUtil.connection;
import static de.fiduciagad.ase.postagecharge.bad_integrativ.util.SqlUtil.execQuery;
import static de.fiduciagad.ase.postagecharge.bad_integrativ.util.SqlUtil.execUpdate;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Currency;
import java.util.List;

import de.fiduciagad.ase.postagecharge.ebrief.Account;

public class AccountRepo {

	public static final String TABLE_NAME = "accounts";

	public static Account createAccount(String name) throws SQLException,
			IOException {
		return new DbsmAccount(connection(), name);
	}

	public static Account byId(String name) throws SQLException, IOException {
		Connection connection = connection();
		List<List<Object>> execQuery = execQuery(connection,
				"SELECT name,balance FROM " + TABLE_NAME + " WHERE name='"
						+ name + "'");
		List<Object> firstRow = execQuery.get(0);
		DbsmAccount dbsmAccount = new DbsmAccount(connection,
				String.valueOf(firstRow.get(0)));
		dbsmAccount.setBalance(Double.parseDouble(String.valueOf(firstRow
				.get(1))));
		return dbsmAccount;
	}

	private static final class DbsmAccount implements Account {

		private final Connection connection;
		private final String name;
		private double balance;

		private DbsmAccount(Connection connection, String name)
				throws SQLException, IOException {
			this.connection = connection;
			this.name = name;
			execUpdate(connection, "INSERT INTO " + TABLE_NAME
					+ " (name,balance) VALUES ('" + name + "', 0)");
		}

		public void setBalance(double balance) {
			this.balance = balance;
		}

		@Override
		public void charge(BigDecimal amount, Currency currency) {
			this.balance -= amount.doubleValue();
			try {
				Statement statement = connection.createStatement();
				try {
					String expression = "UPDATE " + TABLE_NAME
							+ " SET balance = " + balance + " WHERE name='"
							+ name + "'";
					int rc = statement.executeUpdate(expression);
					if (rc < 0) {
						throw new RuntimeException(rc + " on " + expression);
					}
				} finally {
					statement.close();
				}
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}

	}

}
