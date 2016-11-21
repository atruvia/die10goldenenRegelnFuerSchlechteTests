package de.fiduciagad.ase.postagecharge.bad_technical_not_professional;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;
import java.util.Currency;

import org.junit.Test;

import de.fiduciagad.ase.postagecharge.DefaultPostageChargeCalculator;
import de.fiduciagad.ase.postagecharge.Letter;
import de.fiduciagad.ase.postagecharge.PostageCharge;

public class PostageChargeCalculatorTest {

	private Currency expectedCurrency = Currency.getInstance("EUR");
	private DefaultPostageChargeCalculator calculator = new DefaultPostageChargeCalculator();

	@Test
	public void testCalculatePostageChargeOfLetterFrom14_9_0_0_to_23dot5_12dot5_0dot5_20() {
		PostageCharge charge;
		
		charge = calculator.calculatePostageCharge(new Letter(14.0, 9.0, 0.0, 0));
		assertThat(charge.getMonAmount(), is(new BigDecimal("0.70")));
		assertThat(charge.getCurrency(), is(expectedCurrency));

		charge = calculator.calculatePostageCharge(new Letter(23.5, 12.5, 0.5, 20));
		assertThat(charge.getMonAmount(), is(new BigDecimal("0.70")));
		assertThat(charge.getCurrency(), is(expectedCurrency));
	}

	@Test
	public void testCalculatePostageChargeOfLetterFrom10_7_0_0_and_Letter23dot5_12dot5_1_50() {
		PostageCharge charge;
		
		charge = calculator.calculatePostageCharge(new Letter(10.0, 7.0, 0.0, 0));
		assertThat(charge.getMonAmount(), is(new BigDecimal("0.85")));
		assertThat(charge.getCurrency(), is(expectedCurrency));

		charge = calculator.calculatePostageCharge(new Letter(23.5, 12.5, 1.0, 50));
		assertThat(charge.getMonAmount(), is(new BigDecimal("0.85")));
		assertThat(charge.getCurrency(), is(expectedCurrency));
	}

}
