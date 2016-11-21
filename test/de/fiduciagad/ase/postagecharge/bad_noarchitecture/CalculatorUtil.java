package de.fiduciagad.ase.postagecharge.bad_noarchitecture;

import de.fiduciagad.ase.postagecharge.DefaultPostageChargeCalculator;
import de.fiduciagad.ase.postagecharge.PostageCharge;
import de.fiduciagad.ase.postagecharge.PostageChargeCalculator;
import de.fiduciagad.ase.postagecharge.Shippable;

public final class CalculatorUtil {

	private static final PostageChargeCalculator calculator = new DefaultPostageChargeCalculator();

	private CalculatorUtil() {
		super();
	}

	public static PostageCharge calculatePostageCharge(Shippable shippable) {
		return calculator.calculatePostageCharge(shippable);
	}

}
