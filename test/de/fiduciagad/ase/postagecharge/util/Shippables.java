package de.fiduciagad.ase.postagecharge.util;

import static de.fiduciagad.ase.postagecharge.WeightUnit.GRAMS;
import de.fiduciagad.ase.postagecharge.Shippable;

public final class Shippables {
	
	private Shippables() {
		super();
	}

	public static Shippable forSmallestPossibleStandardLetter() {
		return new LetterBuilder().length(14.0).width(9.0).height(0.0)
				.weight(0, GRAMS).build();
	}

	public static Shippable forLargestPossibleStandardLetter() {
		return new LetterBuilder().length(23.5).width(12.5).height(0.5)
				.weight(20, GRAMS).build();
	}

	public static Shippable forSmallestPossibleCompactLetter() {
		return new LetterBuilder().length(10.0).width(7.0).height(0.0)
				.weight(0, GRAMS).build();
	}

	public static Shippable forLargestPossibleCompactLetter() {
		return new LetterBuilder().length(23.5).width(12.5).height(1.0)
				.weight(50, GRAMS).build();
	}

}
