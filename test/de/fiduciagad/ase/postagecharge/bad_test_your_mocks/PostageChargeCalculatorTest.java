package de.fiduciagad.ase.postagecharge.bad_test_your_mocks;

import static de.fiduciagad.ase.postagecharge.Type.LETTER;
import static de.fiduciagad.ase.postagecharge.WeightUnit.GRAMS;
import static de.fiduciagad.ase.postagecharge.hamcrest.CentMatcher.cent;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Currency;

import org.junit.Before;
import org.junit.Test;

import de.fiduciagad.ase.postagecharge.Letter;
import de.fiduciagad.ase.postagecharge.PostageCharge;
import de.fiduciagad.ase.postagecharge.PostageChargeCalculator;

public class PostageChargeCalculatorTest {

	private PostageChargeCalculator calculator = mock(PostageChargeCalculator.class);

	private PostageCharge charge70 = chargeOf("0.70",
			Currency.getInstance("EUR"));
	private PostageCharge charge85 = chargeOf("0.85",
			Currency.getInstance("EUR"));

	private Letter smallestStandardLetter = letterMock(14.0, 9.0, 0.0, 0);
	private Letter largestStandardLetter = letterMock(23.5, 12.5, 0.5, 20);

	private Letter smallestCompactLetter = letterMock(10.0, 7.0, 0.0, 0);
	private Letter largestCompactLetter = letterMock(23.5, 12.5, 1.0, 50);

	@Before
	public void setup() {
		when(calculator.calculatePostageCharge(smallestStandardLetter))
				.thenReturn(charge70);
		when(calculator.calculatePostageCharge(largestStandardLetter))
				.thenReturn(charge70);
		when(calculator.calculatePostageCharge(smallestCompactLetter))
				.thenReturn(charge85);
		when(calculator.calculatePostageCharge(largestCompactLetter))
				.thenReturn(charge85);
	}

	private PostageCharge chargeOf(String amount, Currency currency) {
		PostageCharge charge = mock(PostageCharge.class);
		when(charge.getMonAmount()).thenReturn(new BigDecimal(amount));
		when(charge.getCurrency()).thenReturn(currency);
		return charge;
	}

	@Test
	public void testCalculatePostageCharge_StandardLetter() {
		assertThat(calculator.calculatePostageCharge(smallestStandardLetter),
				is(cent(70)));
		assertThat(calculator.calculatePostageCharge(largestStandardLetter),
				is(cent(70)));
	}

	@Test
	public void testCalculatePostageCharge_compactLetter() {
		assertThat(calculator.calculatePostageCharge(smallestCompactLetter),
				is(cent(85)));
		assertThat(calculator.calculatePostageCharge(largestCompactLetter),
				is(cent(85)));
	}

	private Letter letterMock(double length, double width, double height,
			int gramm) {
		Letter letter = mock(Letter.class);
		when(letter.isType(LETTER)).thenReturn(true);
		when(letter.getLength()).thenReturn(length);
		when(letter.getWidth()).thenReturn(width);
		when(letter.getHeight()).thenReturn(height);
		when(letter.getWeight(GRAMS)).thenReturn(gramm);
		return letter;
	}

}
