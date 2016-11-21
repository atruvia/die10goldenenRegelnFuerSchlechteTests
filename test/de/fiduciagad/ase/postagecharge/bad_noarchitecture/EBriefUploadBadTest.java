package de.fiduciagad.ase.postagecharge.bad_noarchitecture;

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

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import de.fiduciagad.ase.postagecharge.bad_integrativ.AccountRepo;

public class EBriefUploadBadTest {

	private static EBriefUploaderBad sut;
	public static Connection connection;
	private static double initialBalance;
	private static String username;

	@BeforeClass
	public static void setup() throws SQLException, IOException {
		createDatabase();
		connection = connection();
		sut = new EBriefUploaderBad();
	}

	@AfterClass
	public static void tearDown() throws SQLException {
		dropDatabase();
	}

	@Test
	public void sendsDataToSink() throws IOException, SQLException {
		username = "testuser";
		initialBalance = 100.00;

		AccountRepo.createAccount(username);
		execUpdate(connection, "UPDATE " + AccountRepo.TABLE_NAME
				+ " SET balance = " + initialBalance + " WHERE name='"
				+ username + "'");
		int amountOfEbriefs = cntEbriefs();

		sut.upload(AccountRepo.byId(username), eBriefWithOneEmptyPage());

		assertThat(cntEbriefs(), is(amountOfEbriefs + 1));
	}

	@Test
	public void chargesAccountWhenDataIsUploadedCorrectly() throws IOException,
			SQLException {

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
