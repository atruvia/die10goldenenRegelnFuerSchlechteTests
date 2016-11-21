package de.fiduciagad.ase.postagecharge.hamcrest;

import java.math.BigDecimal;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import de.fiduciagad.ase.postagecharge.PostageCharge;

public final class CentMatcher extends TypeSafeMatcher<PostageCharge> {

	private final int cent;

	private CentMatcher(int cent) {
		this.cent = cent;
	}

	@Override
	public void describeTo(Description description) {
		description.appendValue(cent).appendText(" Cent");
	}

	@Override
	protected boolean matchesSafely(PostageCharge actual) {
		return isEur(actual)
				&& divide(cent, 100).compareTo(actual.getMonAmount()) == 0;
	}

	private boolean isEur(PostageCharge actual) {
		return "EUR".equals(actual.getCurrency().getCurrencyCode());
	}

	private BigDecimal divide(final int cent, int val) {
		return new BigDecimal(cent).divide(new BigDecimal(val));
	}

	public static Matcher<PostageCharge> cent(int cent) {
		return new CentMatcher(cent);
	}

}