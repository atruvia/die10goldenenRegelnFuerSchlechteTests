package de.fiduciagad.ase.postagecharge;

import static de.fiduciagad.ase.postagecharge.hamcrest.CentMatcher.cent;
import static de.fiduciagad.ase.postagecharge.util.Shippables.forLargestPossibleCompactLetter;
import static de.fiduciagad.ase.postagecharge.util.Shippables.forLargestPossibleStandardLetter;
import static de.fiduciagad.ase.postagecharge.util.Shippables.forSmallestPossibleCompactLetter;
import static de.fiduciagad.ase.postagecharge.util.Shippables.forSmallestPossibleStandardLetter;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class PostageChargeCalculatorTest {

	private final PostageChargeCalculator sut = new DefaultPostageChargeCalculator();

	@Test
	public void canCalculatePostageChargeForSmallestStandardLetter() {
		assertThat(postageCharge(forSmallestPossibleStandardLetter()), is(cent(70)));
	}

	@Test
	public void canCalculatePostageChargeForLargestStandardLetter() {
		assertThat(postageCharge(forLargestPossibleStandardLetter()), is(cent(70)));
	}

	@Test
	public void canCalculatePostageChargeForSmallestCompactLetter() {
		assertThat(postageCharge(forSmallestPossibleCompactLetter()), is(cent(85)));
	}

	@Test
	public void canCalculatePostageChargeForLargestCompactLetter() {
		assertThat(postageCharge(forLargestPossibleCompactLetter()), is(cent(85)));
	}

	private PostageCharge postageCharge(Shippable shippable) {
		return sut.calculatePostageCharge(shippable);
	}

}
