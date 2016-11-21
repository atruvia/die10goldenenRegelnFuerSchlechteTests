package de.fiduciagad.ase.postagecharge.bad_integrativ;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import de.fiduciagad.ase.postagecharge.ebrief.Account;
import de.fiduciagad.ase.postagecharge.ebrief.DataSink;
import de.fiduciagad.ase.postagecharge.ebrief.EBrief;

public class DbmsDataSink implements DataSink {

	private final Connection connection;

	public DbmsDataSink(Connection connection) {
		this.connection = connection;
	}

	@Override
	public void store(Account account, EBrief eBrief) throws IOException {
		try {
			Statement st = connection.createStatement();
			try {
				String expression = "INSERT INTO ebriefs (data) VALUES (hextoraw('"
						+ eBrief.toHex() + "'))";
				int rc = st.executeUpdate(expression);
				if (rc < 0) {
					throw new IOException(rc + " on " + expression);
				}
			} finally {
				st.close();
			}
		} catch (SQLException e) {
			throw new IOException(e);
		}
	}

}
