package de.fiduciagad.ase.postagecharge;

import java.math.BigDecimal;
import java.util.Currency;

public class PostageCharge {

	private final BigDecimal monAmount;
	private final Currency currency;

	public PostageCharge(BigDecimal monAmount, Currency currency) {
		this.monAmount = monAmount;
		this.currency = currency;
	}

	public BigDecimal getMonAmount() {
		return monAmount;
	}

	public Currency getCurrency() {
		return currency;
	}

}
