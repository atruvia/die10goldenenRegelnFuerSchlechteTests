package de.fiduciagad.ase.postagecharge.ebrief;

import static de.fiduciagad.ase.postagecharge.ebrief.EBriefs.eBriefWithOneEmptyPage;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Currency;

import org.junit.Before;
import org.junit.Test;

import de.fiduciagad.ase.postagecharge.PostageCharge;
import de.fiduciagad.ase.postagecharge.PostageChargeCalculator;

public class EBriefUploadTest {

	private final DataSink dataSink = mock(DataSink.class);
	private final PostageChargeCalculator calculator = mock(PostageChargeCalculator.class);
	private final EBriefUploader sut = new EBriefUploader(calculator, dataSink);

	private final Account account = mock(Account.class);
	private final EBrief eBrief = eBriefWithOneEmptyPage();
	private final BigDecimal charge = anyCharge();
	private final Currency chargeCurrency = anyCurrency();

	@Before
	public void setup() {
		when(calculator.calculatePostageCharge(eBrief)).thenReturn(
				new PostageCharge(charge, chargeCurrency));
	}

	@Test
	public void sendsDataToSinkAndChargesAccount() throws IOException {
		sut.upload(account, eBrief);

		verify(dataSink).store(account, eBrief);
		verify(account).charge(charge, chargeCurrency);
	}

	@Test
	public void doesNotChargeAccountIfSinkHasErrors() throws IOException {
		configureSinkToThrowExeption();
		try {
			sut.upload(account, eBrief);
			fail("exception not thrown");
		} catch (IOException e) {
			verify(dataSink).store(account, eBrief);
			verify(account, never()).charge(any(BigDecimal.class),
					any(Currency.class));
		}

	}

	private BigDecimal anyCharge() {
		return new BigDecimal(42);
	}

	private Currency anyCurrency() {
		return Currency.getInstance("USD");
	}

	private void configureSinkToThrowExeption() throws IOException {
		doThrow(IOException.class).when(dataSink).store(account, eBrief);
	}

}
