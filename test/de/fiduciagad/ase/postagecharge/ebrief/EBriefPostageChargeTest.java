package de.fiduciagad.ase.postagecharge.ebrief;

import static de.fiduciagad.ase.postagecharge.ebrief.EBriefs.eBriefWithOneEmptyPage;
import static de.fiduciagad.ase.postagecharge.hamcrest.CentMatcher.cent;
import static de.fiduciagad.ase.postagecharge.util.Shippables.forLargestPossibleStandardLetter;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;

import org.junit.Test;

import de.fiduciagad.ase.postagecharge.DefaultPostageChargeCalculator;
import de.fiduciagad.ase.postagecharge.PostageCharge;
import de.fiduciagad.ase.postagecharge.PostageChargeCalculator;
import de.fiduciagad.ase.postagecharge.Shippable;

public class EBriefPostageChargeTest {

	private final PostageChargeCalculator calculator = new DefaultPostageChargeCalculator();

	// what should the test define? That the charge is 1.05€ or that the service
	// charge is 0.35€?

	@Test
	public void canCalculatePostageChargeForEBrief_Variant1() {
		assertThat(chargeFor(eBriefWithOneEmptyPage()), is(cent(105)));
	}

	@Test
	public void canCalculatePostageChargeForEBrief_Variant2() {
		int eBriefServiceCharge = 35;
		assertThat(chargeFor(eBriefWithOneEmptyPage()),
				is(cent(chargeAsCent(forLargestPossibleStandardLetter())
						+ eBriefServiceCharge)));
	}

	private int chargeAsCent(Shippable shippable) {
		PostageCharge chargeFor = chargeFor(shippable);
		assertThat(chargeFor.getCurrency().getCurrencyCode(), is("EUR"));
		return asCent(chargeFor.getMonAmount());
	}

	private PostageCharge chargeFor(Shippable shippable) {
		return calculator.calculatePostageCharge(shippable);
	}

	private int asCent(BigDecimal chargeFor) {
		return chargeFor.multiply(new BigDecimal(100)).intValue();
	}

}
