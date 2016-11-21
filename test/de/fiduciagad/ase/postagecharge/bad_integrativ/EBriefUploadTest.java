package de.fiduciagad.ase.postagecharge.bad_integrativ;

import static de.fiduciagad.ase.postagecharge.bad_integrativ.util.SqlUtil.connection;
import static de.fiduciagad.ase.postagecharge.bad_integrativ.util.SqlUtil.createDatabase;
import static de.fiduciagad.ase.postagecharge.bad_integrativ.util.SqlUtil.dropDatabase;
import static de.fiduciagad.ase.postagecharge.bad_integrativ.util.SqlUtil.execQuery;
import static de.fiduciagad.ase.postagecharge.bad_integrativ.util.SqlUtil.execUpdate;
import static de.fiduciagad.ase.postagecharge.ebrief.EBriefs.eBriefWithOneEmptyPage;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import de.fiduciagad.ase.postagecharge.DefaultPostageChargeCalculator;
import de.fiduciagad.ase.postagecharge.ebrief.EBriefUploader;

public class EBriefUploadTest {

	private EBriefUploader sut;
	private Connection connection;

	@Before
	public void setup() throws SQLException, IOException {
		createDatabase();
		connection = connection();
		sut = new EBriefUploader(new DefaultPostageChargeCalculator(),
				new DbmsDataSink(connection));
	}

	@After
	public void tearDown() throws SQLException {
		dropDatabase();
	}

	@Test
	public void sendsDataToSinkAndChargesAccount() throws IOException,
			SQLException {
		String username = "testuser";
		double initialBalance = 100.00;

		AccountRepo.createAccount(username);
		execUpdate(connection, "UPDATE " + AccountRepo.TABLE_NAME
				+ " SET balance = " + initialBalance + " WHERE name='"
				+ username + "'");
		int amountOfEbriefs = cntEbriefs();

		sut.upload(AccountRepo.byId(username), eBriefWithOneEmptyPage());

		assertThat(cntEbriefs(), is(amountOfEbriefs + 1));
		Object newBalance = execQuery(
				connection,
				"SELECT balance FROM " + AccountRepo.TABLE_NAME
						+ " WHERE name = '" + username + "'").get(0).get(0);
		assertThat(newBalance, is((Object) (initialBalance - 1.05)));
	}

	private int cntEbriefs() throws SQLException, IOException {
		return Integer.parseInt(String.valueOf(execQuery(connection,
				"SELECT COUNT(*) FROM ebriefs").get(0).get(0)));
	}

	@Test
	@Ignore
	public void doesNotChargeAccountIfSinkHasErrors() throws IOException,
			SQLException {
		// TODO How to simulate a sink that throws an exception?
	}

}
