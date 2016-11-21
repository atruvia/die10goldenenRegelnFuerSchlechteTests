package de.fiduciagad.ase.postagecharge.bad_noarchitecture;

import static de.fiduciagad.ase.postagecharge.bad_integrativ.util.SqlUtil.connection;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import de.fiduciagad.ase.postagecharge.PostageCharge;
import de.fiduciagad.ase.postagecharge.ebrief.Account;
import de.fiduciagad.ase.postagecharge.ebrief.EBrief;

public class EBriefUploaderBad {

	public void upload(Account account, EBrief eBrief) throws IOException {
		try (Connection connection = connection()) {
			try (Statement statement = connection.createStatement()) {
				String expression = "INSERT INTO ebriefs (data) VALUES (hextoraw('"
						+ eBrief.toHex() + "'))";
				int rc = statement.executeUpdate(expression);
				if (rc < 0) {
					throw new IOException(rc + " on " + expression);
				}
			}
		} catch (SQLException e) {
			throw new IOException(e);
		}

		PostageCharge charge = CalculatorUtil.calculatePostageCharge(eBrief);
		AccountUtil.charge(account, charge);
	}

}
